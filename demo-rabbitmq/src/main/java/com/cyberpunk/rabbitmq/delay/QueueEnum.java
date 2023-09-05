package com.cyberpunk.rabbitmq.delay;

import lombok.Getter;

/**
 * @author lujun
 * @date 2023/9/5 13:52
 */
@Getter
public enum QueueEnum {
    /**
     * delay
     */
    TEST_DELAY("test.delay.queue", "delay");
    /**
     * 队列名称
     */
    private String name;
    /**
     * 队列路由键
     */
    private String routingKey;
    QueueEnum(String name, String routingKey) {
        this.name = name;
        this.routingKey = routingKey;
    }
}
