package com.cyberpunk.netty.server;

import javax.annotation.PreDestroy;

/**
 * @author lujun
 * @date 2023/8/17 16:29
 */
public interface NettyServer {


    /**
     * 主启动程序，初始化参数
     */
    void start();


    /**
     * 优雅的结束服务器
     *
     * @throws InterruptedException 提前中断异常
     */
    @PreDestroy
    void destroy() throws InterruptedException;
}
