package com.kingname.enterprisebackend.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticsearchRepository {

    private final ElasticsearchClient client;

    public SearchResponse<Map> search(SearchRequest searchRequest) throws IOException {
        return client.search(searchRequest, Map.class);
    }
}
