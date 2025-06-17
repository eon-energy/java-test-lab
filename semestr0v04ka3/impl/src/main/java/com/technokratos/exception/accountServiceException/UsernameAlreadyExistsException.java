package com.technokratos.exception.accountServiceException;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends AccountServiceException {

    public UsernameAlreadyExistsException(String username) {
        super("username %s already exists".formatted(username), HttpStatus.CONFLICT);
    }
}
