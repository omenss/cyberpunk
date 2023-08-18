package com.cyberpunk.netty.configuration;

import com.cyberpunk.netty.properties.NettyServerProperties;
import com.cyberpunk.netty.server.NettyServer;
import com.cyberpunk.netty.server.TcpServer;
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
@ConditionalOnProperty(value = "netty.server.enabled", havingValue = "true")
public class NettyServerConfiguration {


    @Bean(name = "tcpServer")
    @ConditionalOnProperty(value = "netty.server.protocol", havingValue = "tcp", matchIfMissing = true)
    public NettyServer tcpServer(NettyServerProperties nettyServerProperties) {
        return new TcpServer(null, nettyServerProperties);
    }

}
