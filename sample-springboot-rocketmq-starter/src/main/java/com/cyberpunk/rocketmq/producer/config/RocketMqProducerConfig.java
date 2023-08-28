package com.cyberpunk.rocketmq.producer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author lujun
 * @date 2023/08/28
 */
@ConfigurationProperties(prefix = "rocket-mq.producer")
public class RocketMqProducerConfig {
    private boolean enabled = false;
    private String namesrvAddr;
    private List<DefaultMQProducerConfig> defaultMqProducerConfigs;

    public List<DefaultMQProducerConfig> getDefaultMqProducerConfigs() {
        return defaultMqProducerConfigs;
    }

    public void setDefaultMqProducerConfigs(List<DefaultMQProducerConfig> producerConfigs) {
        this.defaultMqProducerConfigs = producerConfigs;
    }

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
