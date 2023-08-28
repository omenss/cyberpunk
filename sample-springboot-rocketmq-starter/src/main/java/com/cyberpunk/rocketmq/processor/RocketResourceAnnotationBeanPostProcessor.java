package com.cyberpunk.rocketmq.processor;

import com.cyberpunk.rocketmq.annotation.RocketResource;
import com.cyberpunk.rocketmq.annotation.RocketTransactionHandler;
import com.cyberpunk.rocketmq.producer.RocketMqProducer;
import com.cyberpunk.rocketmq.producer.RocketMqTransactionHandler;
import com.cyberpunk.rocketmq.producer.RocketMqTransactionProducer;
import com.cyberpunk.rocketmq.producer.factory.RocketMqProducerFactory;
import com.cyberpunk.rocketmq.producer.service.impl.RocketMqProduceTransactionServiceImpl;
import com.cyberpunk.rocketmq.producer.service.impl.RocketMqProducerDefaultServiceImpl;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Modifier;


/**
 * @author lujun
 * mq 资源后置处理器
 * <p>
 * 1.注入生产者工厂
 * 2. 工厂将给定的属性值应用于给定的bean之前，对其进行后处理 手动注入 RockerMqProducer或者 RocketMqTransactionProducer
 * 3.对所有实现了RocketMqTransactionHandler的实现类 存储到内存中 RocketMqProducerFactory.ROCKET_TRANSACTION_HANDLER
 */
@Slf4j
public class RocketResourceAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    private final RocketMqProducerFactory rocketMqProducerFactory;

    public RocketResourceAnnotationBeanPostProcessor(RocketMqProducerFactory rocketMqProducerFactory) {
        this.rocketMqProducerFactory = rocketMqProducerFactory;
    }


    @Override
    public PropertyValues postProcessProperties(@NonNull PropertyValues pvs, @NonNull Object bean, @NonNull String beanName) throws BeansException {
        rocketResourceAnnotationHandler(bean.getClass(), bean);
        return pvs;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, @NonNull String beanName) throws BeansException {
        if (!RocketMqTransactionHandler.class.isAssignableFrom(bean.getClass())) {
            return bean;
        }
        RocketTransactionHandler annotation = bean.getClass().getAnnotation(RocketTransactionHandler.class);
        if (annotation == null) {
            return bean;
        }
        RocketMqProducerFactory.ROCKET_TRANSACTION_HANDLER.put(annotation.producerGroup(), (RocketMqTransactionHandler) bean);
        return bean;
    }

    private void rocketResourceAnnotationHandler(Class<?> aClass, Object bean) {
        ReflectionUtils.doWithLocalFields(aClass, (field) -> {
            if (field.isAnnotationPresent(RocketResource.class)) {
                if (Modifier.isStatic(field.getModifiers())) {
                    throw new IllegalStateException("@RocketResource annotation is not supported on static fields");
                }
                RocketResource rocketResource = AnnotationUtils.getAnnotation(field, RocketResource.class);
                Assert.notNull(rocketResource, "rocketResource setting is not null");
                String producerGroup = rocketResource.producerGroup();
                try {
                    if (field.getType().equals(RocketMqProducer.class)) {
                        field.setAccessible(true);
                        RocketMqProducer rocketMqProducer = new RocketMqProducer((RocketMqProducerDefaultServiceImpl) rocketMqProducerFactory.getRocketMqProducerService(producerGroup));
                        field.set(bean, rocketMqProducer);
                    }
                    if (field.getType().equals(RocketMqTransactionProducer.class)) {
                        field.setAccessible(true);
                        RocketMqTransactionProducer rocketMqProducer = new RocketMqTransactionProducer((RocketMqProduceTransactionServiceImpl) rocketMqProducerFactory.getRocketMqProducerService(producerGroup));
                        field.set(bean, rocketMqProducer);
                    }
                } catch (IllegalAccessException e) {
                    log.error("RocketResourceAnnotationBeanPostProcessor.rocketResourceAnnotationHandler IllegalAccessException:{}", e.getMessage());
                }
            }
        });
    }

}
