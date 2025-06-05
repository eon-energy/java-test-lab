package com.technokratos.dto.response.solution;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Добавленное решение")
public class SolutionCreateResponse {
    private UUID id;
    private UUID problemId;
    private String solutionCode;
    private LocalDateTime createdAt;
}
