package com.kingname.enterprisebackend.elasticsearch.query;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnterPriseSearchQuery implements EsQueryBuilder {

    public static Query getQuery() {
        return new FunctionScoreQuery.Builder()
                .query(getConditionQuery())
                .boost(QueryConstant.CONST_FLOAT_1)
                .functions(getFunctions())
                .maxBoost(QueryConstant.CONST_DOUBLE_100_000)
                .boostMode(FunctionBoostMode.Replace)
                .scoreMode(FunctionScoreMode.Sum)
                .minScore(QueryConstant.CONST_DOUBLE_0)
                .build()
                ._toQuery();
    }

    private static Query getConditionQuery() {
        return null;
    }

    private static FunctionScore getFunctions() {
        return null;
    }

}
