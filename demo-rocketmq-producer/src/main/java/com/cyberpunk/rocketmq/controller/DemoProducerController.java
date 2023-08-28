package com.cyberpunk.rocketmq.controller;

import com.cyberpunk.rocketmq.service.DemoProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lujun
 * @date 2023/8/28 16:20
 */
@RestController
@RequestMapping("/producer")
public class DemoProducerController {

    @Autowired
    DemoProducer demoProducer;

    @PostMapping("/send")
    public String send(@RequestParam(value = "msg") String msg,
                       @RequestParam(value = "topic") String topic,
                       @RequestParam(value = "tag") String tag) {
        demoProducer.sendMq(msg, topic, tag);
        return "success";
    }
}
