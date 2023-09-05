package com.cyberpunk.rabbitmq.receive;

import com.cyberpunk.rabbitmq.conf.TopicRabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author lujun
 * @date 2023/9/5 13:32
 */
@Component
@RabbitListener(queues = TopicRabbitConfig.TOPIC_TWO)
public class TopicReceiverTwo {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("ReceiverTwo  : " + msg);
    }
}
