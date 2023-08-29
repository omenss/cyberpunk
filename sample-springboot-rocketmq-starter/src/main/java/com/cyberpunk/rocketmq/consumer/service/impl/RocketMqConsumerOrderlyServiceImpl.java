package com.cyberpunk.rocketmq.consumer.service.impl;

import com.cyberpunk.rocketmq.consumer.RocketMqMsgHandler;
import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerBaseConfig;
import com.cyberpunk.rocketmq.consumer.service.AbstractRocketMqConsumer;
import com.cyberpunk.rocketmq.consumer.service.RocketMqConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author lujun
 */
@Slf4j
public class RocketMqConsumerOrderlyServiceImpl extends AbstractRocketMqConsumer implements RocketMqConsumerService {

    private final DefaultMQPushConsumer consumer;

    private final RocketMqConsumerBaseConfig consumerBaseConfig;

    private final RocketMqMsgHandler mqMsgHandler;

    public RocketMqConsumerOrderlyServiceImpl(RocketMqConsumerBaseConfig consumerConfig, RocketMqMsgHandler mqMsgHandler) {
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
            // 注册回调实现类来处理从broker拉取回来的消息
            consumer.registerMessageListener(new MessageListenerOrderly() {
                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    for (MessageExt messageExt : msgs) {
                        try {
                            if (mqMsgHandler.beforeMsgHandler(messageExt)) {
                                if (!mqMsgHandler.handle(messageExt)) {
                                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                                }
                                mqMsgHandler.afterMsgHandler(messageExt);
                            }
                        } catch (Exception e) {
                            if (mqMsgHandler.exceptionMsgHandler(messageExt, e)) {
                                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                            }
                        } finally {
                            mqMsgHandler.finallyMsgHandler(messageExt);
                        }
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
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
