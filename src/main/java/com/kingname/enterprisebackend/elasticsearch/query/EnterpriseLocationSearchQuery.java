package com.kingname.enterprisebackend.elasticsearch.query;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.util.ObjectBuilder;
import com.kingname.enterprisebackend.elasticsearch.query.impl.*;
import com.kingname.enterprisebackend.vo.SearchQuery;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnterpriseLocationSearchQuery implements EsQueryBuilder {

    public static Query getQuery(SearchQuery.Request request) {
        return new FunctionScoreQuery.Builder()
                .query(getConditionQuery(request))
                .boost(QueryConstant.CONST_FLOAT_1)
                .scoreMode(FunctionScoreMode.Sum)
                .build()
                ._toQuery();
    }

    private static Query getConditionQuery(SearchQuery.Request request) {
        return new BoolQuery.Builder()
                .filter(getFilters(request))
                .build()
                ._toQuery();
    }

    private static List<Query> getFilters(SearchQuery.Request request) {
        return Stream.of(
                        MemberFilterQueryBuilder.getQuery(request.getMaxMemberCount(), request.getMinMemberCount()),
                        DateTermQueryBuilder.getQuery(request),
                        LocationNameQueryBuilder.getQuery(request)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
