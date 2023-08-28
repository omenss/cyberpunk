package com.cyberpunk.rocketmq.producer;
import com.cyberpunk.rocketmq.producer.service.impl.RocketMqProduceTransactionServiceImpl;
import com.cyberpunk.rocketmq.producer.service.impl.RocketMqProducerDefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import java.util.List;
/**
 * @author lujun
 * @date 2023/08/28 14:06
 * oneway 单向消息
 * ordered 顺序消息
 * sync 同步消息
 * delay 延迟消息
 * async 异步消息
 */
@Slf4j
public class RocketMqProducer {

    private final String producerGroup;

    private final DefaultMQProducer producer;

    public RocketMqProducer(RocketMqProduceTransactionServiceImpl defaultService) {
        this.producer = defaultService.getMqProducer();
        this.producerGroup = defaultService.getProducerGroup();
    }

    public RocketMqProducer(RocketMqProducerDefaultServiceImpl defaultService) {
        this.producer = defaultService.getMqProducer();
        this.producerGroup = defaultService.getProducerGroup();
    }

    /**
     * 简单同步消息发送
     *
     * @param topic 主题
     * @param msg   消息 消息内容
     * @return boolean 成功 true  失败 false
     * 场景：可靠的同步传输应用于广泛的场景，如重要通知消息、短信通知、短信营销系统等
     **/
    public boolean syncProducerSend(String topic, String msg) {
        return syncProducerSend(topic, topic, msg, 0);
    }

    /**
     * @param topic 主题
     * @param tag   标签
     * @param msg   消息
     * @return boolean 成功 true  失败 false
     * 场景：可靠的同步传输应用于广泛的场景，如重要通知消息、短信通知、短信营销系统等
     * 带标签的同步消息发送
     **/
    public boolean syncProducerSend(String topic, String tag, String msg) {
        return syncProducerSend(topic, tag, msg, 0);
    }

    /**
     * @param topic          主题
     * @param tags           标签
     * @param msg            消息
     * @param delayTimeLevel : "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
     * @return boolean 成功 true  失败 false
     * 场景：可靠的同步传输应用于广泛的场景，如重要通知消息、短信通知、短信营销系统等
     * 同步消息生产者
     **/

