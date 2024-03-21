package com.kingname.enterprisebackend.service;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.kingname.enterprisebackend.elasticsearch.ElasticsearchRepository;
import com.kingname.enterprisebackend.elasticsearch.query.EnterPriseSearchQuery;
import com.kingname.enterprisebackend.elasticsearch.query.EnterpriseIndustrySearchQuery;
import com.kingname.enterprisebackend.elasticsearch.query.EnterpriseLocationSearchQuery;
import com.kingname.enterprisebackend.properties.ElasticsearchIndexProperties;
import com.kingname.enterprisebackend.vo.CompanyDetail;
import com.kingname.enterprisebackend.vo.IndustryStatistic;
import com.kingname.enterprisebackend.vo.LocationStatistic;
import com.kingname.enterprisebackend.vo.SearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseSearchService {

    private final ElasticsearchRepository repository;
    private final ModelMapper modelMapper;
    private final ElasticsearchIndexProperties elasticsearchIndexProperties;

    public List<CompanyDetail> getEnterpriseSearchList(SearchQuery.Request param) throws IOException {
        List<SortOptions> sortOptions = new ArrayList<>();
        getSortOptions(param, "currentMonthDueAmount", sortOptions);
        getSortOptions(param, "totalMemberCount", sortOptions);

        SearchResponse<Map> response = repository.search(getSearchRequest(param, sortOptions));
        return response.hits().hits().stream()
                .map(Hit::source)
                .map(map -> modelMapper.map(map, CompanyDetail.class))
                .collect(Collectors.toList());
    }


    public List<LocationStatistic> getEnterpriseLocationList(SearchQuery.Request param) throws IOException {
        List<SortOptions> sortOptions = new ArrayList<>();
        getSortOptions(param, "totalMemberCount", sortOptions);

        SearchResponse<Map> response = repository.search(getLocationSearchRequest(param, sortOptions));
        return response.hits().hits().stream()
                .map(Hit::source)
                .map(map -> modelMapper.map(map, LocationStatistic.class))
                .collect(Collectors.toList());
    }

    public List<IndustryStatistic> getEnterpriseIndustryList(SearchQuery.Request param) throws IOException {
        List<SortOptions> sortOptions = new ArrayList<>();
        getSortOptions(param, "totalMemberCount", sortOptions);

        SearchResponse<Map> response = repository.search(getIndustrySearchRequest(param, sortOptions));
        return response.hits().hits().stream()
                .map(Hit::source)
                .map(map -> modelMapper.map(map, IndustryStatistic.class))
                .collect(Collectors.toList());
    }

    private SearchRequest getIndustrySearchRequest(SearchQuery.Request param, List<SortOptions> sortOptions) {
        Query query = EnterpriseIndustrySearchQuery.getQuery(param);
        return SearchRequest.of(req ->
                req.query(query)
                        .from(param.getPage() == 1 ? 0 : param.getPage() * param.getSize())
                        .size(param.getSize())
                        .sort(sortOptions)
                        .index(elasticsearchIndexProperties.getNationalPensionCollectAlias())
        );
    }

    public List<?> getEnterprishuffleList(SearchQuery.Request request, int size) throws IOException {
        List<CompanyDetail> enterpriseSearchList = getEnterpriseSearchList(request);
        Collections.shuffle(enterpriseSearchList); // 랜덤
        return enterpriseSearchList.subList(0, size);
    }

    private SearchRequest getLocationSearchRequest(SearchQuery.Request param, List<SortOptions> sortOptions) {
        Query query = EnterpriseLocationSearchQuery.getQuery(param);
        return SearchRequest.of(req ->
                req.query(query)
                        .from(param.getPage() == 1 ? 0 : param.getPage() * param.getSize())
                        .size(param.getSize())
                        .sort(sortOptions)
                        .index(elasticsearchIndexProperties.getNationalPensionCollectAlias())
        );
    }


    private SearchRequest getSearchRequest(SearchQuery.Request param, List<SortOptions> sortOptions) {
        Query query = EnterPriseSearchQuery.getQuery(param);
        return SearchRequest.of(req ->
                req.query(query)
                        .from(param.getPage() == 1 ? 0 : param.getPage() * param.getSize())
                        .size(param.getSize())
                        .sort(sortOptions)
                        .index(elasticsearchIndexProperties.getNationalPensionCollectAlias())
        );
    }

    private static void getSortOptions(SearchQuery.Request param, String sortType, List<SortOptions> sortOptions) {
        if (param.getSort().equals(sortType)) {
            SortOptions options = new SortOptions.Builder()
                    .field(new FieldSort.Builder().field(param.getSort()).order(SortOrder.Desc).build()).build();
            sortOptions.add(options);
        }
    }
}
