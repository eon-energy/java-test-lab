package com.technokratos.exception.problemServiceException;

import org.springframework.http.HttpStatus;

public class ProblemTemplateLoadingException extends ProblemServiceException{
    public ProblemTemplateLoadingException(Throwable cause) {
        super("Failed to load problem templates from storage", cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
