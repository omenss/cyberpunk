package com.cyberpunk.netty.configuration;

import com.cyberpunk.netty.channel.ChannelInit;
import com.cyberpunk.netty.handler.MessageHandler;
import com.cyberpunk.netty.properties.NettyServerProperties;
import com.cyberpunk.netty.server.TcpServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lujun
 * @date 2023/8/17 15:55
 */
@Configuration
@EnableConfigurationProperties(NettyServerProperties.class)
public class NettyServerConfiguration {


    @Bean
    @ConditionalOnProperty(prefix = "netty.server", name = "protocol", havingValue = "tcp")
    @ConditionalOnBean(ChannelInit.class)
    public TcpServer nettyServer(ChannelInit channelInit, NettyServerProperties nettyServerProperties) {
        return new TcpServer(channelInit, nettyServerProperties);
    }

    @Bean
    @ConditionalOnBean(MessageHandler.class)
    public ChannelInit channelInit(MessageHandler messageHandler) {
        return new ChannelInit(messageHandler);
    }


    @Bean
    @ConditionalOnMissingBean
    public MessageHandler messageHandler() {
        return new MessageHandler();
    }
}
