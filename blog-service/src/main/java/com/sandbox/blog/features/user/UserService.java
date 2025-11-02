package com.sandbox.blog.features.user;

import com.sandbox.blog.features.user.domain.User;
import com.sandbox.blog.features.user.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getUser(UUID id) {
        return repository.findById(id)
                .orElseThrow(UserNotFound::new);
    }
}
