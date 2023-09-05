package com.cyberpunk.rabbitmq.send;

import com.cyberpunk.rabbitmq.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author lujun
 * @date 2023/9/5 10:55
 */
@Service
public class Sender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(){
        String context = "hello " + LocalDateTime.now();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }


    public void sendObj(){
        User user = new User();
        user.setName("cyberpunk");
        user.setPass("123456");
        this.rabbitTemplate.convertAndSend("hello", user);
    }
}
