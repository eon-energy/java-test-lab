package com.technokratos.controller.rest;

import com.technokratos.api.SolutionController;
import com.technokratos.dto.request.SolutionCreateRequest;
import com.technokratos.dto.response.solution.SolutionCreateResponse;
import com.technokratos.dto.response.solution.SolutionResponse;
import com.technokratos.service.general.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/solutions")
@RequiredArgsConstructor
public class SolutionControllerImpl implements SolutionController {
    private final SolutionService service;

    @Override
    public SolutionResponse getById(UUID id) {
        return service.getById(id);
    }

    @Override
    public SolutionCreateResponse addSolution(SolutionCreateRequest solutionCreateRequest) {
        return service.addSolution(solutionCreateRequest);
    }
}
