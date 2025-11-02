package com.sandbox.blog.features.subscription.exception;

public class SelfSubscriptionException extends RuntimeException {
    public SelfSubscriptionException() {
        super("User cannot subscribe himself");
    }
}
