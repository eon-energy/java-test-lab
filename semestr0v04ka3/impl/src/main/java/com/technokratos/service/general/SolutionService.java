package com.technokratos.service.general;

import com.technokratos.dto.security.UserDetailsImpl;
import com.technokratos.dto.request.SolutionCreateRequest;
import com.technokratos.dto.response.SolutionCreateDto;
import com.technokratos.dto.response.solution.SolutionCreateResponse;
import com.technokratos.dto.response.solution.SolutionResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface SolutionService {

    SolutionResponse getById(UUID id);

    @Transactional
    SolutionCreateResponse addSolution(SolutionCreateRequest request);

    @Transactional
    UUID addSolution(SolutionCreateDto dto, UserDetailsImpl user);
}
