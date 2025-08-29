package com.sandbox.blog.server.features.notification.domain;

import java.util.UUID;

public record Notification(UUID recipientId, String message) {
}
