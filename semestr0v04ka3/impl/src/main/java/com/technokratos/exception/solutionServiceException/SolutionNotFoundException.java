package com.technokratos.exception.solutionServiceException;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class SolutionNotFoundException extends SolutionServiceException {

    public SolutionNotFoundException(UUID id) {
        super("Solution with id = %s not found".formatted(id.toString()), HttpStatus.NOT_FOUND);
    }
}
