package com.cyberpunk.elasticsearch.autoconfigure;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.cyberpunk.elasticsearch.properties.ElasticSearchClientProperties;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lujun
 * @date 2023/8/21 14:51
 */
@ConditionalOnProperty(prefix = ElasticSearchClientProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = false)
@Configuration
@EnableConfigurationProperties(value = ElasticSearchClientProperties.class)
public class ElasticSearchAutoConfigure {


    @ConditionalOnMissingBean
    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticSearchClientProperties elasticSearchClientProperties) {
        RestClient restClient = RestClient
                .builder(HttpHost.create(elasticSearchClientProperties.getServerUrl()))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + elasticSearchClientProperties.getApiKey())
                })
                .build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @ConditionalOnMissingBean
    @Bean
    public ElasticsearchAsyncClient elasticsearchAsyncClient(ElasticSearchClientProperties elasticSearchClientProperties) {
        RestClient restClient = RestClient
                .builder(HttpHost.create(elasticSearchClientProperties.getServerUrl()))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + elasticSearchClientProperties.getApiKey())
                })
                .build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        return new ElasticsearchAsyncClient(transport);
    }
}
