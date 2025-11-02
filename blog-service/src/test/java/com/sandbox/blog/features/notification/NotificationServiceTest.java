package com.sandbox.blog.features.notification;

import com.sandbox.blog.features.notification.domain.Notification;
import com.sandbox.blog.features.subscription.SubscriptionService;
import com.sandbox.blog.features.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private SubscriptionService subscriptionService;

    @Captor
    private ArgumentCaptor<Set<Notification>> notificationSetCaptor;

    @InjectMocks
    private NotificationService notificationService;

    private final UUID authorId = UUID.randomUUID();
    private final UUID subscriberId1 = UUID.randomUUID();
    private final UUID subscriberId2 = UUID.randomUUID();

    private final User author = new User(authorId, "author@example.com", "AuthorName");
    private final User subscriber1 = new User(subscriberId1, "sub1@example.com", "Sub1");
    private final User subscriber2 = new User(subscriberId2, "sub2@example.com", "Sub2");


    @Test
    void shouldSendNotificationsToSubscribers() {
        // given
        when(subscriptionService.getSubscribers(authorId)).thenReturn(List.of(subscriber1, subscriber2));

        // when
        notificationService.notifySubscribers(author);

        // then
        verify(rabbitTemplate).convertAndSend(eq(Constants.NOTIFICATION_QUEUE_NAME), notificationSetCaptor.capture());
        Set<Notification> sent = notificationSetCaptor.getValue();

        assertEquals(2, sent.size());
        assertTrue(sent.stream().allMatch(n ->
                n.message().contains("Author AuthorName published something new!") &&
                        (List.of(subscriberId1, subscriberId2).contains(n.recipientId()))
        ));
    }

    @Test
    void shouldAddToPendingWhenRabbitFails() {
        // given
        when(subscriptionService.getSubscribers(authorId)).thenReturn(List.of(subscriber1));
        doThrow(new RuntimeException("Rabbit down"))
                .when(rabbitTemplate)
                .convertAndSend(Constants.NOTIFICATION_QUEUE_NAME, "message");

        // when
        notificationService.notifySubscribers(author);

        // then
        // Manually trigger retry
        notificationService.retryPendingNotifications();

        verify(rabbitTemplate, times(2)).convertAndSend(anyString(), Optional.ofNullable(any()));
    }

    @Test
    void shouldNotSendWhenNoSubscribers() {
        // given
        when(subscriptionService.getSubscribers(authorId)).thenReturn(List.of());

        // when
        notificationService.notifySubscribers(author);

        // then
        verifyNoInteractions(rabbitTemplate);
    }

    @Test
    void shouldSkipRetryIfNoPending() {
        // when
        notificationService.retryPendingNotifications();

        // then
        verifyNoInteractions(rabbitTemplate);
    }
}