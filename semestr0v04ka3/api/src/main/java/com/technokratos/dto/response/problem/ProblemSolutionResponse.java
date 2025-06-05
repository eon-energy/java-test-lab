package com.technokratos.dto.response.problem;

import com.technokratos.dto.response.DifficultyLevelResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Данные для решения задачи")
public class ProblemSolutionResponse {
    private UUID id;
    private String title;
    private DifficultyLevelResponse difficultyLevelResponse;
    private int maxExecutionTimeMs;
    private int maxMemoryUsedMb;
    private boolean isPrivate;
    private String description;
    private String solutionTemplate;
    private LocalDateTime createdAt;

}
