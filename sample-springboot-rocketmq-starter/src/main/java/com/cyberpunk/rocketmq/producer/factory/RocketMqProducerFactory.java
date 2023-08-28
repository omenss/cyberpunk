package com.cyberpunk.rocketmq.producer.factory;

import com.cyberpunk.rocketmq.producer.RocketMqTransactionHandler;
import com.cyberpunk.rocketmq.producer.config.RocketMqProducerConfig;
import com.cyberpunk.rocketmq.producer.service.RocketMqProducerService;
import com.cyberpunk.rocketmq.producer.service.impl.RocketMqProduceTransactionServiceImpl;
import com.cyberpunk.rocketmq.producer.service.impl.RocketMqProducerDefaultServiceImpl;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lujun
 * @description 1.1 启动mq生产者
 * 1.2 关闭mq生产者
 * 1.3 在初始化的时候 把配置文件内的生产者信息保存在内存中（RocketMqProducerFactory.ROCKET_PRODUCER） KEY为生产者组名称，
 * value根据配置文件是否支持事务的不同 选取不同的实现类(RocketMqProducerService)
 * 1.4 通过生产者组从内存中获取生产者信息
 */
public class RocketMqProducerFactory {

    private static final Map<String, RocketMqProducerService> ROCKET_PRODUCER = new ConcurrentHashMap<>(4);

    public static final Map<String, RocketMqTransactionHandler> ROCKET_TRANSACTION_HANDLER = new ConcurrentHashMap<>(16);

    public RocketMqProducerFactory(RocketMqProducerConfig rocketMqProducerConfig) {
        Assert.notNull(rocketMqProducerConfig.getDefaultMqProducerConfigs(), "rocketMqProducerConfig is not null");
        rocketMqProducerConfig.getDefaultMqProducerConfigs().forEach(item -> {
            item.setNamesrvAddr(item.getNamesrvAddr() == null ? rocketMqProducerConfig.getNamesrvAddr() : item.getNamesrvAddr());
            ROCKET_PRODUCER.put(item.getProducerGroup(), item.getSupportTransaction() ?
                    new RocketMqProduceTransactionServiceImpl(item) : new RocketMqProducerDefaultServiceImpl(item));
        });
    }

    public RocketMqProducerService getRocketMqProducerService(String producerGroup) {
        RocketMqProducerService rocketMqProducerDefaultService = ROCKET_PRODUCER.get(producerGroup);
        Assert.notNull(rocketMqProducerDefaultService, producerGroup + "is not exist,setting attribute fail");
        return rocketMqProducerDefaultService;
    }

    @PostConstruct
    public void start() {
        for (RocketMqProducerService rocketMqProducerService : ROCKET_PRODUCER.values()) {
            rocketMqProducerService.startProducer();
        }
    }

    @PreDestroy
    public void shutDown() {
        for (RocketMqProducerService rocketMqProducerService : ROCKET_PRODUCER.values()) {
            rocketMqProducerService.shutDownProducer();
        }
    }
}
