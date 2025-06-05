package com.technokratos.exception.difficultyServiceException;

import org.springframework.http.HttpStatus;

public class DifficultyLevelNotFoundException extends DifficultyServiceException {

    public DifficultyLevelNotFoundException(String s) {
        super("Difficulty level with name = %s not found".formatted(s), HttpStatus.NOT_FOUND);
    }
}
