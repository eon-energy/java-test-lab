package com.technokratos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание задачи")
public class ProblemCreateRequest {
    @NotBlank(message = "Title must not be blank")
    @Schema(description = "Название задачи", example = "Two Sum", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotNull(message = "Execution time is required")
    @Min(value = 1, message = "Execution time must be at least 1 ms")
    @Max(value = 30000, message = "Execution time must not exceed 30000 ms")
    @Schema(description = "Макс. время выполнения (мс)", example = "2000", requiredMode = Schema.RequiredMode.REQUIRED)
    private int maxExecutionTimeMs;

    @NotNull(message = "Memory is required")
    @Min(value = 16, message = "Memory must be at least 16 MB")
    @Max(value = 4096, message = "Memory must not exceed 4096 MB")
    @Schema(description = "Макс. использование памяти (МБ)", example = "256", requiredMode = Schema.RequiredMode.REQUIRED)
    private int maxMemoryUsedMb;

    @NotBlank(message = "Difficulty level is required")
    @Schema(description = "Уровень сложности", example = "EASY", requiredMode = Schema.RequiredMode.REQUIRED)
    private String difficultyLevel;

    @NotBlank
    @Size(max = 50_000)
    @Schema(description = "Описание задачи (Markdown)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @NotBlank
    @Size(max = 50_000)
    @Schema(description = "Шаблон решения", requiredMode = Schema.RequiredMode.REQUIRED)
    private String solutionTemplate;

    @NotBlank
    @Size(max = 50_000)
    @Schema(description = "Тесты для решения", requiredMode = Schema.RequiredMode.REQUIRED)
    private String solutionTest;


}
