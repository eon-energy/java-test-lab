package com.technokratos.exception.solutionServiceException;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class SolutionUploadingException extends SolutionServiceException {
    public SolutionUploadingException(UUID problemId, Throwable cause) {
        super("Failed uploading solution for problem with id = %s".formatted(problemId.toString()), cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
