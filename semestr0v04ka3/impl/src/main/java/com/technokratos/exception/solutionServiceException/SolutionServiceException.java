package com.technokratos.exception.solutionServiceException;

import com.technokratos.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.rmi.ServerException;

public class SolutionServiceException extends ServiceException {
    public SolutionServiceException(String message, Throwable cause, HttpStatus status) {
        super(message, cause, status);
    }

    public SolutionServiceException(String message, HttpStatus status) {
        super(message, status);
    }
}
