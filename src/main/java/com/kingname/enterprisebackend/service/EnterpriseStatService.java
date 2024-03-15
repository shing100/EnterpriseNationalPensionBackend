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
import com.kingname.enterprisebackend.utils.DateUtils;
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
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.kingname.enterprisebackend.utils.MathUtils.calculateRate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseStatService {

    private final EnterpriseSearchService enterpriseSearchService;

    private final ElasticsearchIndexProperties elasticsearchIndexProperties;
    private final ElasticsearchRepository repository;
    private final ModelMapper modelMapper;

    // 오늘의 인사이트 (기업 총 수, 전체 근로자 수, 취업자수, 실업자수)
    public Map<String, Object> getTodayEnterpriseInsight() throws IOException {
        String lastDate = getNationalPensionLastDate(elasticsearchIndexProperties.getNationalPensionLocationCollectIndex());
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
        request.setDate(DateUtils.oneMonthBefore(lastDate, "yyyyMM"));
        List<LocationStatistic> beforeEnterpriseLocationList = getNationalPensionSearchList(
                elasticsearchIndexProperties.getNationalPensionLocationCollectIndex(),
                request,
                LocationStatistic.class
        );

        // 현재 달
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

        // 이전 달 데이터 계산
        BigDecimal beforeTotalEmployed = BigDecimal.valueOf(beforeEnterpriseLocationList.stream()
                .map(LocationStatistic::getTotalMemberCount)
                .reduce(0, Integer::sum));
        BigDecimal beforeTotalLostMemberCount = BigDecimal.valueOf(beforeEnterpriseLocationList.stream()
                .map(LocationStatistic::getLostMemberCount)
                .reduce(0, Integer::sum));
        BigDecimal beforeTotalEmployedMemberCount = BigDecimal.valueOf(beforeEnterpriseLocationList.stream()
                .map(LocationStatistic::getNewMemberCount)
                .reduce(0, Integer::sum));
        BigDecimal beforeTotalCompanyCount = BigDecimal.valueOf(beforeEnterpriseLocationList.stream()
                .map(LocationStatistic::getLocationCompanyCount)
                .reduce(0L, Long::sum));

        // 증감률 계산
        BigDecimal totalEmployedRate = calculateRate(totalEmployed, beforeTotalEmployed);
        BigDecimal totalLostMemberRate = calculateRate(totalLostMemberCount, beforeTotalLostMemberCount);
        BigDecimal totalEmployedMemberRate = calculateRate(totalEmployedMemberCount, beforeTotalEmployedMemberCount);
        BigDecimal totalCompanyRate = calculateRate(totalCompanyCount, beforeTotalCompanyCount);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("year", lastDate.substring(0, 4));
        resultMap.put("month", lastDate.substring(4, 6));
        resultMap.put("beforeDate", lastDate);
        resultMap.put("totalEmployed", totalEmployed);
        resultMap.put("beforeTotalEmployed", beforeTotalEmployed);
        resultMap.put("totalLostMemberCount", totalLostMemberCount);
        resultMap.put("beforeTotalLostMemberCount", beforeTotalLostMemberCount);
        resultMap.put("totalEmployedMemberCount", totalEmployedMemberCount);
        resultMap.put("beforeTotalEmployedMemberCount", beforeTotalEmployedMemberCount);
        resultMap.put("totalCompanyCount", totalCompanyCount);
        resultMap.put("beforeTotalCompanyCount", beforeTotalCompanyCount);
        resultMap.put("totalEmployedRate", totalEmployedRate);
        resultMap.put("totalLostMemberRate", totalLostMemberRate);
        resultMap.put("totalEmployedMemberRate", totalEmployedMemberRate);
        resultMap.put("totalCompanyRate", totalCompanyRate);
        return resultMap;
    }


    // 평균연봉정보 (이번달 평균, 중위 연봉, 최신년도 평균 연봉, 중위 연봉)
    public Map<String, Object> getAverageSalaryInfo() throws IOException {
        String lastDate = getNationalPensionLastDate(elasticsearchIndexProperties.getNationalPensionLocationCollectIndex());
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
        request.setDate(DateUtils.oneMonthBefore(lastDate, "yyyyMM"));
        List<LocationStatistic> beforeEnterpriseLocationList = getNationalPensionSearchList(
                elasticsearchIndexProperties.getNationalPensionLocationCollectIndex(),
                request,
                LocationStatistic.class
        );

        // 현재달
        BigDecimal averageSalary = BigDecimal.valueOf(enterpriseLocationList.stream()
                .map(LocationStatistic::getLocationAverageSalary)
                .reduce(0L, Long::sum))
                .divide(BigDecimal.valueOf(enterpriseLocationList.size()), 0, RoundingMode.HALF_UP);
        BigDecimal medianSalary = BigDecimal.valueOf(enterpriseLocationList.stream()
                .map(LocationStatistic::getLocationMedianSalary)
                .reduce(0L, Long::sum))
                .divide(BigDecimal.valueOf(enterpriseLocationList.size()), 0, RoundingMode.HALF_UP);
        BigDecimal upperQuartileSalary = BigDecimal.valueOf(enterpriseLocationList.stream()
                .map(LocationStatistic::getLocationUpperQuartileSalary)
                .reduce(0L, Long::sum))
                .divide(BigDecimal.valueOf(enterpriseLocationList.size()), 0, RoundingMode.HALF_UP);
        BigDecimal lowerQuartileSalary = BigDecimal.valueOf(enterpriseLocationList.stream()
                .map(LocationStatistic::getLocationLowerQuartileSalary)
                .reduce(0L, Long::sum))
                .divide(BigDecimal.valueOf(enterpriseLocationList.size()), 0, RoundingMode.HALF_UP);

        // 이전달
        BigDecimal beforeAverageSalary = BigDecimal.valueOf(beforeEnterpriseLocationList.stream()
                .map(LocationStatistic::getLocationAverageSalary)
                .reduce(0L, Long::sum))
                .divide(BigDecimal.valueOf(beforeEnterpriseLocationList.size()), 0, RoundingMode.HALF_UP);
        BigDecimal beforeMedianSalary = BigDecimal.valueOf(beforeEnterpriseLocationList.stream()
                .map(LocationStatistic::getLocationMedianSalary)
                .reduce(0L, Long::sum))
                .divide(BigDecimal.valueOf(beforeEnterpriseLocationList.size()), 0, RoundingMode.HALF_UP);
        BigDecimal beforeUpperQuartileSalary = BigDecimal.valueOf(beforeEnterpriseLocationList.stream()
                .map(LocationStatistic::getLocationUpperQuartileSalary)
                .reduce(0L, Long::sum))
                .divide(BigDecimal.valueOf(beforeEnterpriseLocationList.size()), 0, RoundingMode.HALF_UP);
        BigDecimal beforeLowerQuartileSalary = BigDecimal.valueOf(beforeEnterpriseLocationList.stream()
                .map(LocationStatistic::getLocationLowerQuartileSalary)
                .reduce(0L, Long::sum))
                .divide(BigDecimal.valueOf(beforeEnterpriseLocationList.size()), 0, RoundingMode.HALF_UP);

        BigDecimal averageSalaryGrowthRate = calculateRate(averageSalary, beforeAverageSalary);
        BigDecimal medianSalaryGrowthRate = calculateRate(medianSalary, beforeMedianSalary);
        BigDecimal upperQuartileSalaryGrowthRate = calculateRate(upperQuartileSalary, beforeUpperQuartileSalary);
        BigDecimal lowerQuartileSalaryGrowthRate = calculateRate(lowerQuartileSalary, beforeLowerQuartileSalary);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("year", lastDate.substring(0, 4));
        result.put("month", lastDate.substring(4, 6));
        result.put("beforeDate", lastDate);
        result.put("averageSalary", averageSalary);
        result.put("medianSalary", medianSalary);
        result.put("upperQuartileSalary", upperQuartileSalary);
        result.put("lowerQuartileSalary", lowerQuartileSalary);
        result.put("averageSalaryGrowthRate", averageSalaryGrowthRate);
        result.put("medianSalaryGrowthRate", medianSalaryGrowthRate);
        result.put("upperQuartileSalaryGrowthRate", upperQuartileSalaryGrowthRate);
        result.put("lowerQuartileSalaryGrowthRate", lowerQuartileSalaryGrowthRate);
        result.put("beforeAverageSalary", beforeAverageSalary);
        result.put("beforeMedianSalary", beforeMedianSalary);
        result.put("beforeUpperQuartileSalary", beforeUpperQuartileSalary);
        result.put("beforeLowerQuartileSalary", beforeLowerQuartileSalary);
        return result;
    }

    // 국민연금 납부액 TOP 5 기업
    public List<CompanyDetail> getTopCompaniesByNationalPension(int size) throws IOException {
        int DEFAULT_SIZE = 5;
        if (size < 1) size = DEFAULT_SIZE;
        String lastDate = getNationalPensionLastDate(elasticsearchIndexProperties.getNationalPensionLocationCollectIndex());
        log.debug("last Date : {}", lastDate);

        SearchQuery.Request request = SearchQuery.Request.builder()
                .date(lastDate)
                .sort("currentMonthDueAmount")
                .page(0)
                .size(size)
                .build();
        return enterpriseSearchService.getEnterpriseSearchList(request);
    }

    private <T> List<T> getNationalPensionSearchList(String indexName, SearchQuery.Request request, Class<T> tClass) throws IOException {
        SearchResponse<Map> response = repository.search(getLocationSortByDateSearchRequest(indexName, request));
        return response.hits().hits().stream()
                .map(Hit::source)
                .map(map -> modelMapper.map(map, tClass))
                .collect(Collectors.toList());
    }


    private String getNationalPensionLastDate(String indexName) throws IOException {
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
