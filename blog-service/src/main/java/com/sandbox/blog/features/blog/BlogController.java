package com.sandbox.blog.features.blog;

import com.sandbox.blog.features.blog.dto.BlogPostDto;
import com.sandbox.blog.common.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogPost")
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<StatusResponse> createBlogPost(@RequestBody BlogPostDto dto) {
        final StatusResponse response = blogService.createBlogPost(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDto> getBlogPost(@PathVariable Long id) {
        BlogPostDto dto = blogService.getBlogPost(id);
        return ResponseEntity.ok(dto);
    }
}
