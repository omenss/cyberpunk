package com.cyberpunk.multipart.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.cyberpunk.multipart.properties.MinioProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lujun
 */
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class AmazonS3Config {

    @Bean(name = "amazonS3Client")
    public AmazonS3 amazonS3Client(MinioProperties minioProperties) {
        //设置连接时的参数
        ClientConfiguration config = new ClientConfiguration();
        //设置连接方式为HTTP，可选参数为HTTP和HTTPS
        config.setProtocol(Protocol.HTTP);
        //设置网络访问超时时间
        config.setConnectionTimeout(minioProperties.getConnectionTimeOut());
        config.setUseExpectContinue(true);
        AWSCredentials credentials = new BasicAWSCredentials(minioProperties.getAccessKey(), minioProperties.getSecretKey());
        //设置Endpoint
        AwsClientBuilder.EndpointConfiguration endPoint = new AwsClientBuilder.EndpointConfiguration(minioProperties.getEndpoint(), Regions.US_EAST_1.name());
        return AmazonS3ClientBuilder.standard()
                .withClientConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endPoint)
                .withPathStyleAccessEnabled(true).build();
    }

}
