package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;
import com.kingname.enterprisebackend.vo.SearchQuery;

public class IndustryCodeQueryBuilder implements EsQueryBuilder {

    public static Query getQuery(SearchQuery.Request request) {
        if (!request.getIndustry().isEmpty())
            return EsQueryBuilder.inQuery("companyIndustryCode.keyword", request.getIndustry());
        return null;
    }
}
