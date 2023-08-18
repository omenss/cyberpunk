package com.cyberpunk.netty.handler;

import com.cyberpunk.netty.store.ChannelStore;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 消息处理,单例启动
 *
 * @author lujun
 */
@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<String> {

    private final AbstractMessageHandler.Builder<Object> builder;


    public MessageHandler(AbstractMessageHandler.Builder<Object> builder) {
        this.builder = builder;
    }

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
        builder.build().doHandler(message);
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
