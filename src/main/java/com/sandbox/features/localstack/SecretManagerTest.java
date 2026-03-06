package com.sandbox.features.localstack;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecretManagerTest {
    @Value("${localstack.my-test-1}")
    private String testA;

    @Value("${localstack.my-test-2}")
    private String testB;

    @PostConstruct
    public void test() {
        log.info("LocalstackTestA: {}", testA);
        log.info("LocalstackTestB: {}", testB);
    }
}
