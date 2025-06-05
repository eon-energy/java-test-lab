package com.technokratos.dto.response.problem;

import com.technokratos.dto.response.DifficultyLevelResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@Schema(description = "Полное содержимое задачи")
public class ProblemFullContentResponse {
    private UUID id;
    private String title;
    private DifficultyLevelResponse difficultyLevelResponse;
    private int  maxExecutionTimeMs;
    private int  maxMemoryUsedMb;
    private boolean isPrivate;
    private LocalDateTime createdAt;
    private String description;
    private String solutionTemplate;
    private String solutionTest;
}
