package com.technokratos.dto.response.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Формат ошибки API")
public class ErrorResponse {
    @Schema(description = "HTTP статус код", example = "400")
    private int status;

    @Schema(description = "Сообщение об ошибке", example = "Validation failed")
    private String message;

    @Schema(description = "URL пути запроса", example = "/api/v1/problems")
    private String path;

    @Schema(description = "HTTP статус код", example = "BAD REQUEST")
    private String error;

    @Schema(description = "Ошибки валидации")
    private Map<String, String> details;

    @Schema(description = "Время возникновения ошибки")
    private Instant timestamp;

}
