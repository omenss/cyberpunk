package com.cyberpunk.rabbitmq.conf;

import com.cyberpunk.rabbitmq.delay.ExchangeEnum;
import com.cyberpunk.rabbitmq.delay.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lujun
 * @date 2023/9/5 13:51
 */
@Configuration
public class DelayQueueConfig {


    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(ExchangeEnum.DELAY_EXCHANGE.getValue(), "x-delayed-message", true, false, args);
    }

    /**
     * 延迟消息队列
     * @return
     */
    @Bean
    public Queue delayQueue() {
        return new Queue(QueueEnum.TEST_DELAY.getName(), true);
    }
    @Bean
    public Binding deplyBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(QueueEnum.TEST_DELAY.getRoutingKey()).noargs();
    }
}
