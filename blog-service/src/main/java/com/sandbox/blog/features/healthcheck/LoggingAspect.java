package com.sandbox.blog.features.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {
    }

    @Before("restControllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Calling: {}()", joinPoint.getSignature().getName());
    }

    @AfterReturning("restControllerMethods()")
    public void logAfterReturning(JoinPoint joinPoint) {
        log.info("Completed: {}()", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "restControllerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("Error in: {}() - {}", joinPoint.getSignature().getName(), ex.getMessage());
    }
}
