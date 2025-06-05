package com.technokratos.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProblemPayload {
    private final UUID solutionId;
    private final String solutionCodeLink;
    private final String testCodeLink;
}
