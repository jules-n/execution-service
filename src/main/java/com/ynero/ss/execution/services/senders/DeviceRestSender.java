package com.ynero.ss.execution.services.senders;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeviceRestSender {
    private final RestTemplate restTemplate;

    public DeviceRestSender() {
        this.restTemplate = new RestTemplate();
    }

    public void sendToDeviceByUrl(String port, Object resultOfPipeline) {
        HttpEntity<Object> request = new HttpEntity<>(resultOfPipeline);
        restTemplate.postForObject(port, request, ResponseEntity.class);
    }
}
