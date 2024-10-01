package com.app.reminder.utils.services;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HttpRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequest.class);

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> get(String url, String token, String apiKey, Map<String, String> queryParams) {
        HttpHeaders headers = new HttpHeaders();

        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }

        if (apiKey != null && !apiKey.isEmpty()) {
            headers.add("Authorization", "Bearer " + apiKey);
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        if (queryParams != null) {
            for (Map.Entry<String, String> param : queryParams.entrySet()) {
                uriBuilder.queryParam(param.getKey(), param.getValue());
            }
        }

        URI uri = uriBuilder.build().encode().toUri();
        LOGGER.info("Request URL {}", uri);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            System.err.println("Error making GET request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<String> post(String url, Object requestBody, String token) {
        HttpHeaders headers = new HttpHeaders();
        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }

        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
        try {
            return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            System.err.println("Error making POST request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
