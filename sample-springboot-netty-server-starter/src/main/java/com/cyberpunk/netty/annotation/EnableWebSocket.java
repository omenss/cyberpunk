package com.cyberpunk.netty.annotation;

import com.cyberpunk.netty.config.NettyWebSocketSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lujun
 * @date 2023/8/18 13:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(NettyWebSocketSelector.class)
public @interface EnableWebSocket {

    String[] scanBasePackages() default {};
}
