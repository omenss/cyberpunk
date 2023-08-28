package com.cyberpunk.rocketmq.service;

import com.cyberpunk.rocketmq.annotation.RocketResource;
import com.cyberpunk.rocketmq.producer.RocketMqProducer;
import com.cyberpunk.rocketmq.producer.RocketMqTransactionProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lujun
 * @date 2023/8/28 16:15
 */

@Service
@Slf4j
public class DemoProducer {


    @RocketResource(producerGroup = "TEST_GROUP_2")
    private RocketMqTransactionProducer rocketMqProducer2;


    public void sendMq(String msg, String topic, String tag) {
        rocketMqProducer2.transactionProducerSend(topic, tag, msg, 100);
    }

}
