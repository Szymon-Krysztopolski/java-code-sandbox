package com.sandbox.blog.server.features.subscription;

import com.sandbox.blog.server.features.subscription.domain.Subscription;
import com.sandbox.blog.server.features.subscription.domain.SubscriptionId;
import com.sandbox.blog.server.features.subscription.dto.SubscriptionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID authorId = UUID.randomUUID();
    private final UUID subscriberId = UUID.randomUUID();

    @Test
    void shouldAddSubscription() throws Exception {
        // given
        SubscriptionRequest request = new SubscriptionRequest(authorId, subscriberId);
        Subscription subscription = Subscription.builder()
                .id(new SubscriptionId(authorId, subscriberId))
                .build();

        when(subscriptionService.addSubscription(authorId, subscriberId))
                .thenReturn(subscription);

        // when and then
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id.authorId").value(authorId.toString()))
                .andExpect(jsonPath("$.id.subscriberId").value(subscriberId.toString()))
                .andExpect(jsonPath("$.message").value("Subscription added successfully"));
    }

    @Test
    void shouldDeleteSubscription() throws Exception {
        // given
        SubscriptionRequest request = new SubscriptionRequest(authorId, subscriberId);
        doNothing().when(subscriptionService).deleteSubscription(authorId, subscriberId);

        // when and then
        mockMvc.perform(delete("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Subscription deleted successfully"));
    }
}
