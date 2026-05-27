package com.teaching.service;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {

    String uploadFile(MultipartFile file, String directory);

    void deleteFile(String fileUrl);

    String getPresignedUrl(String fileUrl);
}
