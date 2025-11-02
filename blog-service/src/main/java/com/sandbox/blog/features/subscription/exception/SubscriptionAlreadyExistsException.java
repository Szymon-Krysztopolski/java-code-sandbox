package com.sandbox.blog.features.subscription.exception;

public class SubscriptionAlreadyExistsException extends RuntimeException {
    public SubscriptionAlreadyExistsException() {
        super("Subscription already exists");
    }
}
