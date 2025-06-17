package com.technokratos.exception.redisServiceException;

public class RedisDeserializationException extends RedisServiceException {
    public RedisDeserializationException(Throwable t) {
        super("Redis deserialization failed", t);
    }
}
