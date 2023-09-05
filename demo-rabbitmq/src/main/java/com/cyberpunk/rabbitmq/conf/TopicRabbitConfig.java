package com.cyberpunk.rabbitmq.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lujun
 * @date 2023/9/5 11:44
 */
@Configuration
public class TopicRabbitConfig {
    public final static String TOPIC_ONE = "topic.one";
    public final static String TOPIC_TWO = "topic.two";
    public final static String TOPIC_EXCHANGE = "topicExchange";

    @Bean
    public Queue queueOne(){
        return new Queue(TOPIC_ONE);
    }

    @Bean
    public Queue queueTwo(){
        return new Queue(TOPIC_TWO);
    }

    @Bean
    TopicExchange exchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    Binding bindingExchangeOne(Queue queueOne, TopicExchange exchange){
        return BindingBuilder.bind(queueOne).to(exchange).with("topic.one");
    }

    @Bean
    Binding bindingExchangeTwo(Queue queueTwo, TopicExchange exchange){
        //# 表示零个或多个词
        //* 表示一个词
        return BindingBuilder.bind(queueTwo).to(exchange).with("topic.#");
    }
}
