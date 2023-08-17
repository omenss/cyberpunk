package com.cyberpunk.netty.server;

import com.cyberpunk.netty.channel.ChannelInit;
import com.cyberpunk.netty.properties.NettyServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author lujun
 * @date 2023/8/17 16:30
 */
@Slf4j
public class TcpServer implements NettyServer {

    private final ChannelInit channelInit;

    private final NettyServerProperties serverProperties;

    private final EventLoopGroup bossGroup;

    private final EventLoopGroup workerGroup;

    public TcpServer(ChannelInit channelInit, NettyServerProperties serverProperties) {
        this.channelInit = channelInit;
        this.serverProperties = serverProperties;
        bossGroup = serverProperties.isUseEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        workerGroup = serverProperties.isUseEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }


    @Override
    public void start() {
        log.info("初始化 TCP server ...");
        this.tcpServer();
    }


    /**
     * 初始化
     */
    private void tcpServer() {
        try {
            new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(serverProperties.isUseEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(serverProperties.getPort()))
                    // 配置 编码器、解码器、业务处理
                    .childHandler(channelInit)
                    // tcp缓冲区
                    .option(ChannelOption.SO_BACKLOG, 2048)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    // 将网络数据积累到一定的数量后,服务器端才发送出去,会造成一定的延迟。希望服务是低延迟的,建议将TCP_NODELAY设置为true
                    .childOption(ChannelOption.TCP_NODELAY, false)
                    // 保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 绑定端口，开始接收进来的连接
                    .bind().sync();
            log.info("tcpServer启动成功！开始监听端口：{}", serverProperties.getPort());
        } catch (Exception e) {
            log.error("tcpServer启动失败！端口：{}", serverProperties.getPort());
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void destroy() throws InterruptedException {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
