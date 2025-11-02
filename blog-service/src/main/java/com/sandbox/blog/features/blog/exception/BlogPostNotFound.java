package com.sandbox.blog.features.blog.exception;

public class BlogPostNotFound extends RuntimeException {
    public BlogPostNotFound() {
        super("Blog post not found");
    }
}
