package com.kingname.enterprisebackend.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.kingname.enterprisebackend.properties.ElasticsearchProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfig {

    private final ElasticsearchProperties elasticsearchProperties;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        String hosts = String.join(",", elasticsearchProperties.getHosts());
        log.info("ES: {}", hosts);
        return new ElasticsearchClient(getTransport(getHttpHosts(elasticsearchProperties.getHosts())));
    }

    private HttpHost[] getHttpHosts(List<String> hosts) {
        return hosts.stream()
                .map(host -> new HttpHost(host, elasticsearchProperties.getPort()))
                .toArray(HttpHost[]::new);
    }

    private ElasticsearchTransport getTransport(HttpHost[] httpHosts) {
        return new RestClientTransport(getRestClient(httpHosts), new JacksonJsonpMapper());
    }

    private RestClient getRestClient(HttpHost[] httpHosts) {
        return RestClient.builder(httpHosts)
                .setRequestConfigCallback(getRequestConfigCallBack())
                .build();
    }

    private RestClientBuilder.RequestConfigCallback getRequestConfigCallBack() {
        return requestConfigBuilder -> requestConfigBuilder
                .setConnectionRequestTimeout(120000)
                .setSocketTimeout(120000);
    }
}
