package com.technokratos.controller.rest;

import com.technokratos.api.ProblemController;
import com.technokratos.dto.request.ProblemCreateRequest;
import com.technokratos.dto.response.problem.ProblemCreateResponse;
import com.technokratos.dto.response.problem.ProblemFileContentResponse;
import com.technokratos.dto.response.problem.ProblemFullContentResponse;
import com.technokratos.dto.response.problem.ProblemSolutionResponse;
import com.technokratos.service.general.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemControllerImpl implements ProblemController {
    private final ProblemService problemService;

    @Override
    public ProblemFileContentResponse getTemplates() {
        return problemService.getTemplates();
    }

    @Override
    public ProblemCreateResponse createProblem(@RequestBody @Valid ProblemCreateRequest problemCreateRequest) {
        return problemService.create(problemCreateRequest);
    }

    @Override
    public ProblemSolutionResponse getForSolving(UUID id) {
        return problemService.getForSolvingById(id);
    }

    @Override
    public ProblemFullContentResponse getFullContentById(UUID id) {
        return problemService.getFullContentById(id);
    }

}
