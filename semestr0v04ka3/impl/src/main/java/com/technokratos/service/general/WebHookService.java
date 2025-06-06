package com.technokratos.service.general;

import org.springframework.scheduling.annotation.Async;

import java.util.UUID;

public interface WebHookService {
    @Async
    void notifyExternalService(String url, String secret, UUID solutionId);
}
