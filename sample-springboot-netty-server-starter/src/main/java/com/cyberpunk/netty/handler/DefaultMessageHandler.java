package com.cyberpunk.netty.handler;

import java.util.Map;

/**
 * @author lujun
 * @date 2023/8/17 16:46
 */
public class DefaultMessageHandler extends AbstractMessageHandler {

    @Override
    public String doHandler(String message) {
        if (next != null) {
            return next.doHandler(message);
        }
        return "";
    }
}
