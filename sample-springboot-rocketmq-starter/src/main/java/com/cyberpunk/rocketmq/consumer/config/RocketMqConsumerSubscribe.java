package com.cyberpunk.rocketmq.consumer.config;

/**
 * mq 消费订阅
 * @author lujun
 * @date 2023/08/28
 */
public class RocketMqConsumerSubscribe {

    private String topic;

    private String tag;

    private String sql;

    private String subExpression;

    public RocketMqConsumerSubscribe(String topic, String tag) {
        this.tag = tag;
        this.topic = topic;
    }

    public RocketMqConsumerSubscribe(String topic, String tag, String sql,String subExpression) {
        this.tag = tag;
        this.topic = topic;
        this.sql = sql;
        this.subExpression=subExpression;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSubExpression() {
        return subExpression;
    }

    public void setSubExpression(String subExpression) {
        this.subExpression = subExpression;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((topic == null) ? 0 : topic.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RocketMqConsumerSubscribe other = (RocketMqConsumerSubscribe) obj;
        if (topic == null) {
            if (other.topic != null) {
                return false;
            }
        } else if (!topic.equals(other.topic)) {
            return false;
        }
        return true;
    }
}
