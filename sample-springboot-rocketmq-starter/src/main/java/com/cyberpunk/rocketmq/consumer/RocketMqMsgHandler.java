package com.cyberpunk.rocketmq.consumer;

import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author lujun
 */
public interface RocketMqMsgHandler {

    /**
     * 消息处理前
     *
     * @param msg 消息
     * @return 是否继续处理
     */
    boolean beforeMsgHandler(MessageExt msg);

    /**
     * 返回：true：代表消息处理成功会执行下面后置处理  false：代表执行失败会进行消息重试
     *
     * @param msg 消息
     * @return 是否继续处理
     */
    boolean handle(MessageExt msg);

    /**
     * 消息处理
     *
     * @param msg 消息
     */
    void afterMsgHandler(MessageExt msg);

    /**
     * 消息异常处理
     * @param msg 消息
     * @param e 异常
     * @return 是否继续处理
     */
    boolean exceptionMsgHandler(MessageExt msg, Exception e);

    /**
     * 消息最终处理
     * @param msg 消息
     */
    void finallyMsgHandler(MessageExt msg);
}
