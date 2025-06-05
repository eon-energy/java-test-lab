package com.technokratos.exception.problemServiceException;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProblemNotFoundException extends ProblemServiceException {
    public ProblemNotFoundException(UUID id) {
        super("Problem with id = %s not found".formatted(id.toString())
                ,HttpStatus.BAD_REQUEST);
    }
}
