package com.teaching.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teaching.common.BusinessException;
import com.teaching.common.Constants;
import com.teaching.entity.ResourceDownloadLog;
import com.teaching.entity.ResourceViewLog;
import com.teaching.entity.TeachingResource;
import com.teaching.mapper.ResourceDownloadLogMapper;
import com.teaching.mapper.ResourceViewLogMapper;
import com.teaching.mapper.TeachingResourceMapper;
import com.teaching.service.MinioService;
import com.teaching.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger log = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    private TeachingResourceMapper resourceMapper;

    @Autowired
    private ResourceViewLogMapper viewLogMapper;

    @Autowired
    private ResourceDownloadLogMapper downloadLogMapper;

    @Autowired
    private MinioService minioService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private Object redisGet(String key) {
        try { return redisTemplate.opsForValue().get(key); } catch (Exception e) { log.warn("Redis read failed: {}", e.getMessage()); return null; }
    }

    private void redisSet(String key, Object value, long seconds) {
        try { redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS); } catch (Exception e) { log.warn("Redis write failed: {}", e.getMessage()); }
    }

    private void redisDel(String key) {
        try { redisTemplate.delete(key); } catch (Exception e) { log.warn("Redis delete failed: {}", e.getMessage()); }
    }

    @Override
    public IPage<TeachingResource> getResourcePage(int page, int size, String title, Long categoryId, String fileType, Integer status, Long uploadUserId) {
        return resourceMapper.selectResourcePage(new Page<>(page, size), title, categoryId, fileType, status, uploadUserId);
    }

    @Override
    public TeachingResource getResourceDetail(Long id) {
        String cacheKey = Constants.REDIS_RESOURCE_PREFIX + id;
        Object cached = redisGet(cacheKey);
        if (cached != null) {
            return (TeachingResource) cached;
        }
        TeachingResource resource = resourceMapper.selectResourceDetail(id);
        if (resource == null) {
            throw new BusinessException("资源不存在");
        }
        redisSet(cacheKey, resource, Constants.REDIS_EXPIRE_SECONDS);
        return resource;
    }

    @Override
    public void uploadResource(TeachingResource resource, MultipartFile file) {
        String fileUrl = minioService.uploadFile(file, "resources");
        resource.setFileUrl(fileUrl);
        resource.setFileName(file.getOriginalFilename());
        resource.setFileSize(file.getSize());
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            resource.setFileType(originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase());
        }
        resource.setStatus(Constants.AUDIT_PENDING);
        resource.setDownloadCount(0);
        resource.setViewCount(0);
        resourceMapper.insert(resource);
    }

    @Override
    public void updateResource(TeachingResource resource) {
        resourceMapper.updateById(resource);
        redisDel(Constants.REDIS_RESOURCE_PREFIX + resource.getId());
    }

    @Override
    public void deleteResource(Long id) {
        TeachingResource resource = resourceMapper.selectById(id);
        if (resource != null && resource.getFileUrl() != null) {
            minioService.deleteFile(resource.getFileUrl());
        }
        resourceMapper.deleteById(id);
        redisDel(Constants.REDIS_RESOURCE_PREFIX + id);
    }

    @Override
    public void auditResource(Long id, Integer status) {
        if (status == null || (status != 1 && status != 2)) {
            throw new BusinessException("审核状态参数无效");
        }
        TeachingResource resource = resourceMapper.selectById(id);
        if (resource == null) {
            throw new BusinessException("资源不存在");
        }
        resource.setStatus(status);
        resourceMapper.updateById(resource);
        redisDel(Constants.REDIS_RESOURCE_PREFIX + id);
    }

    @Override
    public String downloadResource(Long resourceId, Long userId) {
        TeachingResource resource = resourceMapper.selectById(resourceId);
        if (resource == null) {
            throw new BusinessException("资源不存在");
        }
        ResourceDownloadLog log = new ResourceDownloadLog();
        log.setResourceId(resourceId);
        log.setUserId(userId);
        downloadLogMapper.insert(log);

        resource.setDownloadCount(resource.getDownloadCount() + 1);
        resourceMapper.updateById(resource);
        redisDel(Constants.REDIS_RESOURCE_PREFIX + resourceId);

        return minioService.getPresignedUrl(resource.getFileUrl());
    }

    @Override
    public void recordView(Long resourceId, Long userId) {
        ResourceViewLog log = new ResourceViewLog();
        log.setResourceId(resourceId);
        log.setUserId(userId);
        viewLogMapper.insert(log);

        TeachingResource resource = resourceMapper.selectById(resourceId);
        if (resource != null) {
            resource.setViewCount(resource.getViewCount() + 1);
            resourceMapper.updateById(resource);
            redisDel(Constants.REDIS_RESOURCE_PREFIX + resourceId);
        }
    }
}
