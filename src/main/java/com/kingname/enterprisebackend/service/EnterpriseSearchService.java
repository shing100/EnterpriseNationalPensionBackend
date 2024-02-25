package com.kingname.enterprisebackend.service;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.core.search.SourceFilter;
import com.kingname.enterprisebackend.elasticsearch.ElasticsearchRepository;
import com.kingname.enterprisebackend.elasticsearch.query.EnterPriseSearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseSearchService {

    private final ElasticsearchRepository repository;

    @Value("${elasticsearch.alias}")
    private String alias;

    public List<Map> getAIPoolSearchList(Request param) throws IOException {
        SearchResponse<Map> response = repository.search(getSearchRequest(param));
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    private SearchRequest getSearchRequest(Request param) {
        return SearchRequest.of(req ->
                req.query(EnterPriseSearchQuery.getQuery())
                        .index(alias)
        );
    }
}
