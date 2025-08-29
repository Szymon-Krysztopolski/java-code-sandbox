package com.sandbox.blog.server.features.subscription;

import com.sandbox.blog.server.features.subscription.domain.Subscription;
import com.sandbox.blog.server.features.subscription.domain.SubscriptionId;
import com.sandbox.blog.server.features.subscription.exception.SelfSubscriptionException;
import com.sandbox.blog.server.features.subscription.exception.SubscriptionAlreadyExistsException;
import com.sandbox.blog.server.features.subscription.exception.SubscriptionNotFoundException;
import com.sandbox.blog.server.features.user.UserService;
import com.sandbox.blog.server.features.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository repository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private final UUID authorId = UUID.randomUUID();
    private final UUID subscriberId = UUID.randomUUID();

    @Test
    void shouldAddSubscriptionSuccessfully() {
        // given
        SubscriptionId id = new SubscriptionId(authorId, subscriberId);

        when(repository.existsById(id)).thenReturn(false);
        when(userService.getUser(authorId)).thenReturn(new User(authorId, "author", "authorUserName"));
        when(userService.getUser(subscriberId)).thenReturn(new User(subscriberId, "subscriber", "subscriberUserName"));

        Subscription expected = Subscription.builder()
                .id(id)
                .author(new User(authorId, "author", "authorUserName"))
                .subscriber(new User(subscriberId, "subscriber", "subscriberUserName"))
                .build();

        when(repository.save(any())).thenReturn(expected);

        // when
        Subscription result = subscriptionService.addSubscription(authorId, subscriberId);

        // then
        assertEquals(expected.getId(), result.getId());
        verify(repository).save(any());
    }

    @Test
    void shouldDeleteSubscriptionSuccessfully() {
        // given
        SubscriptionId id = new SubscriptionId(authorId, subscriberId);
        when(repository.existsById(id)).thenReturn(true);

        // when
        subscriptionService.deleteSubscription(authorId, subscriberId);

        // then
        verify(repository).deleteById(id);
    }

    @Test
    void shouldReturnListOfSubscribers() {
        // given
        User sub1 = new User(UUID.randomUUID(), "user1", "user1");
        User sub2 = new User(UUID.randomUUID(), "user2", "user2");

        List<Subscription> mockSubscriptions = List.of(
                Subscription.builder().subscriber(sub1).build(),
                Subscription.builder().subscriber(sub2).build()
        );

        when(repository.findByIdAuthorId(authorId)).thenReturn(mockSubscriptions);

        // when
        List<User> subscribers = subscriptionService.getSubscribers(authorId);

        // then
        assertEquals(2, subscribers.size());
        assertTrue(subscribers.contains(sub1));
        assertTrue(subscribers.contains(sub2));
    }

    @Test
    void shouldThrowSelfSubscriptionException() {
        // when and then
        assertThrows(SelfSubscriptionException.class,
                () -> subscriptionService.addSubscription(authorId, authorId));
    }

    @Test
    void shouldThrowSubscriptionAlreadyExistsException() {
        // given
        SubscriptionId id = new SubscriptionId(authorId, subscriberId);
        when(repository.existsById(id)).thenReturn(true);

        // when and then
        assertThrows(SubscriptionAlreadyExistsException.class,
                () -> subscriptionService.addSubscription(authorId, subscriberId));
    }

    @Test
    void shouldThrowSubscriptionNotFoundExceptionWhenDeleting() {
        // given
        SubscriptionId id = new SubscriptionId(authorId, subscriberId);
        when(repository.existsById(id)).thenReturn(false);

        // when and then
        assertThrows(SubscriptionNotFoundException.class,
                () -> subscriptionService.deleteSubscription(authorId, subscriberId));
    }
}