package com.technokratos.exception.miniServiceException;

public class MinioCopyException extends MinioServiceException {
    public MinioCopyException(String srcBucket, String srcKey, String dstBucket, String dstKey, Throwable cause) {
        super("Failed to copy `%s: %s` → `%s: %s`".formatted(srcBucket, srcKey, dstBucket, dstKey), cause);
    }
}
