package com.sandbox.features.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitConsumer {

    @RabbitListener(queues = RabbitConstants.MY_QUEUE)
    public void listen(String in) {
        log.info("Message read from myQueue : {}", in);
    }
}
