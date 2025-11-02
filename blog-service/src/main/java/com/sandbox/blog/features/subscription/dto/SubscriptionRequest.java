package com.sandbox.blog.features.subscription.dto;

import com.sandbox.blog.common.JsonDto;

import java.util.UUID;

public record SubscriptionRequest(UUID authorId, UUID subscriberId)
        implements JsonDto {
}
