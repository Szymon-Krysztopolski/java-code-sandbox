package com.sandbox.blog.server.features.subscription;

import com.sandbox.blog.server.common.StatusResponse;
import com.sandbox.blog.server.features.subscription.domain.Subscription;
import com.sandbox.blog.server.features.subscription.dto.SubscriptionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<StatusResponse> addSubscription(@RequestBody SubscriptionRequest request) {
        final Subscription subscription = subscriptionService.addSubscription(request.authorId(), request.subscriberId());
        final StatusResponse response = StatusResponse.builder()
                .id(subscription.getId())
                .message("Subscription added successfully")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity<StatusResponse> deleteSubscription(@RequestBody SubscriptionRequest request) {
        subscriptionService.deleteSubscription(request.authorId(), request.subscriberId());
        final StatusResponse response = StatusResponse.builder()
                .message("Subscription deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
