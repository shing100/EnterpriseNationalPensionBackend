package com.kingname.enterprisebackend.elasticsearch.query;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.vo.SearchQuery;

public class EnterpriseIndustrySearchQuery {

    public static Query getQuery(SearchQuery.Request request) {
        return new MatchAllQuery.Builder().build()._toQuery();
    }

}
