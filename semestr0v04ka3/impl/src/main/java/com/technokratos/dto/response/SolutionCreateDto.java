package com.technokratos.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolutionCreateDto {

    @NotNull(message = "Problem ID is required")
    private UUID problemId;

    @NotBlank(message = "Solution code is required")
    @Size(max = 50_000)
    private String solutionCode;
}
