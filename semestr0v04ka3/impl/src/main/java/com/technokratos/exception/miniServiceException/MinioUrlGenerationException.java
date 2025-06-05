package com.technokratos.exception.miniServiceException;

public class MinioUrlGenerationException extends MinioServiceException{
    public MinioUrlGenerationException(String key, Throwable cause) {
        super("Cannot generate URL for `%s`".formatted(key), cause);
    }
}
