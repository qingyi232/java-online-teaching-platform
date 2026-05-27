package com.teaching.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teaching.common.Result;
import com.teaching.entity.TeachingResource;
import com.teaching.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/list")
    public Result<IPage<TeachingResource>> getResourcePage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long uploadUserId) {
        return Result.success(resourceService.getResourcePage(page, size, title, categoryId, fileType, status, uploadUserId));
    }

    @GetMapping("/detail/{id}")
    public Result<TeachingResource> getResourceDetail(@PathVariable Long id) {
        return Result.success(resourceService.getResourceDetail(id));
    }

    @PostMapping("/upload")
    public Result<?> uploadResource(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        TeachingResource resource = new TeachingResource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setCategoryId(categoryId);
        resource.setUploadUserId(userId);
        resourceService.uploadResource(resource, file);
        return Result.success("上传成功，等待审核");
    }

    @PutMapping("/update")
    public Result<?> updateResource(@RequestBody TeachingResource resource) {
        resourceService.updateResource(resource);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return Result.success("删除成功");
    }

    @PutMapping("/audit/{id}")
    public Result<?> auditResource(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        resourceService.auditResource(id, params.get("status"));
        return Result.success("审核完成");
    }

    @GetMapping("/download/{id}")
    public Result<Map<String, String>> downloadResource(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String url = resourceService.downloadResource(id, userId);
        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        return Result.success(data);
    }

    @PostMapping("/view/{id}")
    public Result<?> recordView(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        resourceService.recordView(id, userId);
        return Result.success();
    }
}
