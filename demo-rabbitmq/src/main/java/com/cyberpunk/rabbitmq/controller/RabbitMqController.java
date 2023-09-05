package com.cyberpunk.rabbitmq.controller;

import com.cyberpunk.rabbitmq.fanout.FanoutSender;
import com.cyberpunk.rabbitmq.send.Sender;
import com.cyberpunk.rabbitmq.send.TopicSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lujun
 * @date 2023/9/5 11:01
 */
@RestController
@RequestMapping("/rabbitmq")
public class RabbitMqController {

    @Autowired
    Sender sender;

    @Autowired
    TopicSender topicSender;

    @Autowired
    FanoutSender fanoutSender;

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
}
