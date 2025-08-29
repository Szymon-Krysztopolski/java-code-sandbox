package com.sandbox.notifications.features.consumer;

import com.sandbox.notifications.features.consumer.domain.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class NotificationConsumerService {
    @Async
    public void processing(Notification notification) {
        final UUID userId = notification.recipientId();
        final String message = notification.message();

        log.info("Processing message for user {}", userId);
        long delaySimulation = 5000L + new Random().nextInt(5000);
        try {
            Thread.sleep(delaySimulation);
            log.info("User {} received message \"{}\" after {}s",
                    userId, message, delaySimulation / 1000);
        } catch (Exception ex) {
            log.error("Unexpected error", ex);
        }
    }
}
