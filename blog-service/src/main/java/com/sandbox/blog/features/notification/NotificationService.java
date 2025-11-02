package com.sandbox.blog.features.notification;

import com.sandbox.blog.features.notification.domain.Notification;
import com.sandbox.blog.features.subscription.SubscriptionService;
import com.sandbox.blog.features.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.sandbox.blog.features.notification.Constants.NOTIFICATION_QUEUE_NAME;

@Slf4j
@Service
public class NotificationService {
    private final Set<Notification> pendingNotifications = ConcurrentHashMap.newKeySet();
    private final RabbitTemplate rabbitTemplate;
    private final SubscriptionService subscriptionService;

    @Autowired
    public NotificationService(RabbitTemplate rabbitTemplate, SubscriptionService subscriptionService) {
        this.rabbitTemplate = rabbitTemplate;
        this.subscriptionService = subscriptionService;
    }

    public void notifySubscribers(User author) {
        final List<User> subs = subscriptionService.getSubscribers(author.getUserId());
        final Set<Notification> messages = subs.stream()
                .map(user -> {
                    String notificationMessage = String.format("Author %s published something new!", author.getAuthorUserName());
                    return new Notification(user.getUserId(), notificationMessage);
                })
                .collect(Collectors.toSet());

        send(messages);
    }

    private void send(Set<Notification> messages) {
        if (messages.isEmpty()) return;

        log.info("Sending {} notifications", messages.size());
        try {
            rabbitTemplate.convertAndSend(NOTIFICATION_QUEUE_NAME, messages);
        } catch (Exception ex) {
            log.error("Notification service is not working properly", ex);
            pendingNotifications.addAll(messages);
        }
    }

    // Every 30 sec
    @Scheduled(cron = "0/30 * * * * *")
    public void retryPendingNotifications() {
        if (pendingNotifications.isEmpty()) {
            log.debug("No pending notifications to retry.");
            return;
        }

        Set<Notification> notificationsToRetry = new HashSet<>(pendingNotifications);
        pendingNotifications.removeAll(notificationsToRetry);

        log.info("Retrying {} pending notification(s)...", notificationsToRetry.size());
        send(notificationsToRetry);
    }
}
