package com.technokratos.exception.solutionStatusServiceException;

import com.technokratos.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class SolutionStatusServiceException extends ServiceException {
    public SolutionStatusServiceException(String message, HttpStatus status) {
        super(message, status);
    }

    public SolutionStatusServiceException(String message, Throwable cause, HttpStatus status) {
        super(message, cause, status);
    }
}
