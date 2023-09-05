package com.cyberpunk.rabbitmq.delay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lujun
 * @date 2023/9/5 13:52
 */
@Slf4j
@Service
public class DelaySender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg, int delayTime) {
        log.info("msg= " + msg + ".delayTime" + delayTime);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDelay(delayTime);
        Message message = new Message(msg.getBytes(), messageProperties);
        rabbitTemplate.send(ExchangeEnum.DELAY_EXCHANGE.getValue(), QueueEnum.TEST_DELAY.getRoutingKey(), message);
    }
}
