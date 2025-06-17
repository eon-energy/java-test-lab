package com.technokratos.exception.accountServiceException;

import com.technokratos.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class AccountServiceException extends ServiceException {
    public AccountServiceException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
