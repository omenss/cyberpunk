package com.cyberpunk.rocketmq.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author lujun
 */
@Target(value = ElementType.TYPE)
@Retention(RUNTIME)
@Component
public @interface RocketTransactionHandler {
    String producerGroup();
}