    public boolean syncProducerSend(String topic, String tags, String msg, int delayTimeLevel) {
        try {
            Message message = new Message(topic, tags, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            if (delayTimeLevel != 0) {
                message.setDelayTimeLevel(delayTimeLevel);
            }
            SendResult result = producer.send(message);
            log.info("[ProducerGroup:{}] TOPIC: {}--> msgID({}) :RESULT({})", producerGroup, topic, result.getMsgId(), result.getSendStatus());
            return result.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            log.info("[ProducerGroup:{}] TOPIC: {}-->SYNC_SEND_ERROR({})", producerGroup, topic, e.getMessage());
        }
        return false;
    }

    /**
     * 顺序同步发送消息
     *
     * @param topic    主题
     * @param tags     标签
     * @param msg      消息
     * @param orderKey 顺序索引
     * @return boolean 成功 true  失败 false
     */
    public boolean syncProducerOrderSend(String topic, String tags, String msg, String orderKey) {
        try {
            Message message = new Message(topic, tags, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult result = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> messageQueues, Message message, Object o) {
                    int index = Math.abs(orderKey.hashCode()) % messageQueues.size();
                    return messageQueues.get(index);
                }
            }, orderKey);
            log.info("[ProducerGroup:{}] TOPIC: {}--> msgID({}) :RESULT({})", producerGroup, topic, result.getMsgId(), result.getSendStatus());
            return result.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            log.info("[ProducerGroup:{}] TOPIC: {}-->SYNC_SEND_ERROR({})", producerGroup, topic, e.getMessage());
        }
        return false;
    }


    /**
     * @param topic  主题
     * @param tags   标签
     * @param msg    消息
     * @param timeMs 延迟时间 1000L 表示 延迟1s后发送 只精确到秒级 必须大于当前时间 例如：当前时间为2021-11-03 15:20:00  延迟时间为 10s  则消息发送时间为 2021-11-03 15:20:10
     *               <p>
     *               延迟消息发送
     *               </p>
     * @return boolean 成功 true  失败 false
     * 场景：可靠的同步传输应用于广泛的场景，如重要通知消息、短信通知、短信营销系统等
     * 同步消息生产者
     **/

    public boolean syncProducerDelaySend(String topic, String tags, String msg, long timeMs) {
        try {
            Message message = new Message(topic, tags, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            message.setDeliverTimeMs(System.currentTimeMillis() + timeMs);
            SendResult result = producer.send(message);
            log.info("[ProducerGroup:{}] TOPIC: {}--> msgID({}) :RESULT({})", producerGroup, topic, result.getMsgId(), result.getSendStatus());
            return result.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            log.info("[ProducerGroup:{}] TOPIC: {}-->SYNC_SEND_ERROR({})", producerGroup, topic, e.getMessage());
        }
        return false;
    }


    /**
     * @param topic 主题
     * @param msg   消息
     *              场景：异步传输一般用于响应时间敏感的业务场景
     *              简单异步消息发送
     **/
    public void asyncProducerSend(String topic, String msg) {
        asyncProducerSend(topic, topic, msg);
    }

    /**
     * @param topic 主题
     * @param tag   标签
     * @param msg   消息
     * @return void 场景：异步传输一般用于响应时间敏感的业务场景
     * 异步消息生产者
     **/
    public void asyncProducerSend(String topic, String tag, String msg) {
        try {
            Message message = new Message(topic, tag, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("[ProducerGroup:{}] TOPIC: {}--> msgID({}) :RESULT({})", producerGroup, topic, sendResult.getMsgId(), sendResult.getSendStatus());
                }

                @Override
                public void onException(Throwable throwable) {
                    log.info("[ProducerGroup:{}] TOPIC: {}-->ASYNC_BACK_ERROR({})", producerGroup, topic, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("[ProducerGroup:" + producerGroup + "] TOPIC:" + topic + "--> ASYNC_SEND_ERROR: " + e.getMessage());
        }
    }


    /**
     * 支持自定义callBack的异步消息发送
     *
     * @param topic        主题
     * @param tag          标签
     * @param msg          消息
     * @param sendCallback 回调函数
     */
    public void asyncProducerSendWithCallBack(String topic, String tag, String msg, SendCallback sendCallback) {
        try {
            Message message = new Message(topic, tag, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, sendCallback);
        } catch (Exception e) {
            log.error("[ProducerGroup:" + producerGroup + "] TOPIC:" + topic + "--> ASYNC_SEND_ERROR: " + e.getMessage());
        }
    }

    /**
     * 异步发送顺序消息
     *
     * @param topic 主题
     * @param msg   消息
     * @param tag   标签
     **/
    public void asyncProducerOrderSend(String topic, String tag, String msg, String orderKey) {
        try {
            Message message = new Message(topic, tag, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> messageQueues, Message message, Object o) {
                    int index = Math.abs(orderKey.hashCode()) % messageQueues.size();
                    return messageQueues.get(index);
                }
            }, orderKey, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("[ProducerGroup:{}] TOPIC: {}--> msgID({}) :RESULT({})", producerGroup, topic, sendResult.getMsgId(), sendResult.getSendStatus());
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("[ProducerGroup:{}] TOPIC: {}-->ASYNC_BACK_ERROR({})", producerGroup, topic, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("[ProducerGroup:" + producerGroup + "] TOPIC:" + topic + "--> ASYNC_SEND_ERROR: " + e.getMessage());
        }
    }

    /**
     * @return void
     * 这种方式主要用在不特别关心发送结果的场景，例如日志发送。
     * @Author czl
     * @Description 简单单向信息发送
     * @Date 2021/11/3 15:30
     * @Param [topic, msg] 主题、消息
     **/
    public void oneWayProducerSend(String topic, String msg) {
        oneWayProducerSend(topic, topic, msg);
    }

    /**
     * @param topic 主题
     * @param tag   标签
     * @param msg   消息
     *              这种方式主要用在不特别关心发送结果的场景，例如日志发送。
     *              单向信息生产者
     **/
    public void oneWayProducerSend(String topic, String tag, String msg) {
        try {
            Message message = new Message(topic, tag, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(message);
            log.info("[ProducerGroup:{}] TOPIC: {}--> ONEWAY_MSG:{}", producerGroup, topic, msg);
        } catch (Exception e) {
            log.info("[ProducerGroup:{}] TOPIC: {}--> ONEWAY_SEND_ERROR({})", producerGroup, topic, e.getMessage());
        }
    }

}
