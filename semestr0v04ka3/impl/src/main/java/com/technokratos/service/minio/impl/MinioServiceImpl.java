package com.technokratos.service.minio.impl;

import com.technokratos.exception.miniServiceException.MinioCopyException;
import com.technokratos.exception.miniServiceException.MinioReadObjectException;
import com.technokratos.exception.miniServiceException.MinioUploadException;
import com.technokratos.exception.miniServiceException.MinioUrlGenerationException;
import com.technokratos.properties.MinioProperties;
import com.technokratos.service.minio.MinioService;
import com.technokratos.util.ContentTypes;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final MinioClient client;
    private final MinioProperties props;

    @Override
    public String uploadFile(MultipartFile file, String bucket, String key) {
        try (InputStream in = file.getInputStream()) {
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(key)
                            .stream(in, file.getSize(), -1)
                            .contentType(
                                    Optional.of(file.getContentType())
                                            .orElse(DEFAULT_CONTENT_TYPE))
                            .build());
            return key;
        } catch (Exception e) {
            throw new MinioUploadException(key, e);
        }
    }

    @Override
    public String uploadText(String content, String bucket, String key) {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);

        try (InputStream stream = new ByteArrayInputStream(data)) {
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(key)
                            .stream(stream, data.length, -1)
                            .contentType(ContentTypes.of(key))
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    @Override
    public void moveFile(String srcBucket, String srcKey, String dstBucket, String dstKet) {
        copyFile(srcBucket, srcKey, dstBucket, dstKet);
        removeFile(srcBucket, srcKey);
    }


    @Override
    public void copyFile(String srcBucket, String srcKey, String dstBucket, String dstKet) {
        try {
            client.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(dstBucket)
                            .object(dstKet)
                            .source(CopySource.builder()
                                    .bucket(srcBucket)
                                    .object(srcKey)
                                    .build())
                            .build());
        } catch (Exception e) {
            throw new MinioCopyException(srcBucket, srcKey, dstBucket, dstKet, e);
        }
    }

    @Override
    public void removeFile(String bucket, String key) {
        try {
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(key)
                            .build());
        } catch (Exception e) {
            log.warn("Cannot remove `{}`: {}", key, e.getMessage());
        }
    }


    @Override
    public String getFileUrl(String bucket,String key) {
        try {
            return client.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(key)
                            .expiry(1, TimeUnit.HOURS)
                            .build());
        } catch (Exception e) {
            throw new MinioUrlGenerationException(key, e);
        }
    }

    @Override
    public String getFileContent(String bucket, String key) {
        try (InputStream stream = client.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .build())) {

            return IOUtils.toString(stream, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new MinioReadObjectException(key, e);
        }

    }

}
