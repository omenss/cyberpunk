package com.cyberpunk.rocketmq.enums;

/**
 * @author lujun
 * @date 2023/8/29 09:55
 */
public enum ConsumerType {


    /**
     * 广播消费
     */
    BROADCAST,


    /**
     * 顺序消费
     */
    ORDERLY,


    /**
     * 默认普通消费
     */
    DEFAULT;
}
