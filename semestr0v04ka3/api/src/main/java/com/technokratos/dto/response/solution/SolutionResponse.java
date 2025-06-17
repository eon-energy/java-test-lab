package com.technokratos.dto.response.solution;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@Schema(description = "Статус решения задачи")
public class SolutionResponse {
    private UUID id;
    private UUID problemId;
    private String statusCode;
    private int totalTests;
    private int skippedTests;
    private int startedTests;
    private int abortedTests;
    private int passedTests;
    private int failedTests;
    private long executionTimeMs;
    private long memoryUsedBytes;
    private String code;
    private String logMessage;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
