package com.demo;

import com.cyberpunk.netty.server.TcpServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import javax.annotation.Resource;

/**
 * @author lujun
 * @date 2023/8/18 09:25
 */
@SpringBootApplication
@EnableAsync
public class NettyServerApplication implements ApplicationRunner {


    @Resource
    TcpServer tcpServer;
    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);
    }

    @Override
    @Async
    public void run(ApplicationArguments args) throws Exception {
        tcpServer.start();
    }
}
