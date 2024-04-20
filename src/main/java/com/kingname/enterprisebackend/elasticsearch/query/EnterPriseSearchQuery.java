package com.kingname.enterprisebackend.elasticsearch.query;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.kingname.enterprisebackend.elasticsearch.query.impl.*;
import com.kingname.enterprisebackend.vo.SearchQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kingname.enterprisebackend.utils.CommonUtils.refineSeed;

@Slf4j
public class EnterPriseSearchQuery implements EsQueryBuilder {

    public static Query getQuery(SearchQuery.Request request) {
        return new FunctionScoreQuery.Builder()
                .query(getConditionQuery(request))
                .boost(QueryConstant.CONST_FLOAT_1)
                //.functions(getFunctions())
                .maxBoost(QueryConstant.CONST_DOUBLE_100_000)
                .boostMode(FunctionBoostMode.Replace)
                .scoreMode(FunctionScoreMode.Sum)
                .minScore(QueryConstant.CONST_DOUBLE_0)
                .build()
                ._toQuery();
    }

    private static Query getConditionQuery(SearchQuery.Request request) {
        return new BoolQuery.Builder()
                .filter(getFilters(request))
                .must(getMustQueries(request))
                .build()
                ._toQuery();
    }

    private static List<Query> getMustQueries(SearchQuery.Request request) {
        return Stream.of(
                        CompanyNameQueryBuilder.getQuery(refineSeed(request.getCompany()))
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static List<Query> getFilters(SearchQuery.Request request) {
        return Stream.of(
                        MemberFilterQueryBuilder.getQuery(request.getMaxMemberCount(), request.getMinMemberCount()),
                        ScaleQueryBuilder.getQuery(request),
                        DateTermQueryBuilder.getQuery(request),
                        CsnQueryBuilder.getQuery(request)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static FunctionScore getFunctions() {
        return null;
    }
}
