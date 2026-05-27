package com.teaching.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teaching.entity.TeachingResource;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    IPage<TeachingResource> getResourcePage(int page, int size, String title, Long categoryId, String fileType, Integer status, Long uploadUserId);

    TeachingResource getResourceDetail(Long id);

    void uploadResource(TeachingResource resource, MultipartFile file);

    void updateResource(TeachingResource resource);

    void deleteResource(Long id);

    void auditResource(Long id, Integer status);

    String downloadResource(Long resourceId, Long userId);

    void recordView(Long resourceId, Long userId);
}
