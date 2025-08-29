package com.sandbox.blog.server.features.blog;

import com.sandbox.blog.server.common.StatusResponse;
import com.sandbox.blog.server.features.blog.domain.BlogPost;
import com.sandbox.blog.server.features.blog.dto.BlogPostDto;
import com.sandbox.blog.server.features.blog.exception.BlogPostNotFound;
import com.sandbox.blog.server.features.notification.NotificationService;
import com.sandbox.blog.server.features.user.UserService;
import com.sandbox.blog.server.features.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BlogService blogService;

    @Test
    void shouldCreateBlogPostSuccessfully() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "john.doe@example.com", "john");
        BlogPostDto dto = BlogPostDto.builder()
                .subject("Test Subject")
                .content("Test Content")
                .tags("tag1,tag2")
                .userId(userId)
                .build();

        BlogPost savedPost = BlogPost.builder()
                .postId(1L)
                .subject(dto.subject())
                .content(dto.content())
                .tags(dto.tags())
                .user(user)
                .modificationDate(new Date())
                .build();

        when(userService.getUser(userId)).thenReturn(user);
        when(blogRepository.save(any(BlogPost.class))).thenReturn(savedPost);

        // when
        StatusResponse response = blogService.createBlogPost(dto);

        // then
        assertEquals(1L, response.id());
        assertEquals("Blog post created successfully!", response.message());

        verify(notificationService).notifySubscribers(user);
    }

    @Test
    void shouldReturnBlogPostDtoIfExists() {
        // given
        User user = new User(UUID.randomUUID(), "test@example.com", "testUser");
        BlogPost blogPost = BlogPost.builder()
                .postId(1L)
                .subject("Sample Subject")
                .content("Sample Content")
                .tags("tag")
                .user(user)
                .modificationDate(new Date())
                .build();

        when(blogRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        // when
        BlogPostDto result = blogService.getBlogPost(1L);

        // then
        assertEquals(blogPost.getPostId(), result.id());
        assertEquals(blogPost.getContent(), result.content());
        assertEquals(blogPost.getSubject(), result.subject());
        assertEquals(blogPost.getTags(), result.tags());
        assertEquals(blogPost.getUser().getUserId(), result.userId());
    }

    @Test
    void shouldThrowExceptionWhenBlogPostNotFound() {
        // given
        when(blogRepository.findById(1L)).thenReturn(Optional.empty());

        // when and then
        assertThrows(BlogPostNotFound.class, () -> blogService.getBlogPost(1L));
    }
}