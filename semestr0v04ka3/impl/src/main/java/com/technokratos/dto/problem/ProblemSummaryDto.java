package com.technokratos.dto.problem;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSummaryDto {

    private UUID id;

    private String title;

    private String difficultyLevel;

    private String difficultyLevelColor;

    private String creator;
}
