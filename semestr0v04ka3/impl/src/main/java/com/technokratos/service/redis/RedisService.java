package com.technokratos.service.redis;

import com.technokratos.dto.redis.ProblemPayload;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public interface RedisService {


    void enqueue(UUID id, String solutionLink, String testLink);

    ProblemPayload dequeue(long timeout, TimeUnit unit);
}
