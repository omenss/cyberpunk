package com.cyberpunk.rocketmq.processor;

import com.cyberpunk.rocketmq.annotation.RocketMqConsumerHandler;
import com.cyberpunk.rocketmq.consumer.RocketMqMsgHandler;
import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerBaseConfig;
import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerConfig;
import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerSubscribe;
import com.cyberpunk.rocketmq.consumer.factory.RocketMqConsumerFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lujun
 * @description mq消息 消费者后置处理器
 * <p>
 * postProcessAfterInitialization
 * 查询所有实现了RocketMqMsgHandler的bean 然后把consumer 和handler注入进去
 * 读取配置
 * </p>
 */
@Slf4j
public class RocketMqConsumerBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    private final RocketMqConsumerFactory rocketMqConsumerFactory;

    private final RocketMqConsumerConfig consumerConfig;

    public RocketMqConsumerBeanPostProcessor(RocketMqConsumerFactory rocketMqConsumerFactory) {
        this.rocketMqConsumerFactory = rocketMqConsumerFactory;
        this.consumerConfig = rocketMqConsumerFactory.getCommonConsumerConfig();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, @NonNull String beanName) throws BeansException {
        if (!RocketMqMsgHandler.class.isAssignableFrom(bean.getClass())) {
            return bean;
        }
        RocketMqConsumerHandler annotation = bean.getClass().getAnnotation(RocketMqConsumerHandler.class);
        if (annotation == null) {
            return bean;
        }
        RocketMqConsumerBaseConfig consumerConfig = getConsumerConfig(annotation);
        rocketMqConsumerFactory.setConsumerConfig(consumerConfig);
        rocketMqConsumerFactory.setConsumer(consumerConfig, (RocketMqMsgHandler) bean);
        return bean;
    }

    private RocketMqConsumerBaseConfig getConsumerConfig(RocketMqConsumerHandler annotation) {
        RocketMqConsumerBaseConfig rocketMqConsumerBaseConfig = new RocketMqConsumerBaseConfig();
        rocketMqConsumerBaseConfig.setNamesrvAddr(StringUtils.isEmpty(annotation.namesrvAddr()) ? consumerConfig.getNamesrvAddr() : annotation.namesrvAddr());
        rocketMqConsumerBaseConfig.setConsumerGroup(annotation.consumerGroup());
        rocketMqConsumerBaseConfig.setConsumeThreadMin(annotation.consumeThreadMin());
        rocketMqConsumerBaseConfig.setConsumeThreadMax(annotation.consumeThreadMax());
        rocketMqConsumerBaseConfig.setConsumeMessageBatchMaxSize(annotation.consumeMessageBatchMaxSize());
        rocketMqConsumerBaseConfig.setPullBatchSize(annotation.pullBatchSize());
        rocketMqConsumerBaseConfig.setConsumeTimeout(annotation.consumeTimeout());
        rocketMqConsumerBaseConfig.setMaxReconsumeTimes(annotation.maxReconsumeTimes());
        rocketMqConsumerBaseConfig.setConsumerType(annotation.consumerType());
        Set<RocketMqConsumerSubscribe> rocketMqConsumerSubscribes = new HashSet<>(4);
        for (int i = 0; i < annotation.subscribes().length; i++) {
            Assert.isTrue(rocketMqConsumerSubscribes.add(new RocketMqConsumerSubscribe(annotation.subscribes()[i].topic(),
                            annotation.subscribes()[i].tag(), annotation.subscribes()[i].sql(), annotation.subscribes()[i].subExpression())),
                    "ConsumerGroup: " + rocketMqConsumerBaseConfig.getConsumerGroup() + " can't subscribe to two of the same topics");
        }
        rocketMqConsumerBaseConfig.setSubscribes(rocketMqConsumerSubscribes);
        return rocketMqConsumerBaseConfig;
    }


}
