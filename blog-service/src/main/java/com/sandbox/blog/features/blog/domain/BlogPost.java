package com.sandbox.blog.server.features.blog.domain;

import com.sandbox.blog.server.features.blog.dto.BlogPostDto;
import com.sandbox.blog.server.features.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String content;
    private String subject;
    private String tags;

    @Temporal(TemporalType.DATE)
    private Date modificationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public static BlogPost from(BlogPostDto dto, User user) {
        return BlogPost.builder()
                .content(dto.content())
                .subject(dto.subject())
                .tags(dto.tags())
                .modificationDate(Date.from(Instant.now()))
                .user(user)
                .build();
    }
}
