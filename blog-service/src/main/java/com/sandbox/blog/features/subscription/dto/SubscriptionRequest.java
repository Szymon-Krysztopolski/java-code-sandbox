package com.sandbox.blog.server.features.subscription.dto;

import com.sandbox.blog.server.common.JsonDto;

import java.util.UUID;

public record SubscriptionRequest(UUID authorId, UUID subscriberId)
        implements JsonDto {
}
