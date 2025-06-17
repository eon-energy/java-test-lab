package com.technokratos.exception.redisServiceException;

import com.technokratos.exception.ServiceException;
import org.springframework.http.HttpStatus;


public class RedisServiceException extends ServiceException {
    public RedisServiceException(String message, Throwable t) {
        super(message, t, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
