package com.sandbox.features.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class TestKafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    @Autowired
    public TestKafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSampleMessage(String message) {
        log.info("Processing...");
        try {
            Thread.sleep(1000 + new Random().nextInt(100));
        } catch (InterruptedException e) {
            log.error("Sleep exception.", e);
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(topicName, message + "-" + UUID.randomUUID());
    }
}
