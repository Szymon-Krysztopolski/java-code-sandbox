package com.sandbox.blog.server.features.subscription.exception;

public class SelfSubscriptionException extends RuntimeException {
    public SelfSubscriptionException() {
        super("User cannot subscribe himself");
    }
}
