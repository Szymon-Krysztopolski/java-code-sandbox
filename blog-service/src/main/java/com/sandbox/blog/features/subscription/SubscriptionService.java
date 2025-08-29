package com.sandbox.blog.server.features.subscription;

import com.sandbox.blog.server.features.subscription.domain.Subscription;
import com.sandbox.blog.server.features.subscription.domain.SubscriptionId;
import com.sandbox.blog.server.features.subscription.exception.SelfSubscriptionException;
import com.sandbox.blog.server.features.subscription.exception.SubscriptionAlreadyExistsException;
import com.sandbox.blog.server.features.subscription.exception.SubscriptionNotFoundException;
import com.sandbox.blog.server.features.user.UserService;
import com.sandbox.blog.server.features.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SubscriptionService {
    private final SubscriptionRepository repository;
    private final UserService userService;

    public SubscriptionService(SubscriptionRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<User> getSubscribers(UUID authorId) {
        final List<Subscription> subscriptions = repository.findByIdAuthorId(authorId);
        return subscriptions.stream()
                .map(Subscription::getSubscriber)
                .toList();
    }

    public Subscription addSubscription(UUID authorId, UUID subscriberId) throws SubscriptionAlreadyExistsException, SelfSubscriptionException {
        if (Objects.equals(authorId, subscriberId)) {
            throw new SelfSubscriptionException();
        }

        final SubscriptionId id = new SubscriptionId(authorId, subscriberId);
        if (repository.existsById(id)) {
            throw new SubscriptionAlreadyExistsException();
        }

        final User author = userService.getUser(authorId);
        final User subscriber = userService.getUser(subscriberId);
        final Subscription subscription = Subscription.builder()
                .id(id)
                .author(author)
                .subscriber(subscriber)
                .build();

        return repository.save(subscription);
    }

    public void deleteSubscription(UUID authorId, UUID subscriberId) {
        final SubscriptionId id = new SubscriptionId(authorId, subscriberId);
        if (!repository.existsById(id)) {
            throw new SubscriptionNotFoundException();
        }
        repository.deleteById(id);
    }
}
