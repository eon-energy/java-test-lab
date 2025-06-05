package com.technokratos.dto.response;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
public class ProblemCreateDto {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @Min(value = 1, message = "Execution time must be at least 1 ms")
    @Max(value = 30000, message = "Execution time must not exceed 30000 ms")
    private int maxExecutionTimeMs;

    @Min(value = 16, message = "Memory must be at least 16 MB")
    @Max(value = 4096, message = "Memory must not exceed 4096 MB")
    private int maxMemoryUsedMb;

    @NotBlank(message = "Difficulty level is required")
    private String difficultyLevel;

    @NotBlank
    @Size(max = 50_000)
    private String description;

    @NotBlank
    @Size(max = 50_000)
    private String solutionTemplate;

    @NotBlank
    @Size(max = 50_000)
    private String solutionTest;

    @NotNull(message = "isPrivate field is required")
    private boolean personal;

}
