package com.cyberpunk.netty.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lujun
 * @date 2023/8/18 13:40
 */
@Configuration
@ConditionalOnMissingBean(ServerEndpointRegistry.class)
public class NettyWebSocketSelector {


    @Bean
    @ConditionalOnMissingBean(ServerEndpointRegistry.class)
    public ServerEndpointRegistry serverEndpoint() {
        return new ServerEndpointRegistry();
    }
}
