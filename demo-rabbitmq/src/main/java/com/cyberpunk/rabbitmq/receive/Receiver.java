package com.cyberpunk.rabbitmq.receive;

import com.cyberpunk.rabbitmq.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author lujun
 * @date 2023/9/5 10:58
 */
@Component
@RabbitListener(queues = "hello")
public class Receiver {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver  : " + hello);
    }

    @RabbitHandler
    public void process(User user) {
        System.out.println("Receiver object : " + user);
    }
}
