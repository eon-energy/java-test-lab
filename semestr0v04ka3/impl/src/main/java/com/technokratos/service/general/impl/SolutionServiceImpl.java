package com.technokratos.service.general.impl;

import com.technokratos.dto.response.solution.SolutionResponse;
import com.technokratos.dto.security.UserDetailsImpl;
import com.technokratos.dto.request.SolutionCreateRequest;
import com.technokratos.dto.response.SolutionCreateDto;
import com.technokratos.dto.response.solution.SolutionCreateResponse;
import com.technokratos.entity.enums.SolutionStatusCode;
import com.technokratos.entity.external.ExternalSolution;
import com.technokratos.entity.internal.Account;
import com.technokratos.entity.internal.Problem;
import com.technokratos.entity.internal.Solution;
import com.technokratos.entity.internal.SolutionStatus;
import com.technokratos.exception.solutionServiceException.SolutionNotFoundException;
import com.technokratos.exception.solutionServiceException.SolutionUploadingException;
import com.technokratos.mapper.SolutionMapper;
import com.technokratos.properties.MinioProperties;
import com.technokratos.repository.ExternalSolutionRepository;
import com.technokratos.repository.SolutionRepository;
import com.technokratos.service.general.ProblemService;
import com.technokratos.service.general.SolutionService;
import com.technokratos.service.minio.MinioService;
import com.technokratos.service.redis.RedisService;
import com.technokratos.service.sub.SolutionStatusService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {
    private static final String TMP_PREFIX_PATTERN = "solutions/tmp/%s/solution.java";
    private static final String FINAL_PREFIX_PATTERN = "solutions/%s/solution.java";

    private final SolutionRepository solutionRepository;
    private final ExternalSolutionRepository externalSolutionRepository;

    private final ProblemService problemService;
    private final SolutionStatusService statusService;
    private final MinioService minio;
    private final MinioProperties props;
    private final RedisService redisService;
    private final SolutionMapper mapper;

    @Override
    public SolutionResponse getById(UUID id) {
        Solution solution = solutionRepository.findById(id)
                .orElseThrow(() -> new SolutionNotFoundException(id));
        SolutionResponse response = mapper.toStatusResponse(solution);
        response.setCode(minio.getFileContent(solution.getBucket(), solution.getPrefix()));
        return response;
    }


    @Override
    @Transactional
    public SolutionCreateResponse addSolution(SolutionCreateRequest request) {
        Solution solution = save(request.getSolutionCode(), request.getProblemId(), null);
        addToRedis(solution);
        externalSolutionRepository.save(ExternalSolution.builder()
                .solution(solution)
                .callbackUrl(request.getCallbackUrl())
                .callbackSecret(request.getCallbackSecret())
                .build());
        SolutionCreateResponse createResponse = mapper.toCreateResponse(solution);
        createResponse.setSolutionCode(request.getSolutionCode());
        return createResponse;
    }

    @Override
    @Transactional
    public UUID addSolution(SolutionCreateDto dto, UserDetailsImpl user) {
        Solution solution = save(dto.getSolutionCode(), dto.getProblemId(), user.getAccount());
        return solution.getId();
    }

    public void updateStatus(UUID id, SolutionStatusCode statusCode) {
        SolutionStatus status = statusService.findByCode(statusCode.name());
        solutionRepository.updateStatusById(id, status);
    }


    private Solution save(String content, UUID problemId, @Nullable Account account) {
        Problem problem = problemService.findById(problemId);
        SolutionStatus submitted = statusService.findByCode(SolutionStatusCode.SUBMITTED.name());

        UUID objectId = UUID.randomUUID();

        final String tmpPrefix = TMP_PREFIX_PATTERN.formatted(objectId);
        final String finalPrefix = FINAL_PREFIX_PATTERN.formatted(objectId);

        Solution solution = Solution.builder()
                .sender(account)
                .problem(problem)
                .bucket(props.getBucket())
                .prefix(finalPrefix)
                .status(submitted)
                .build();


        try {
            minio.uploadText(content, props.getBucket(), tmpPrefix);
            minio.moveFile(props.getBucket(), tmpPrefix, props.getBucket(), finalPrefix);
            solutionRepository.save(solution);
            log.info("Solution {} uploaded successfully", problem.getId());
            return solution;
        } catch (Exception e) {
            minio.removeFile(props.getBucket(), tmpPrefix);
            minio.removeFile(props.getBucket(), finalPrefix);
            log.error("Uploading solution {} failed, rollback started", problem.getId(), e);
            throw new SolutionUploadingException(problem.getId(), e);
        }
    }

    private void addToRedis(Solution solution) {
        log.info("Putting in queue");
        try {
            String solutionUrl = minio.getFileUrl(solution.getBucket(), solution.getPrefix());
            String testFileUrl = problemService.getTestFileUrl(solution.getProblem());
            redisService.enqueue(solution.getId(), testFileUrl, solutionUrl);
        } catch (Exception e) {
            minio.removeFile(props.getBucket(), solution.getPrefix());
            updateStatus(solution.getId(), SolutionStatusCode.INTERNAL_ERROR);
            throw new SolutionUploadingException(solution.getId(), e);
        }
        updateStatus(solution.getId(), SolutionStatusCode.QUEUED);

    }


}
