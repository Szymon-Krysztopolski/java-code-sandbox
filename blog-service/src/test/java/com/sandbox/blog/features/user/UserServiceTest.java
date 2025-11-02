package com.sandbox.blog.features.user;

import com.sandbox.blog.features.user.domain.User;
import com.sandbox.blog.features.user.exception.UserNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUserWhenUserExists() {
        // given
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .userId(userId)
                .authorName("Test Author")
                .authorUserName("test")
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(user));

        // when
        User result = userService.getUser(userId);

        // then
        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowUserNotFoundException() {
        // given
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // when and then
        assertThrows(UserNotFound.class, () -> userService.getUser(userId));
        verify(userRepository).findById(userId);
    }
}