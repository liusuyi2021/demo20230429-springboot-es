package com.example.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: client
 * @Description:
 * @Author: 刘苏义
 * @Date: 2023年04月29日21:57
 * @Version: 1.0
 **/
@Configuration
public class ElasticsearchClient {

    // Create the low-level client
    @Bean
    RestHighLevelClient getRestClient() {
        // 低级客户端
        RestClient httpClient = RestClient.builder(
                new HttpHost("localhost", 9200)
        ).build();

        //创建旧版旧版High Level Rest Client
        RestHighLevelClient hlrc = new RestHighLevelClientBuilder(httpClient)
                .setApiCompatibilityMode(true)
                .build();
        return hlrc;
    }

}
