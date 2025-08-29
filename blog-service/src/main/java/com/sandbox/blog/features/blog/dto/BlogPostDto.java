package com.sandbox.blog.server.features.blog.dto;

import com.sandbox.blog.server.common.JsonDto;
import com.sandbox.blog.server.features.blog.domain.BlogPost;
import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record BlogPostDto(
        Long id,
        String content,
        String subject,
        String tags,
        Date modificationDate,
        UUID userId
) implements JsonDto {
    public static BlogPostDto from(BlogPost blogPost) {
        return BlogPostDto.builder()
                .id(blogPost.getPostId())
                .content(blogPost.getContent())
                .subject(blogPost.getSubject())
                .tags(blogPost.getTags())
                .modificationDate(blogPost.getModificationDate())
                .userId(blogPost.getUser().getUserId())
                .build();
    }
}
