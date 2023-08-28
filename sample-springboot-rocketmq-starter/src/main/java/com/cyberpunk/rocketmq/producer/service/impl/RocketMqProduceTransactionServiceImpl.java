package com.cyberpunk.rocketmq.producer.service.impl;

import com.cyberpunk.rocketmq.listener.RocketMqTransactionListener;
import com.cyberpunk.rocketmq.producer.config.DefaultMQProducerConfig;
import com.cyberpunk.rocketmq.producer.service.RocketMqProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.util.Assert;


/**
 * @author lujun
 * @date 2023/08/28 14:06
 * 事务消息
 */
@Slf4j
public class RocketMqProduceTransactionServiceImpl implements RocketMqProducerService {

    private final String producerGroup;

    private final TransactionMQProducer producer;


    public RocketMqProduceTransactionServiceImpl(DefaultMQProducerConfig rocketMqProducerConfig) {
        Assert.notNull(rocketMqProducerConfig.getProducerGroup(), "ProducerGroup setting is not null");
        Assert.notNull(rocketMqProducerConfig.getNamesrvAddr(), "NamesrvAddr setting is not null");
        this.producerGroup = rocketMqProducerConfig.getProducerGroup();
        this.producer = new TransactionMQProducer(rocketMqProducerConfig.getProducerGroup());
        this.producer.setNamesrvAddr(rocketMqProducerConfig.getNamesrvAddr());
        this.producer.setRetryTimesWhenSendFailed(rocketMqProducerConfig.getRetryTimesWhenSendFailed());
        this.producer.setSendMsgTimeout(rocketMqProducerConfig.getSendMsgTimeout());
        this.producer.setTransactionListener(new RocketMqTransactionListener(producerGroup));
    }

    public String getProducerGroup() {
        return this.producerGroup;
    }

    public TransactionMQProducer getMqProducer() {
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
