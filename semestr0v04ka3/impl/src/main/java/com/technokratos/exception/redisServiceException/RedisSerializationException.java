package com.technokratos.exception.redisServiceException;

public class RedisSerializationException extends RedisServiceException {
    public RedisSerializationException(Throwable t) {
        super("Redis serialization failed", t);
    }
}
