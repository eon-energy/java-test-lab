package com.technokratos.exception.problemServiceException;

import com.technokratos.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class ProblemServiceException extends ServiceException {
    public ProblemServiceException(String message, Throwable cause, HttpStatus status) {
        super(message, cause, status);
    }
    public ProblemServiceException(String message, HttpStatus status) {
        super(message, status);
    }
}
