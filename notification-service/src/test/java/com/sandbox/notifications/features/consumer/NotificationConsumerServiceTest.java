package com.sandbox.notifications.features.consumer;

import com.sandbox.notifications.features.consumer.domain.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerServiceTest {

    @Spy
    private NotificationConsumerService service;

    @Test
    void shouldCallProcessing() {
        // given
        Notification notification = new Notification(UUID.randomUUID(), "Test message");

        // when
        service.processing(notification);

        // then
        verify(service).processing(notification);
    }
}