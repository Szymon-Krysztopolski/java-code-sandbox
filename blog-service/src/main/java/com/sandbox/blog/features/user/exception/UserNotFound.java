package com.sandbox.blog.server.features.user.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super("User not found");
    }
}
