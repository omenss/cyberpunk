package com.cyberpunk.rabbitmq.send;

import com.cyberpunk.rabbitmq.conf.TopicRabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lujun
 * @date 2023/9/5 11:45
 */
@Service
public class TopicSender {


    @Autowired
    private AmqpTemplate rabbitTemplate;

    //两个消息接受者都可以收到
    public void sendOne() {
        String context = "Hi, I am message one";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend(TopicRabbitConfig.TOPIC_EXCHANGE, "topic.one", context);
    }


    //只有TopicReceiverTwo都可以收到
    public void sendTwo() {
        String context = "Hi, I am message two";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend(TopicRabbitConfig.TOPIC_EXCHANGE, "topic.two", context);
    }
}
