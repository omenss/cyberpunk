package com.cyberpunk.rocketmq.producer.service;

public interface RocketMqProducerService {

    /**
     * 启动生产者
     */
    void startProducer();

    /**
     * 关闭生产者
     */
    void shutDownProducer();
}
