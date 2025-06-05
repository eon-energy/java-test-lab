package com.technokratos.dto.response.solution;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private long executionTimeMs;
    private long memoryUsedBytes;
    private String code;
    private String logMessage;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
