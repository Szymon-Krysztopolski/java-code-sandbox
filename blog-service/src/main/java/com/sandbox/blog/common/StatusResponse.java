package com.sandbox.blog.server.common;

import lombok.Builder;

@Builder
public record StatusResponse(
        Object id,
        String message) implements JsonDto {
}
