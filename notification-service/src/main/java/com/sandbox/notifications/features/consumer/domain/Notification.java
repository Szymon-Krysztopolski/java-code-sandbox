package com.sandbox.notifications.features.consumer.domain;

import java.util.UUID;

public record Notification(UUID recipientId, String message) {
}
