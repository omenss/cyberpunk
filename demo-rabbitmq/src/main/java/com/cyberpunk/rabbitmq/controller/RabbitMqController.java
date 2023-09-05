package com.cyberpunk.rabbitmq.controller;

import com.cyberpunk.rabbitmq.delay.DelaySender;
import com.cyberpunk.rabbitmq.fanout.FanoutSender;
import com.cyberpunk.rabbitmq.send.Sender;
import com.cyberpunk.rabbitmq.send.TopicSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lujun
 * @date 2023/9/5 11:01
 */
@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitMqController {

    @Autowired
    Sender sender;

    @Autowired
    TopicSender topicSender;

    @Autowired
    FanoutSender fanoutSender;

    @Autowired
    DelaySender delaySender;

    @PostMapping("/send")
    public String send() {
        sender.send();
        return "success";
    }

    @PostMapping("/sendObj")
    public String sendObj() {
        sender.sendObj();
        return "success";
    }

    @PostMapping("/sendTopic")
    public String sendTopic() {
        topicSender.sendOne();
        topicSender.sendTwo();
        return "success";
    }

    @PostMapping("/sendFanout")
    public String fanoutSend(){
        fanoutSender.send();
        return "success";
    }


    @PostMapping("/delaySend")
    public String delaySend(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());
        log.info("发送测试消息的时间：" +currentTime );
        delaySender.send(currentTime + "发送一个测试消息,延迟10秒", 10000);
        delaySender.send(currentTime + "发送一个测试消息，延迟20秒", 20000);
        delaySender.send(currentTime + "发送一个测试消息，延迟30秒", 30000);
        return "success";
    }

}
