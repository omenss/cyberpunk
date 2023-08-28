package com.cyberpunk.rocketmq.producer.config;

/**
 * @author lujun
 * @date 2023/08/28
 */
public class DefaultMQProducerConfig {
    private String namesrvAddr;
    private String producerGroup;
    private Integer retryTimesWhenSendFailed = 3;
    private Integer sendMsgTimeout = 5000;
    private Boolean isSupportTransaction = false;

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public Integer getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(Integer retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    public Integer getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(Integer sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }


    public Boolean getSupportTransaction() {
        return isSupportTransaction;
    }

    public void setSupportTransaction(Boolean supportTransaction) {
        isSupportTransaction = supportTransaction;
    }
}
