package com.technokratos.service.general.impl;

import com.technokratos.dto.request.ProblemCreateRequest;
import com.technokratos.dto.response.problem.ProblemCreateResponse;
import com.technokratos.dto.response.problem.ProblemFileContentResponse;
import com.technokratos.dto.response.problem.ProblemFullContentResponse;
import com.technokratos.dto.response.problem.ProblemSolutionResponse;
import com.technokratos.dto.response.ProblemCreateDto;
import com.technokratos.dto.response.ProblemSummaryDto;
import com.technokratos.dto.security.UserDetailsImpl;
import com.technokratos.entity.internal.Account;
import com.technokratos.entity.internal.DifficultyLevel;
import com.technokratos.entity.internal.Problem;
import com.technokratos.exception.problemServiceException.ProblemNotFoundException;
import com.technokratos.exception.problemServiceException.ProblemTemplateLoadingException;
import com.technokratos.exception.problemServiceException.ProblemUploadingException;
import com.technokratos.mapper.ProblemMapper;
import com.technokratos.properties.MinioProperties;
import com.technokratos.repository.ProblemRepository;
import com.technokratos.service.sub.DifficultyLevelService;
import com.technokratos.service.general.ProblemService;
import com.technokratos.service.minio.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {
    private static final String DESCRIPTION_SUFFIX = "description.md";
    private static final String SOLUTION_TEMPLATE_SUFFIX = "solution-template.java";
    private static final String SOLUTION_TEST_SUFFIX = "solution-test.java";

    private static final String TEMPLATES_PREFIX = "problems/templates/";
    private static final String TMP_PREFIX_PATTERN = "problems/tmp/%s/";
    private static final String FINAL_PREFIX_PATTERN = "problems/%s/";

    private final MinioProperties props;

    private final MinioService minio;
    private final DifficultyLevelService difficultyService;
    private final ProblemRepository problemRepository;
    private final ProblemMapper mapper;

    @Override
    public ProblemFileContentResponse getTemplates() {
        return getContent(props.getBucket(), TEMPLATES_PREFIX);
    }


    @Override
    public Page<ProblemSummaryDto> getAll(Pageable pageable) {
        //TODO починить n+1
        return problemRepository.findAll(pageable)
                .map(mapper::toSummary);
    }


    @Override
    @Transactional
    public Problem create(UserDetailsImpl user, ProblemCreateDto dto) {
        DifficultyLevel difficulty = difficultyService.findByName(dto.getDifficultyLevel());
        UUID objectId = UUID.randomUUID();

        final String tmpPrefix = TMP_PREFIX_PATTERN.formatted(objectId);
        final String finalPrefix = FINAL_PREFIX_PATTERN.formatted(objectId);

        Account account = null;
        if (user != null) {
            account = user.getAccount();
        }

        Problem problem = Problem.builder()
                .title(dto.getTitle())
                .creator(account)
                .difficulty(difficulty)
                .bucket(props.getBucket())
                .prefix(finalPrefix)
                .maxMemoryUsedMb(dto.getMaxMemoryUsedMb())
                .maxExecutionTimeMs(dto.getMaxExecutionTimeMs())
                .isPrivate(dto.isPersonal())
                .build();
        try {
            uploadAssets(dto, problem.getBucket(), tmpPrefix);

            //todo проверить что оно компилится

            promoteAssets(props.getBucket(), tmpPrefix, props.getBucket(), finalPrefix);
            problem = problemRepository.save(problem);
            log.info("Problem {} uploaded successfully", problem.getId());
            return problem;
        } catch (Exception e) {
            log.error("Uploading problem {} failed, rollback started", problem.getId(), e);
            purgePrefix(props.getBucket(), tmpPrefix);
            purgePrefix(props.getBucket(), finalPrefix);
            throw new ProblemUploadingException(e);
        }
    }

    @Override
    public String getTestFileUrl(Problem problem) {
        return minio.getFileUrl(problem.getBucket(), problem.getPrefix() + SOLUTION_TEST_SUFFIX);
    }

    @Override
    @Transactional
    public ProblemCreateResponse create(ProblemCreateRequest request) {
        ProblemCreateDto dto = mapper.toDto(request);
        dto.setPersonal(true);
        Problem problem = create(new UserDetailsImpl(null), dto);
        return mapper.toCreateResponse(problem);
    }

    @Override
    public ProblemFullContentResponse getFullContentById(UUID id) {
        Problem problem = findById(id);

        ProblemFileContentResponse fileContent = getContent(problem.getBucket(), problem.getPrefix());

        return fullContent(problem, fileContent);
    }

    @Override
    public ProblemSolutionResponse getForSolvingById(UUID id) {
        Problem problem = findById(id);
        ProblemFileContentResponse fileContent = getContent(problem.getBucket(), problem.getPrefix());
        return forSolving(problem, fileContent);
    }

    @Override
    public Problem findById(UUID id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new ProblemNotFoundException(id));
    }


    private ProblemSolutionResponse forSolving(Problem problem, ProblemFileContentResponse fileContent) {
        ProblemSolutionResponse response = mapper.forSolving(problem);
        response.setDescription(fileContent.getDescription());
        response.setSolutionTemplate(fileContent.getSolutionTemplate());
        return response;
    }


    private ProblemFullContentResponse fullContent(Problem problem, ProblemFileContentResponse fileContent) {
        ProblemFullContentResponse response = mapper.toFullContentResponse(problem);
        response.setDescription(fileContent.getDescription());
        response.setSolutionTemplate(fileContent.getSolutionTemplate());
        response.setSolutionTest(fileContent.getSolutionTest());
        return response;
    }


    private ProblemFileContentResponse getContent(String bucket, String prefix) {
        try {
            return ProblemFileContentResponse.builder()
                    .description(minio.getFileContent(bucket, prefix + DESCRIPTION_SUFFIX))
                    .solutionTemplate(minio.getFileContent(bucket, prefix + SOLUTION_TEMPLATE_SUFFIX))
                    .solutionTest(minio.getFileContent(bucket, prefix + SOLUTION_TEST_SUFFIX))
                    .build();
        } catch (Exception e) {
            throw new ProblemTemplateLoadingException(e);
        }
    }

    private void uploadAssets(ProblemCreateDto dto, String bucket, String prefix) {
        minio.uploadText(dto.getDescription(), bucket, prefix + DESCRIPTION_SUFFIX);
        minio.uploadText(dto.getSolutionTemplate(), bucket, prefix + SOLUTION_TEMPLATE_SUFFIX);
        minio.uploadText(dto.getSolutionTest(), bucket, prefix + SOLUTION_TEST_SUFFIX);
    }


    private void promoteAssets(String tmpBucket, String tmpPrefix, String finalBucket, String finalPrefix) {
        minio.moveFile(tmpBucket, tmpPrefix + DESCRIPTION_SUFFIX, finalBucket, finalPrefix + DESCRIPTION_SUFFIX);
        minio.moveFile(tmpBucket, tmpPrefix + SOLUTION_TEMPLATE_SUFFIX, finalBucket, finalPrefix + SOLUTION_TEMPLATE_SUFFIX);
        minio.moveFile(tmpBucket, tmpPrefix + SOLUTION_TEST_SUFFIX, finalBucket, finalPrefix + SOLUTION_TEST_SUFFIX);
    }

    private void purgePrefix(String bucket, String prefix) {
        try {
            minio.removeFile(bucket, prefix + DESCRIPTION_SUFFIX);
            minio.removeFile(bucket, prefix + SOLUTION_TEMPLATE_SUFFIX);
            minio.removeFile(bucket, prefix + SOLUTION_TEST_SUFFIX);
        } catch (Exception ignored) {
        }
    }


}
