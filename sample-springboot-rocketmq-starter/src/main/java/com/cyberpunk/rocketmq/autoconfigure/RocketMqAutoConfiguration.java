package com.cyberpunk.rocketmq.autoconfigure;

import com.cyberpunk.rocketmq.consumer.config.RocketMqConsumerConfig;
import com.cyberpunk.rocketmq.consumer.factory.RocketMqConsumerFactory;
import com.cyberpunk.rocketmq.processor.RocketMqConsumerBeanPostProcessor;
import com.cyberpunk.rocketmq.processor.RocketResourceAnnotationBeanPostProcessor;
import com.cyberpunk.rocketmq.producer.config.RocketMqProducerConfig;
import com.cyberpunk.rocketmq.producer.factory.RocketMqProducerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lujun
 * @description 自动装配
 * <p>
 * 自动装配四个配置类
 * 1.RocketMqProducerFactory mq生产者工厂
 * 主要用于
 * 1.1 启动mq生产者
 * 1.2 关闭mq生产者
 * 1.3 在初始化的时候 把配置文件内的生产者信息保存在内存中（RocketMqProducerFactory.ROCKET_PRODUCER） KEY为生产者组名称，
 * value根据配置文件是否支持事务的不同 选取不同的实现类(RocketMqProducerService)
 * 1.4 通过配置文件从内存中获取生产者信息
 * </p>
 * <p>
 * <p>
 * 2.RocketMqConsumerFactory 消费者工厂
 * 2.1 在初始化的时候 把配置文件内的消费者信息保存在内存中（RocketMqConsumerFactory.CONSUMER_MAP） KEY为消费者组名称，
 * 2.2 setConusmer 手动添加消费者
 * 2.3 setConsumerConfig 手动添加消费者配置
 * 2.4 关闭所有的消费者
 * </p>
 * <p>
 * 3.RocketResourceAnnotationBeanPostProcessor mq资源后置处理器
 * 3.1 注入生产者工厂
 * 3.2  工厂将给定的属性值应用于给定的bean之前，对其进行后处理 手动注入 RockerMqProducer或者 RocketMqTransactionProducer
 * 3.3对所有实现了RocketMqTransactionHandler的实现类 存储到内存中 RocketMqProducerFactory.ROCKET_TRANSACTION_HANDLER
 * </p>
 * <p>
 * 4.RocketMqConsumerBeanPostProcessor.postProcessAfterInitialization
 * 查询所有实现了RocketMqMsgHandler的bean 然后把consumer 和handler注入进去
 * 读取配置
 * </p>
 * @date 2023/08/28
 */
@Configuration
@EnableConfigurationProperties({RocketMqProducerConfig.class, RocketMqConsumerConfig.class})
public class RocketMqAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = {"rocket-mq.producer.enabled"}, havingValue = "true")
    public RocketMqProducerFactory rocketMqProducerFactory(RocketMqProducerConfig rocketMqProducerConfig) {
        return new RocketMqProducerFactory(rocketMqProducerConfig);
    }

    @Bean
    @ConditionalOnProperty(value = {"rocket-mq.consumer.enabled"}, havingValue = "true")
    public RocketMqConsumerFactory rocketMqConsumerFactory(RocketMqConsumerConfig rocketMqConsumerConfig) {
        return new RocketMqConsumerFactory(rocketMqConsumerConfig);
    }

    @Bean
    @ConditionalOnBean(RocketMqProducerFactory.class)
    public RocketResourceAnnotationBeanPostProcessor rocketResourceAnnotationBeanPostProcessor(RocketMqProducerFactory rocketMqProducerFactory) {
        return new RocketResourceAnnotationBeanPostProcessor(rocketMqProducerFactory);
    }


    @Bean
    @ConditionalOnBean(RocketMqConsumerFactory.class)
    public RocketMqConsumerBeanPostProcessor rocketMqConsumerBeanPostProcessor(RocketMqConsumerFactory rocketMqConsumerFactory) {
        return new RocketMqConsumerBeanPostProcessor(rocketMqConsumerFactory);
    }
}
