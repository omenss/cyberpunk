package com.cyberpunk.rocketmq.consumer.service;

import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerBaseConfig;
import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerSubscribe;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @author lujun
 * @date 2023/8/29 09:30
 */
public abstract class AbstractRocketMqConsumer {


    public void subScribe(RocketMqConsumerBaseConfig consumerBaseConfig, DefaultMQPushConsumer consumer) throws MQClientException {
        for (RocketMqConsumerSubscribe next : consumerBaseConfig.getSubscribes()) {
            if (next.getSql() != null) {
                consumer.subscribe(next.getTopic(), MessageSelector.bySql(next.getSql()));
            } else if (next.getSubExpression() != null) {
                consumer.subscribe(next.getTopic(), next.getSubExpression());
            } else {
                consumer.subscribe(next.getTopic(), next.getTag());
            }
        }
    }

}
