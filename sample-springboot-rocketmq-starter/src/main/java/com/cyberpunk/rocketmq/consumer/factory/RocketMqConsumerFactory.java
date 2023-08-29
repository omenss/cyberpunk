package com.cyberpunk.rocketmq.consumer.factory;

import com.cyberpunk.rocketmq.consumer.RocketMqMsgHandler;
import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerBaseConfig;
import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerConfig;
import com.cyberpunk.rocketmq.consumer.service.RocketMqConsumerService;
import com.cyberpunk.rocketmq.consumer.service.impl.RocketMqConsumerBroadcastServiceImpl;
import com.cyberpunk.rocketmq.consumer.service.impl.RocketMqConsumerDefaultServiceImpl;
import com.cyberpunk.rocketmq.consumer.service.impl.RocketMqConsumerOrderlyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lujun
 * @description 消费者工厂
 * 1.1 在初始化的时候 把配置文件内的消费者信息保存在内存中（RocketMqConsumerFactory.CONSUMER_MAP） KEY为消费者组名称，
 * 1.2 setConusmer 手动添加消费者
 * 1.3 setConsumerConfig 手动添加消费者配置
 * 1.4 关闭所有的消费者
 */
@Slf4j
public class RocketMqConsumerFactory {

    public static ConcurrentHashMap<String, RocketMqConsumerService> CONSUMER_MAP = new ConcurrentHashMap<>(4);

    private static final Set<RocketMqConsumerBaseConfig> CONSUMER_CONFIG = new HashSet<>(4);

    private final RocketMqConsumerConfig commonConsumerConfig;


    public RocketMqConsumerFactory(RocketMqConsumerConfig mqConsumerConfig) {
        this.commonConsumerConfig = mqConsumerConfig;
    }


    public void setConsumerConfig(RocketMqConsumerBaseConfig consumerBaseConfig) {
        consumerBaseConfig.setNamesrvAddr(consumerBaseConfig.getNamesrvAddr() == null ? commonConsumerConfig.getNamesrvAddr() : consumerBaseConfig.getNamesrvAddr());
        Assert.notNull(consumerBaseConfig.getNamesrvAddr(), consumerBaseConfig.getConsumerGroup() + " : namesrvAddr is not null ");
        Assert.isTrue(CONSUMER_CONFIG.add(consumerBaseConfig), "There are two identical consumer groups : " + consumerBaseConfig.getConsumerGroup());
    }

    public void setConsumer(RocketMqConsumerBaseConfig consumerBaseConfig, RocketMqMsgHandler mqMsgHandler) {
        switch (consumerBaseConfig.getConsumerType()) {
            case BROADCAST:
                CONSUMER_MAP.put(consumerBaseConfig.getConsumerGroup(), new RocketMqConsumerBroadcastServiceImpl(consumerBaseConfig, mqMsgHandler));
                break;
            case ORDERLY:
                CONSUMER_MAP.put(consumerBaseConfig.getConsumerGroup(), new RocketMqConsumerOrderlyServiceImpl(consumerBaseConfig, mqMsgHandler));
                break;
            default:
                CONSUMER_MAP.put(consumerBaseConfig.getConsumerGroup(), new RocketMqConsumerDefaultServiceImpl(consumerBaseConfig, mqMsgHandler));
                break;
        }
    }

    public RocketMqConsumerConfig getCommonConsumerConfig() {
        return commonConsumerConfig;
    }


    @PreDestroy
    public void shutDown() {
        for (RocketMqConsumerService rocketMqConsumerService : CONSUMER_MAP.values()) {
            rocketMqConsumerService.shutDownConsumer();
        }
    }
}
