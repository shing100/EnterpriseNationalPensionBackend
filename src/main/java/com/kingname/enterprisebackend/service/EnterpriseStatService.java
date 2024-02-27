package com.kingname.enterprisebackend.service;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.kingname.enterprisebackend.elasticsearch.ElasticsearchRepository;
import com.kingname.enterprisebackend.elasticsearch.query.EnterpriseLocationSearchQuery;
import com.kingname.enterprisebackend.properties.ElasticsearchIndexProperties;
import com.kingname.enterprisebackend.vo.AverageSalaryInfo;
import com.kingname.enterprisebackend.vo.CompanyDetail;
import com.kingname.enterprisebackend.vo.LocationStatistic;
import com.kingname.enterprisebackend.vo.SearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseStatService {

    private final EnterpriseSearchService enterpriseSearchService;

    private final ElasticsearchIndexProperties elasticsearchIndexProperties;
    private final ElasticsearchRepository repository;
    private final ModelMapper modelMapper;

    public List<?> getEnterpriseLocationList(SearchQuery.Request request) throws IOException {
        List<LocationStatistic> nationalPensionLocationSearchList = getNationalPensionSearchList(
                elasticsearchIndexProperties.getNationalPensionLocationCollectIndex(),
                request,
                LocationStatistic.class
        );
        log.info("search list size : {}", nationalPensionLocationSearchList.size());
        Collections.shuffle(nationalPensionLocationSearchList); // 랜덤
        return nationalPensionLocationSearchList.subList(0, 4); // 최대 4개 호출
    }

    // 오늘의 인사이트 (기업 총 수, 전체 근로자 수, 취업자수, 실업자수)
    public Map<String, Object> getTodayEnterpriseInsight() throws IOException {
        String lastDate = getNationalPensionLastDate(elasticsearchIndexProperties.getNationalPensionLocationCollectIndex());
        log.info("last Date : {}", lastDate);
        SearchQuery.Request request = SearchQuery.Request.builder()
                .date(lastDate)
                .sort("date.keyword")
                .page(0)
                .size(10000)
                .build();
        List<LocationStatistic> enterpriseLocationList = getNationalPensionSearchList(
                elasticsearchIndexProperties.getNationalPensionLocationCollectIndex(),
                request,
                LocationStatistic.class
        );
        BigDecimal totalEmployed = BigDecimal.valueOf(enterpriseLocationList.stream()
                .map(LocationStatistic::getTotalMemberCount)
                .reduce(0, Integer::sum));
        BigDecimal totalLostMemberCount = BigDecimal.valueOf(enterpriseLocationList.stream()
                .map(LocationStatistic::getLostMemberCount)
                .reduce(0, Integer::sum));
        BigDecimal totalEmployedMemberCount = BigDecimal.valueOf(enterpriseLocationList.stream()
                .map(LocationStatistic::getNewMemberCount)
                .reduce(0, Integer::sum));
        BigDecimal totalCompanyCount = BigDecimal.valueOf(enterpriseLocationList.stream()
                .map(LocationStatistic::getLocationCompanyCount)
                .reduce(0L, Long::sum));

        return Map.of(
                "totalEmployed", totalEmployed,
                "totalLostMemberCount", totalLostMemberCount,
                "totalEmployedMemberCount", totalEmployedMemberCount,
                "totalCompanyCount", totalCompanyCount
        );
    }

    // 평균연봉정보 (이번달 평균, 중위 연봉, 최신년도 평균 연봉, 중위 연봉)
    public AverageSalaryInfo getAverageSalaryInfo() throws IOException {
        String lastDate = getNationalPensionLastDate(elasticsearchIndexProperties.getNationalPensionLocationCollectIndex());
        log.info("last Date : {}", lastDate);
        SearchQuery.Request request = SearchQuery.Request.builder()
                .date(lastDate)
                .sort("date.keyword")
                .page(0)
                .size(10000)
                .build();
        List<LocationStatistic> enterpriseLocationList = getNationalPensionSearchList(
                elasticsearchIndexProperties.getNationalPensionLocationCollectIndex(),
                request,
                LocationStatistic.class
        );
        // 이전년도 대비 평균, 중위 연봉 계산
        // 이전달 대비 평균, 중위 연봉 상승률 계산
        return null;
    }

    // 국민연금 납부액 TOP 5 기업
    public List<?> getTop5CompaniesByNationalPension() throws IOException {
        String lastDate = getNationalPensionLastDate(elasticsearchIndexProperties.getNationalPensionLocationCollectIndex());
        log.info("last Date : {}", lastDate);
        SearchQuery.Request request = SearchQuery.Request.builder()
                .date(lastDate)
                .sort("currentMonthDueAmount")
                .page(0)
                .size(5)
                .build();
        List<CompanyDetail> enterpriseSearchList = enterpriseSearchService.getEnterpriseSearchList(request);
        return enterpriseSearchList;
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
                        .from(request.getPage() == 1 ? 0 : request.getPage() * request.getSize())
                        .size(request.getSize())
                        .index(indexName)
        );
    }
}
