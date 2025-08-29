package com.sandbox.blog.server.features.user;

import com.sandbox.blog.server.features.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}
