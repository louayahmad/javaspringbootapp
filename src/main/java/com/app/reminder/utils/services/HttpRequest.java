package com.app.reminder.utils.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpRequest {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> get(String url, String token, String apiKey) {
        HttpHeaders headers = new HttpHeaders();

        if (token != null && !token.isEmpty()) {
            headers.setBearerAuth(token);
        }

        if (apiKey != null && !apiKey.isEmpty()) {
            headers.add("Authorization", "Bearer " + apiKey); 
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
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
