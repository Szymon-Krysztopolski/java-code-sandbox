package com.sandbox.blog.features.subscription;

import com.sandbox.blog.features.subscription.domain.Subscription;
import com.sandbox.blog.features.subscription.domain.SubscriptionId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, SubscriptionId> {
    List<Subscription> findByIdAuthorId(UUID authorId);
}
