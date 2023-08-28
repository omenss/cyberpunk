package com.cyberpunk.rocketmq.service;

import com.cyberpunk.rocketmq.annotation.RocketResource;
import com.cyberpunk.rocketmq.producer.RocketMqProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lujun
 * @date 2023/8/28 16:15
 */

@Service
@Slf4j
public class DemoProducer {

    @RocketResource(producerGroup = "TEST_GROUP_1")
    private RocketMqProducer rocketMqProducer1;

    @RocketResource(producerGroup = "TEST_GROUP_2")
    private RocketMqProducer rocketMqProducer2;


    public void sendMq(String msg, String topic, String tag) {
        boolean result = rocketMqProducer1.syncProducerSend(topic, tag, msg);
        log.info("1.send result:{}", result);
        result = rocketMqProducer2.syncProducerSend(topic, tag, msg);
        log.info("2.send result:{}", result);
    }

}
