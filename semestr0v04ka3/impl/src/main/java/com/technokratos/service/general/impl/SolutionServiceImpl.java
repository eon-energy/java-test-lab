package com.technokratos.service.general.impl;

import com.technokratos.dto.response.solution.SolutionCreateResponse;
import com.technokratos.dto.response.solution.SolutionResponse;
import com.technokratos.dto.security.UserDetailsImpl;
import com.technokratos.dto.solution.SolutionCreateDto;
import com.technokratos.dto.tests.TestResult;
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
import com.technokratos.service.general.WebHookService;
import com.technokratos.service.minio.MinioService;
import com.technokratos.service.redis.RedisService;
import com.technokratos.service.sub.SolutionStatusService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {
    private static final String TMP_PREFIX_PATTERN = "solutions/tmp/%s/solution.java";
    private static final String FINAL_PREFIX_PATTERN = "solutions/%s/solution.java";

    private final SolutionRepository solutionRepository;
    private final ExternalSolutionRepository externalSolutionRepository;

    private final MinioProperties props;

    private final ProblemService problemService;
    private final SolutionStatusService statusService;
    private final MinioService minio;
    private final RedisService redisService;
    private final WebHookService webHookService;

    private final SolutionMapper mapper;

    @Override
    public SolutionResponse getById(UUID id) {
        Solution solution = findById(id);
        SolutionResponse response = mapper.toStatusResponse(solution);
        response.setCode(minio.getFileContent(solution.getBucket(), solution.getPrefix()));
        return response;
    }


    @Override
    @Transactional
    public SolutionCreateResponse addSolution(com.technokratos.dto.request.SolutionCreateRequest request) {
        Solution solution = save(request.getSolutionCode(), request.getProblemId(), null);
        addToRedis(solution);
        log.debug("saving external solution");
        ExternalSolution externalSolution = externalSolutionRepository.save(ExternalSolution.builder()
                .solution(solution)
                .callbackUrl(request.getCallbackUrl())
                .callbackSecret(request.getCallbackSecret())
                .build());
        log.info("external solution saved");
        SolutionCreateResponse createResponse = mapper.toCreateResponse(solution);
        createResponse.setSolutionCode(request.getSolutionCode());
        return createResponse;
    }

    @Override
    @Transactional
    public UUID addSolution(SolutionCreateDto dto, UserDetailsImpl user) {
        Solution solution = save(dto.getSolutionCode(), dto.getProblemId(), user.getAccount());
        addToRedis(solution);
        return solution.getId();
    }

    private void notifyIfExternal(UUID id) {
        Optional<ExternalSolution> externalSolution = externalSolutionRepository.findBySolutionId(id);
        externalSolution.ifPresent(solution -> webHookService.notifyExternalService(
                solution.getCallbackUrl(),
                solution.getCallbackSecret(),
                solution.getSolution().getId()));
    }

    @Override
    public Solution findById(UUID id) {
        return solutionRepository.findById(id)
                .orElseThrow(() -> new SolutionNotFoundException(id));
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, SolutionStatusCode statusCode) {
        SolutionStatus status = statusService.findByCode(statusCode.name());
        solutionRepository.updateStatusById(id, status);
        log.info("solution %s status updated".formatted(id));
        notifyIfExternal(id);
    }

    @Override
    @Transactional
    public void updateStats(UUID id, TestResult result) {
        solutionRepository.updateTestStats(id,
                result.getTotalTests(),
                result.getSkippedTests(),
                result.getStartedTests(),
                result.getAbortedTests(),
                result.getPassedTests(),
                result.getFailedTests(),
                result.getExecutionTimeSeconds(),
                result.getMaxMemoryMb(),
                result.getCompilationErrors()
        );
        updateStatus(id, result.getStatus());
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

    @Transactional
    protected void addToRedis(Solution solution) {
        log.info("Putting in queue");
        try {
            String solutionLink = minio.getFileUrl(solution.getBucket(), solution.getPrefix());
            String testFileLink = problemService.getTestFileLink(solution.getProblem());

            redisService.enqueue(solution.getId(), solutionLink, testFileLink);

            updateStatus(solution.getId(), SolutionStatusCode.QUEUED);

        } catch (Exception e) {
            minio.removeFile(props.getBucket(), solution.getPrefix());
            updateStatus(solution.getId(), SolutionStatusCode.INTERNAL_ERROR);
            throw new SolutionUploadingException(solution.getId(), e);
        }

    }


}
