package com.sandbox.blog.features.blog;

import com.sandbox.blog.features.blog.domain.BlogPost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends CrudRepository<BlogPost, Long> {
}
