package com.cyberpunk;


import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lujun
 * @date 2023/8/16 13:46
 */
@SpringBootApplication
@EnableCasClient
public class SampleClientAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleClientAppApplication.class, args);
    }

}
