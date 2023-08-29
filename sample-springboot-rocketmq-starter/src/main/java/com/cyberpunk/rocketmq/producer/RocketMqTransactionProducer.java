package com.cyberpunk.rocketmq.producer;

import com.cyberpunk.rocketmq.producer.service.impl.RocketMqProduceTransactionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;


/**
 * @author lujun
 */
@Slf4j
public class RocketMqTransactionProducer extends RocketMqProducer {

    private final String producerGroup;

    private final TransactionMQProducer producer;

    public RocketMqTransactionProducer(RocketMqProduceTransactionServiceImpl defaultService) {
        super(defaultService);
        this.producer = defaultService.getMqProducer();
        this.producerGroup = defaultService.getProducerGroup();
    }

    public void transactionProducerSend(String topic, String tag, String msg, Object arg) {
        try {
            Message message = new Message(topic, tag, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            TransactionSendResult transactionSendResult = producer.sendMessageInTransaction(message, arg);
            log.info("[ProducerGroup:{}] TOPIC: {}--> TRANSACTION_MSG:{}---TransactionSendResult{}", producerGroup, topic, msg,transactionSendResult);
        } catch (UnsupportedEncodingException | MQClientException e) {
            log.error("[ProducerGroup:{}] TOPIC: {}--> TRANSACTION_SEND_ERROR({})", producerGroup, topic, e.getMessage(),e);
        }
    }
}
