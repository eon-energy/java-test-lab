package com.technokratos.dto.response.problem;

import com.technokratos.dto.response.DifficultyLevelResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Id созданной задачи")
public class ProblemCreateResponse {
    private UUID id;
    private String title;
    private LocalDateTime createdAt;
}
