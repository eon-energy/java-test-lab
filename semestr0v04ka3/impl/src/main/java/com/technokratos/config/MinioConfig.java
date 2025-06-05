package com.technokratos.config;

import com.technokratos.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class MinioConfig {
    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(
                        minioProperties.getRootUser(),
                        minioProperties.getRootPassword())
                .build();
    }

    @Bean
    public ApplicationRunner initializeBucket(MinioClient client) {
        return args -> {
            String bucket = minioProperties.getBucket();
            try {
                boolean exists = client.bucketExists(
                        BucketExistsArgs.builder().bucket(bucket).build()
                );

                if (!exists) {
                    client.makeBucket(
                            MakeBucketArgs.builder().bucket(bucket).build()
                    );
                    log.info("Bucket '{}' created", bucket);
                }
            } catch (ErrorResponseException e) {
                log.error("Minio error: {}", e.getMessage());
                throw new IllegalStateException("Failed to initialize bucket", e);
            }
        };
    }

}
