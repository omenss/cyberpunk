package com.cyberpunk.netty.handler;

import com.cyberpunk.netty.store.ChannelStore;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 消息处理,单例启动
 *
 * @author lujun
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("\n");
            log.debug("channelId:{}", ctx.channel().id());
        }
        log.info("收到消息:{}", message);
        this.handleMessage(message);
    }


    protected void handleMessage(String message) {
        AbstractMessageHandler.Builder<Object> builder = new AbstractMessageHandler.Builder<>();
        builder.addHandler(new DefaultMessageHandler())
                .build().doHandler(null);
    }

    /**
     * 指定客户端发送
     *
     * @param clientId 其它已成功登录的客户端
     * @param message  消息
     */
    public void sendByClientId(String clientId, String message) {
        Channel channel = ChannelStore.getChannel(clientId);
        if (null != channel) {
            log.info("发送消息给{}:{}", clientId, message);
            channel.writeAndFlush(message);
        } else {
            log.warn("客户端{}不在线", clientId);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("\n");
        log.info("断开连接");
        ChannelStore.closeAndClean(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("\n");
            log.debug("成功建立连接,channelId：{}", ctx.channel().id());
        }
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("心跳事件时触发");
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        onUnhandledInboundException(cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }


    protected void onUnhandledInboundException(Throwable cause) {
        try {
            log.warn(
                    "An exceptionCaught() event was fired, and it reached at the tail of the pipeline. " +
                            "It usually means the last handler in the pipeline did not handle the exception.",
                    cause);
        } finally {
            ReferenceCountUtil.release(cause);
        }
    }

}
