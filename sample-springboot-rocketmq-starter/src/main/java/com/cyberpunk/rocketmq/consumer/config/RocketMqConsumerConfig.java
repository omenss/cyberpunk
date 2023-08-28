package com.cyberpunk.rocketmq.consumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lujun
 * @date 2023/08/28
 */
@ConfigurationProperties(prefix = "rocket-mq.consumer")
public class RocketMqConsumerConfig {
    private boolean enabled=false;
    private String namesrvAddr;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }
}
