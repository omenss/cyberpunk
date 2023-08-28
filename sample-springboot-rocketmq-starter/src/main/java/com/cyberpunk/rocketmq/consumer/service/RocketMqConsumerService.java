package com.cyberpunk.rocketmq.consumer.service;


public interface RocketMqConsumerService {

    /**
     * 启动消费者
     */
    void startConsumer();

    /**
     * 关闭消费者
     */
    void shutDownConsumer();
}
