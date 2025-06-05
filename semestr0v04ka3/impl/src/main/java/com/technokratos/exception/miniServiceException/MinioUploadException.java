package com.technokratos.exception.miniServiceException;

public class MinioUploadException extends MinioServiceException{
    public MinioUploadException(String key, Throwable cause) {
        super("Failed to upload `%s`".formatted(key), cause);
    }
}
