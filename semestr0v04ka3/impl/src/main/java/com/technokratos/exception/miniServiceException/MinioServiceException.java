package com.technokratos.exception.miniServiceException;

import com.technokratos.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class MinioServiceException extends ServiceException {
    public MinioServiceException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
