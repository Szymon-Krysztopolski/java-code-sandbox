package com.sandbox.notifications.features.consumer;

import com.sandbox.notifications.features.consumer.domain.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class NotificationListenerTest {

    @Mock
    private NotificationConsumerService notificationConsumerService;

    @InjectMocks
    private NotificationListener notificationListener;

    @Test
    void shouldProcessAllIncomingNotifications() {
        // given
        Notification n1 = new Notification(UUID.randomUUID(), "Message 1");
        Notification n2 = new Notification(UUID.randomUUID(), "Message 2");
        Notification[] incoming = new Notification[]{n1, n2};

        // when
        notificationListener.listen(incoming);

        // then
        verify(notificationConsumerService).processing(n1);
        verify(notificationConsumerService).processing(n2);
        verifyNoMoreInteractions(notificationConsumerService);
    }
}