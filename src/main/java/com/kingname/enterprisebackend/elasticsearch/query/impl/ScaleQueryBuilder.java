package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;
import com.kingname.enterprisebackend.vo.SearchQuery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScaleQueryBuilder implements EsQueryBuilder {

    public static Query getQuery(SearchQuery.Request request) {
        if (request.getCompanyType() == null) return null;

        List<String> scale = Arrays.stream(request.getCompanyType().split(",")).collect(Collectors.toList());
        if (!scale.isEmpty()) {
            return EsQueryBuilder.termsQuery("scale", scale);
        }
        return null;
    }
}
