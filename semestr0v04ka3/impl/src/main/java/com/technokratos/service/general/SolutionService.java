package com.technokratos.service.general;

import com.technokratos.dto.response.solution.SolutionCreateResponse;
import com.technokratos.dto.security.UserDetailsImpl;
import com.technokratos.dto.solution.SolutionCreateDto;
import com.technokratos.dto.response.solution.SolutionResponse;
import com.technokratos.dto.tests.TestResult;
import com.technokratos.entity.enums.SolutionStatusCode;
import com.technokratos.entity.internal.Solution;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface SolutionService {

    SolutionResponse getById(UUID id);

    @Transactional
    SolutionCreateResponse addSolution(com.technokratos.dto.request.SolutionCreateRequest request);

    @Transactional
    UUID addSolution(SolutionCreateDto dto, UserDetailsImpl user);

    Solution findById(UUID id);

    @Transactional
    void updateStatus(UUID id, SolutionStatusCode statusCode);

    @Transactional
    void updateStats(UUID id, TestResult result);
}
