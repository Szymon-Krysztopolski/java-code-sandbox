package com.sandbox.blog.server.features.subscription.exception;

public class SubscriptionAlreadyExistsException extends RuntimeException {
    public SubscriptionAlreadyExistsException() {
        super("Subscription already exists");
    }
}
