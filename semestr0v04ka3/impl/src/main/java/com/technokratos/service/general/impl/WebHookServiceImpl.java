package com.technokratos.service.general.impl;

import com.technokratos.service.general.WebHookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebHookServiceImpl implements WebHookService {
    @Async
    @Override
    public void notifyExternalService(String url, String secret, UUID solutionId) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> payload = new HashMap<>();
        String URL_PATTERN = "http://localhost:8080/api/v1/solutions/%s";


        payload.put("link", URL_PATTERN.formatted(solutionId));

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Secret", secret);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (RestClientException ex) {
            System.err.println("Failed to send webhook to " + url + ": " + ex.getMessage());
        }


    }


}
