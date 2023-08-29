package com.cyberpunk.rocketmq.consumer.service.impl;

import com.cyberpunk.rocketmq.consumer.RocketMqMsgHandler;
import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerBaseConfig;
import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerConfig;
import com.cyberpunk.rocketmq.consumer.service.AbstractRocketMqConsumer;
import com.cyberpunk.rocketmq.consumer.service.RocketMqConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @author lujun
 * @date 2023/8/29 09:35
 */
@Slf4j
public class RocketMqConsumerBroadcastServiceImpl extends AbstractRocketMqConsumer implements RocketMqConsumerService {

    private final DefaultMQPushConsumer consumer;

    private final RocketMqConsumerBaseConfig consumerBaseConfig;

    private final RocketMqMsgHandler mqMsgHandler;


    public RocketMqConsumerBroadcastServiceImpl(RocketMqConsumerBaseConfig consumerConfig, RocketMqMsgHandler mqMsgHandler) {
        this.consumerBaseConfig = consumerConfig;
        this.consumer = new DefaultMQPushConsumer(consumerConfig.getConsumerGroup());
        this.consumer.setNamesrvAddr(consumerConfig.getNamesrvAddr());
        this.consumer.setConsumeThreadMax(consumerConfig.getConsumeThreadMax());
        this.consumer.setConsumeThreadMin(consumerConfig.getConsumeThreadMin());
        this.consumer.setPullBatchSize(consumerConfig.getPullBatchSize());
        this.consumer.setConsumeMessageBatchMaxSize(consumerConfig.getConsumeMessageBatchMaxSize());
        this.consumer.setConsumeTimeout(consumerConfig.getConsumeTimeout());
        this.consumer.setMaxReconsumeTimes(consumerConfig.getMaxReconsumeTimes());
        this.mqMsgHandler = mqMsgHandler;
        startConsumer();
    }

    @Override
    public void startConsumer() {
        try {
            super.subScribe(consumerBaseConfig, this.consumer);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.setMessageModel(MessageModel.BROADCASTING);
            // 注册回调实现类来处理从broker拉取回来的消息
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt messageExt : msgs) {
                        try {
                            if (mqMsgHandler.beforeMsgHandler(messageExt)) {
                                if (!mqMsgHandler.handle(messageExt)) {
                                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                                }
                                mqMsgHandler.afterMsgHandler(messageExt);
                            }
                        } catch (Exception e) {
                            if (mqMsgHandler.exceptionMsgHandler(messageExt, e)) {
                                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                            }
                        } finally {
                            mqMsgHandler.finallyMsgHandler(messageExt);
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            // 启动消费者实例
            consumer.start();
        } catch (MQClientException e) {
            log.error("[CustomerGroup:{} ] --> START_ERROR: {}", consumerBaseConfig.getConsumerGroup(), e.getMessage());
        }
        log.info("[CustomerGroup: {} ] --> START_SUCCESS ", consumerBaseConfig.getConsumerGroup());
    }

    @Override
    public void shutDownConsumer() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }
}
