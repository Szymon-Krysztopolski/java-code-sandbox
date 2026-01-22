package com.sandbox.features.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestKafkaController {
    private final TestKafkaService service;

    @Autowired
    public TestKafkaController(TestKafkaService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendSampleMessage(@RequestParam String message) {
        service.sendSampleMessage(message);
        return ResponseEntity.ok("Message sent");
    }
}
