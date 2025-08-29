package com.sandbox.notifications.features.consumer;

import com.sandbox.notifications.features.consumer.domain.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.sandbox.notifications.features.consumer.Constants.NOTIFICATION_QUEUE_NAME;

@Slf4j
@Service
public class NotificationListener {
    private final NotificationConsumerService notificationConsumerService;

    public NotificationListener(NotificationConsumerService notificationConsumerService) {
        this.notificationConsumerService = notificationConsumerService;
    }

    @RabbitListener(queues = NOTIFICATION_QUEUE_NAME)
    public void listen(Notification[] in) {
        log.info("Getting new {} notifications", in.length);
        Arrays.stream(in).forEach(notificationConsumerService::processing);
    }
}
