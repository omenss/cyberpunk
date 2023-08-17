package com.cyberpunk.netty.handler;

import java.util.Map;

/**
 * @author lujun
 * @date 2023/8/17 16:46
 */
public class DefaultMessageHandler extends AbstractMessageHandler {

    @Override
    public String doHandler(Map<String, String> map) {
        if (next != null) {
            return next.doHandler(map);
        }
        return "";
    }
}
