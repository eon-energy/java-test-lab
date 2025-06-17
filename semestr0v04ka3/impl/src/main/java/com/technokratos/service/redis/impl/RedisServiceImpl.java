package com.technokratos.service.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technokratos.dto.redis.ProblemPayload;
import com.technokratos.exception.redisServiceException.RedisDeserializationException;
import com.technokratos.exception.redisServiceException.RedisSerializationException;
import com.technokratos.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private static final String QUEUE_KEY = "queue:problems";
    private final RedisTemplate<String, String> redisTpl;
    private final ObjectMapper objectMapper;


    @Override
    public void enqueue(UUID id, String solutionLink, String testLink) {
        ProblemPayload payload = new ProblemPayload(id, solutionLink, testLink);
        try {
            String json = objectMapper.writeValueAsString(payload);
            redisTpl.opsForList().leftPush(QUEUE_KEY, json);
            log.info("solution %s in queue".formatted(id));
        } catch (JsonProcessingException e) {
            log.error("Serialization failed ProblemPayload", e);
            throw new RedisSerializationException(e);
        }
    }

    @Override
    public ProblemPayload dequeue(long timeout, TimeUnit unit) {
        try {
            String json = redisTpl.opsForList().rightPop(QUEUE_KEY, timeout, unit);

            //noinspection ConstantConditions
            if (json == null) {
                return null;
            }

            return objectMapper.readValue(json, ProblemPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Deserialization failed ProblemPayload", e);
            throw new RedisDeserializationException(e);
        }
    }

}
