package com.sandbox.blog.server.features.subscription.domain;

import com.sandbox.blog.server.features.user.domain.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscriptions")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @EmbeddedId
    private SubscriptionId id;

    @MapsId("authorId")
    @OneToOne
    @JoinColumn(name = "authorId", insertable = false, updatable = false)
    private User author;

    @MapsId("subscriberId")
    @OneToOne
    @JoinColumn(name = "subscriberId", insertable = false, updatable = false)
    private User subscriber;
}
