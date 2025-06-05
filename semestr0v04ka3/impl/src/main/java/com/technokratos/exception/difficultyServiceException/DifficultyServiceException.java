package com.technokratos.exception.difficultyServiceException;

import com.technokratos.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class DifficultyServiceException extends ServiceException {
    public DifficultyServiceException(String s, HttpStatus status) {
        super(s, status);
    }
}
