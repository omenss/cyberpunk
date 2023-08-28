package com.cyberpunk.consumer.consumer;

import com.alibaba.fastjson.JSON;
import com.cyberpunk.rocketmq.annotation.RocketMqConsumerHandler;
import com.cyberpunk.rocketmq.annotation.RocketMqSubscribes;
import com.cyberpunk.rocketmq.consumer.RocketMqMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;

/**
 * @author lujun
 * @date 2023/8/28 16:42
 */
@Slf4j
@RocketMqConsumerHandler(consumerGroup = "TEST_GROUP_2", subscribes = {@RocketMqSubscribes(topic = "TOPIC_ORDER")})
public class SimpleConsumer implements RocketMqMsgHandler {

    @Override
    public boolean beforeMsgHandler(MessageExt msg) {
        log.info("beforeMsgHandler:{}", msg);
        Object json = JSON.toJSON(new String(msg.getBody(), StandardCharsets.UTF_8));
        log.info("信息前置，可以做比如：日志收集、幂等校验等，返回值说明：true：代表通过会执行下面的消息处理 false：代表拦截直接返回消费成功:{}",json);
        return false;
    }

    @Override
    public boolean handle(MessageExt msg) {
        log.info("handle:{}", msg);
        Object json = JSON.toJSON(new String(msg.getBody(), StandardCharsets.UTF_8));
        log.info("消息处理，消息：{} ，返回：true：代表消息处理成功会执行下面后置处理  false：代表执行失败会进行消息重试", json);
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
