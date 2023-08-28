package com.cyberpunk.rocketmq.service;

import com.cyberpunk.rocketmq.annotation.RocketTransactionHandler;
import com.cyberpunk.rocketmq.producer.RocketMqTransactionHandler;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author lujun
 * @date 2023/8/28 17:03
 */
@RocketTransactionHandler(producerGroup="TEST_GROUP_2")
public class TransactionProducer implements RocketMqTransactionHandler {

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
