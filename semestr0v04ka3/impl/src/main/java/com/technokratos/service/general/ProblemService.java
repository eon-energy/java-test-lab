package com.technokratos.service.general;

import com.technokratos.dto.request.ProblemCreateRequest;
import com.technokratos.dto.response.problem.ProblemCreateResponse;
import com.technokratos.dto.response.problem.ProblemFileContentResponse;
import com.technokratos.dto.response.problem.ProblemFullContentResponse;
import com.technokratos.dto.response.problem.ProblemSolutionResponse;
import com.technokratos.dto.response.ProblemCreateDto;
import com.technokratos.dto.response.ProblemSummaryDto;
import com.technokratos.dto.security.UserDetailsImpl;
import com.technokratos.entity.internal.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProblemService {

    Page<ProblemSummaryDto> getAll(Pageable pageable);

    ProblemFileContentResponse getTemplates();

    Problem create(UserDetailsImpl userDetails, ProblemCreateDto problemCreateDto);

    String getTestFileUrl(Problem problem);

    ProblemCreateResponse create(ProblemCreateRequest problemCreateDto);

    ProblemFullContentResponse getFullContentById(UUID id);

    ProblemSolutionResponse getForSolvingById(UUID id);

    Problem findById(UUID id);
}
