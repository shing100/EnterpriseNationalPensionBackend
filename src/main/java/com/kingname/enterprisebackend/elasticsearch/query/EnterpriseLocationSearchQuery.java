package com.kingname.enterprisebackend.elasticsearch.query;

import co.elastic.clients.elasticsearch._types.query_dsl.*;

public class EnterpriseLocationSearchQuery implements EsQueryBuilder {

    public static Query getQuery() {
        return new MatchAllQuery.Builder().build()._toQuery();
    }
}
