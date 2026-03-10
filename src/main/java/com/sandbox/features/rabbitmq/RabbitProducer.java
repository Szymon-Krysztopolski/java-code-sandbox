package com.sandbox.features.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RabbitProducer {
    private final RabbitTemplate template;

    @Autowired
    public RabbitProducer(RabbitTemplate template) {
        this.template = template;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void send() throws InterruptedException {
        template.convertAndSend(RabbitConstants.MY_QUEUE, "Hello, world!");
        Thread.sleep(1000 + new Random().nextInt(1000));
        template.convertAndSend(RabbitConstants.MY_QUEUE, "Goodbye, world!");
    }
}
