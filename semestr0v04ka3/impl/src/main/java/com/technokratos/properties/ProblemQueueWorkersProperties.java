package com.technokratos.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "problem-queue-workers")
@Getter
@RequiredArgsConstructor
@Validated
public class ProblemQueueWorkersProperties {
    private final int workersCount;
    private final int redisDequeueTimeoutSeconds;
}
