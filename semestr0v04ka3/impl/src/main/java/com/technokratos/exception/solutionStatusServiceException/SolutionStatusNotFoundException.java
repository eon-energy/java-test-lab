package com.technokratos.exception.solutionStatusServiceException;

import org.springframework.http.HttpStatus;

public class SolutionStatusNotFoundException extends SolutionStatusServiceException {
    public SolutionStatusNotFoundException() {
        super("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
