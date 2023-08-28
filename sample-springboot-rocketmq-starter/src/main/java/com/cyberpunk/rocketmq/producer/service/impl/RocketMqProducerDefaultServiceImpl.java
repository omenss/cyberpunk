package com.cyberpunk.rocketmq.producer.service.impl;

import com.cyberpunk.rocketmq.producer.config.DefaultMQProducerConfig;
import com.cyberpunk.rocketmq.producer.service.RocketMqProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.util.Assert;


@Slf4j
public class RocketMqProducerDefaultServiceImpl implements RocketMqProducerService {

    private final String producerGroup;

    private final DefaultMQProducer producer;


    public RocketMqProducerDefaultServiceImpl(DefaultMQProducerConfig rocketMqProducerConfig) {
        Assert.notNull(rocketMqProducerConfig.getProducerGroup(), "ProducerGroup setting is not null");
        Assert.notNull(rocketMqProducerConfig.getNamesrvAddr(), "NamesrvAddr setting is not null");
        this.producerGroup = rocketMqProducerConfig.getProducerGroup();
        this.producer = new DefaultMQProducer(rocketMqProducerConfig.getProducerGroup());
        this.producer.setNamesrvAddr(rocketMqProducerConfig.getNamesrvAddr());
        this.producer.setRetryTimesWhenSendFailed(rocketMqProducerConfig.getRetryTimesWhenSendFailed());
        this.producer.setSendMsgTimeout(rocketMqProducerConfig.getSendMsgTimeout());
    }

    public String getProducerGroup() {
        return this.producerGroup;
    }

    public DefaultMQProducer getMqProducer() {
        return this.producer;
    }


    @Override
    public void startProducer() {
        try {
            this.producer.start();
            log.info("RocketMqProducerGroup:{} start success", this.producerGroup);
        } catch (Exception e) {
            log.info("RocketMqProducerGroup:{} start error:{}", this.producerGroup, e.getMessage());
        }
    }

    @Override
    public void shutDownProducer() {
        if (this.producer != null) {
            this.producer.shutdown();
        }
    }
}
