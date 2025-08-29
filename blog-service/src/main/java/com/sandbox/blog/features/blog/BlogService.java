package com.sandbox.blog.server.features.blog;

import com.sandbox.blog.server.features.blog.domain.BlogPost;
import com.sandbox.blog.server.features.blog.dto.BlogPostDto;
import com.sandbox.blog.server.common.StatusResponse;
import com.sandbox.blog.server.features.blog.exception.BlogPostNotFound;
import com.sandbox.blog.server.features.notification.NotificationService;
import com.sandbox.blog.server.features.user.UserService;
import com.sandbox.blog.server.features.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BlogService {
    private final BlogRepository repository;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public BlogService(BlogRepository repository, UserService userService, NotificationService notificationService) {
        this.repository = repository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public StatusResponse createBlogPost(BlogPostDto dto) {
        final User user = userService.getUser(dto.userId());
        final BlogPost blogPost = repository.save(BlogPost.from(dto, user));

        notificationService.notifySubscribers(blogPost.getUser());

        return StatusResponse.builder()
                .id(blogPost.getPostId())
                .message("Blog post created successfully!")
                .build();
    }

    public BlogPostDto getBlogPost(Long id) {
        log.info("Getting blog post with id: {}", id);
        return repository.findById(id)
                .map(BlogPostDto::from)
                .orElseThrow(BlogPostNotFound::new);
    }
}
