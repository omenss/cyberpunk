package com.cyberpunk.rocketmq.service;

import com.alibaba.fastjson.JSON;
import com.cyberpunk.rocketmq.annotation.RocketTransactionHandler;
import com.cyberpunk.rocketmq.producer.RocketMqTransactionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;

/**
 * @author lujun
 * @date 2023/8/28 17:03
 */
@RocketTransactionHandler(producerGroup="TEST_GROUP_2")
@Slf4j
public class TransactionProducer implements RocketMqTransactionHandler {

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("执行本地事务，消息：{}，参数：{}", JSON.toJSON(new String(msg.getBody(), StandardCharsets.UTF_8)), arg);
        Integer index =(Integer) arg;
        if(index>100){
            return LocalTransactionState.COMMIT_MESSAGE;
        }else{
            return LocalTransactionState.UNKNOW;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Object json = JSON.toJSON(new String(msg.getBody(), StandardCharsets.UTF_8));
        log.info("消息回查，消息：{}，返回：COMMIT_MESSAGE：提交消息  ROLLBACK_MESSAGE：回滚消息  UNKNOW：继续回查", json);
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
