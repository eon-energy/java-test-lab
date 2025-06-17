package com.technokratos.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "minio")
@Getter
@RequiredArgsConstructor
@Validated
public class MinioProperties {
    @NotBlank
    private final String endpoint;
    @NotBlank
    private final String rootUser;
    @NotBlank
    private final String rootPassword;
    @NotBlank
    private final String bucket;
}
