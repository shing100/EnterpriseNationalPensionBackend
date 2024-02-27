package com.kingname.enterprisebackend.service;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.kingname.enterprisebackend.elasticsearch.ElasticsearchRepository;
import com.kingname.enterprisebackend.elasticsearch.query.EnterPriseSearchQuery;
import com.kingname.enterprisebackend.elasticsearch.query.EnterpriseLocationSearchQuery;
import com.kingname.enterprisebackend.vo.CompanyDetail;
import com.kingname.enterprisebackend.vo.SearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseSearchService {

    private final ElasticsearchRepository repository;
    private final ModelMapper modelMapper;

    @Value("${elasticsearch.alias}")
    private String alias;

    public List<CompanyDetail> getEnterpriseSearchList(SearchQuery.Request param) throws IOException {
        SearchResponse<Map> response = repository.search(getSearchRequest(param));
        return response.hits().hits().stream()
                .map(Hit::source)
                .map(map -> modelMapper.map(map, CompanyDetail.class))
                .collect(Collectors.toList());
    }

    public <T> List<T> getNationalPensionSearchList(String indexName, SearchQuery.Request request, Class<T> tClass) throws IOException {
        SearchResponse<Map> response = repository.search(getLocationSortByDateSearchRequest(indexName, request));
        return response.hits().hits().stream()
                .map(Hit::source)
                .map(map -> modelMapper.map(map, tClass))
                .collect(Collectors.toList());
    }



    public String getNationalPensionLastDate(String indexName) throws IOException {
        SearchResponse<Map> response = repository.search(getLastDateSearchRequest(indexName));
        return response.hits().hits().get(0).source().get("date").toString();
    }

    private SearchRequest getLastDateSearchRequest(String indexName) {
        return SearchRequest.of(req ->
                req.query(new MatchAllQuery.Builder().build()._toQuery())
                        .sort(new SortOptions.Builder()
                                .field(new FieldSort.Builder()
                                        .field("date.keyword")
                                        .order(SortOrder.Desc)
                                        .build()
                                ).build()
                        )
                        .size(1)
                        .index(indexName)
        );
    }

    private SearchRequest getLocationSortByDateSearchRequest(String indexName, SearchQuery.Request request) {
        return SearchRequest.of(req ->
                req.query(EnterpriseLocationSearchQuery.getQuery(request))
                        .sort(new SortOptions.Builder()
                                .field(new FieldSort.Builder()
                                        .field(request.getSort())
                                        .order(SortOrder.Desc)
                                        .build()
                                ).build()
                        )
                        .index(indexName)
        );
    }

    private SearchRequest getSearchRequest(SearchQuery.Request param) {
        return SearchRequest.of(req ->
                req.query(EnterPriseSearchQuery.getQuery(param))
                        .index(alias)
        );
    }
}
