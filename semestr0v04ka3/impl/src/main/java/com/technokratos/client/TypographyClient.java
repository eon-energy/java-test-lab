package com.technokratos.client;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TypographyClient {
    public String formatMarkdown(String mdContent) {
        String url = "https://typograf.ru/webservice/";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("text", mdContent);
        params.add("chr", "UTF-8");

        params.add("text", mdContent);
        params.add("chr", "UTF-8");

        params.add("entityType", "3");
        params.add("noTags", "1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Ошибка: " + response.getStatusCode());
        }
    }
}
