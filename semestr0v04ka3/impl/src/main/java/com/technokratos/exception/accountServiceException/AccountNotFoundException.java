package com.technokratos.exception.accountServiceException;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends AccountServiceException{

    public AccountNotFoundException(String username) {
        super("User with name: %s not found".formatted(username), HttpStatus.BAD_REQUEST);
    }
}
