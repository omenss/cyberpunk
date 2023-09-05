package com.cyberpunk.rabbitmq.delay;

import lombok.Getter;

/**
 * @author lujun
 * @date 2023/9/5 13:51
 */
@Getter
public enum ExchangeEnum {
    DELAY_EXCHANGE("test.deply.exchange");
    private String value;
    ExchangeEnum(String value) {
        this.value = value;
    }
}
