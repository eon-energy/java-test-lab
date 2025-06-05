package com.technokratos.exception.miniServiceException;

public class MinioReadObjectException extends MinioServiceException{
    public MinioReadObjectException(String key, Throwable cause) {
        super("Failed to read object `%s`".formatted(key), cause);
    }
}
