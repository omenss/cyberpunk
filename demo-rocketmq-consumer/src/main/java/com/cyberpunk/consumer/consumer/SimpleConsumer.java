package com.cyberpunk.consumer.consumer;

import com.cyberpunk.rocketmq.annotation.RocketMqConsumerHandler;
import com.cyberpunk.rocketmq.annotation.RocketMqSubscribes;
import com.cyberpunk.rocketmq.consumer.RocketMqMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author lujun
 * @date 2023/8/28 16:42
 */
@Slf4j
@RocketMqConsumerHandler(consumerGroup = "TEST_GROUP_1", subscribes = {@RocketMqSubscribes(topic = "TOPIC_ORDER")})
public class SimpleConsumer implements RocketMqMsgHandler {

    @Override
    public boolean beforeMsgHandler(MessageExt msg) {
        log.info("beforeMsgHandler:{}", msg);
        return false;
    }

    @Override
    public boolean handle(MessageExt msg) {
        log.info("handle:{}", msg);
        return true;
    }

    @Override
    public void afterMsgHandler(MessageExt msg) {
        log.info("afterMsgHandler:{}", msg);

    }

    @Override
    public boolean exceptionMsgHandler(MessageExt msg, Exception e) {
        log.info("exceptionMsgHandler:{}", msg);
        return false;
    }

    @Override
    public void finallyMsgHandler(MessageExt msg) {
        log.info("finallyMsgHandler:{}", msg);
    }
}
