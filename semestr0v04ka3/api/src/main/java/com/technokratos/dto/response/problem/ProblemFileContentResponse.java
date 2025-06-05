package com.technokratos.dto.response.problem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@Schema(description = "Контент файлов задачи")
public class ProblemFileContentResponse {

    private String description;

    private String solutionTemplate;

    private String solutionTest;
}
