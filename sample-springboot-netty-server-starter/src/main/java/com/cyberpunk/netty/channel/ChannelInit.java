package com.cyberpunk.netty.channel;

import com.cyberpunk.netty.handler.MessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Netty 通道初始化
 *
 * @author lujun
 */
@Component
@RequiredArgsConstructor
public class ChannelInit extends ChannelInitializer<SocketChannel> {

    private static final String DELIMITER_SYMBOL = "]";

    private final MessageHandler messageHandler;

    @Override
    protected void initChannel(SocketChannel channel) {
        ByteBuf delimiter = Unpooled.copiedBuffer(DELIMITER_SYMBOL.getBytes());
        channel.pipeline()
                // 心跳时间
                .addLast("idle", new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS))
                .addLast(new DelimiterBasedFrameDecoder(40960,false, delimiter))
                // 添加解码器
                .addLast(new StringDecoder())
                // 添加编码器
                .addLast(new StringEncoder())
                // 添加消息处理器
                .addLast("messageHandler", messageHandler)
        ;
    }

}


