package com.technokratos.service.minio;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {
    String uploadFile(MultipartFile file, String bucket, String key);

    String uploadText(String content, String bucket, String key);

    void moveFile(String srcBucket, String srcKey, String dstBucket, String dstKet);

    void copyFile(String srcBucket, String srcKey, String dstBucket, String dstKet);

    void removeFile(String bucket, String key);

    String getFileUrl(String bucket,String objectKey);

    String getFileContent(String bucket, String objectKey);

    InputStream getFileStream(String bucket, String key);
}
