package com.kingname.enterprisebackend.elasticsearch.query;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.kingname.enterprisebackend.vo.SearchQuery;

public class EnterpriseLocationSearchQuery implements EsQueryBuilder {

    public static Query getQuery(SearchQuery.Request request) {
        return new MatchAllQuery.Builder().build()._toQuery();
    }
}