package com.cyberpunk.netty.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lujun
 * @date 2023/8/17 16:27
 */
@Getter
@Setter
@ConfigurationProperties(prefix = NettyServerProperties.PREFIX)
public class NettyServerProperties {
    public static final String PREFIX = "netty.server";

    private int port;

    private String host;

    private boolean useEpoll;

    private boolean enabled;

    private String protocol;
}
