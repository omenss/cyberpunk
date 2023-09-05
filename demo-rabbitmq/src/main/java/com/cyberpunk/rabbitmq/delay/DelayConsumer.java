package com.cyberpunk.rabbitmq.delay;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author lujun
 * @date 2023/9/5 13:54
 */
@Component
@Slf4j
@RabbitListener(queues = "test.delay.queue")
public class DelayConsumer {

    @RabbitHandler
    public void onMessage(byte[] message,
                          @Headers Map<String, Object> headers,
                          Channel channel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info(sdf.format(new Date())+"接收到延时消息:" + new String(message));
    }
}
