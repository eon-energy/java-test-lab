package com.technokratos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на отправку решения")
public class SolutionCreateRequest {
    @NotNull(message = "Problem ID is required")
    @Schema(description = "ID задачи", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID problemId;

    @NotBlank(message = "Solution code is required")
    @Size(max = 50_000)
    @Schema(description = "Код решения", requiredMode = Schema.RequiredMode.REQUIRED)
    private String solutionCode;

    @NotBlank(message = "callback url is required")
    @URL(message = "Invalid callback URL format")
    @Schema(description = "Callback URL для уведомлений", example = "https://api.example.com/callback", requiredMode = Schema.RequiredMode.REQUIRED)
    private String callbackUrl;

    @NotBlank(message = "callback secret is required")
    @Size(min = 32, max = 64, message = "Secret must be 32-64 characters")
    @Schema(description = "Секрет для подписи callback", requiredMode = Schema.RequiredMode.REQUIRED)
    private String callbackSecret;
}
