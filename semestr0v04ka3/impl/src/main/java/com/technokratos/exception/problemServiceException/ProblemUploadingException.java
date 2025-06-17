package com.technokratos.exception.problemServiceException;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProblemUploadingException extends ProblemServiceException {
    public ProblemUploadingException(Throwable cause) {
        super("Failed problem uploading ", cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

