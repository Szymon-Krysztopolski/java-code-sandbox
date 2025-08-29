package com.sandbox.blog.server.features.blog;

import com.sandbox.blog.server.common.StatusResponse;
import com.sandbox.blog.server.features.blog.dto.BlogPostDto;
import com.sandbox.blog.server.features.blog.exception.BlogPostNotFound;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogController.class)
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateBlogPost() throws Exception {
        // given
        BlogPostDto inputDto = BlogPostDto.builder()
                .content("Sample content")
                .subject("Sample subject")
                .tags("java,spring")
                .userId(UUID.randomUUID())
                .build();

        StatusResponse response = StatusResponse.builder()
                .id(1L)
                .message("Created")
                .build();
        when(blogService.createBlogPost(any())).thenReturn(response);

        // when and then
        mockMvc.perform(post("/blogPost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("Created"));
    }

    @Test
    void shouldGetBlogPostById() throws Exception {
        // given
        long id = 1L;
        BlogPostDto dto = BlogPostDto.builder()
                .id(id)
                .content("Test content")
                .subject("Test subject")
                .tags("tag1,tag2")
                .modificationDate(new Date())
                .userId(UUID.randomUUID())
                .build();
        when(blogService.getBlogPost(id)).thenReturn(dto);

        // when and then
        mockMvc.perform(get("/blogPost/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.subject").value("Test subject"));
    }

    @Test
    void shouldReturnNotFoundIfBlogPostMissing() throws Exception {
        // given
        long id = 123L;
        when(blogService.getBlogPost(id))
                .thenThrow(new BlogPostNotFound());

        // when and then
        mockMvc.perform(get("/blogPost/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Blog post not found"));
    }
}
