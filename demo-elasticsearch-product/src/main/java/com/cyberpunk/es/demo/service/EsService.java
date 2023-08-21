package com.cyberpunk.es.demo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author lujun
 * @date 2023/8/21 15:15
 */
@Service
public class EsService {

    @Autowired
    ElasticsearchClient elasticsearchClient;


    public void createIndex() {
        CreateIndexRequest.Builder builder = new CreateIndexRequest.Builder().index("product");
        try {
            elasticsearchClient.indices().create(builder.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
