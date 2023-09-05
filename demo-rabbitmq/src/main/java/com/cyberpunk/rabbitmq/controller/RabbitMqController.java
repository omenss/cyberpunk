package com.cyberpunk.rabbitmq.controller;

import com.cyberpunk.rabbitmq.send.Sender;
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
}
