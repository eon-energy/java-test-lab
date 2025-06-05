package com.technokratos.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Уровень сложности")
public class DifficultyLevelResponse {
    private String name;
    private String color;
}
