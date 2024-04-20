package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;
import com.kingname.enterprisebackend.vo.SearchQuery;

public class ZipCodeQueryBuilder implements EsQueryBuilder {

    public static Query getQuery(SearchQuery.Request request) {
        if (request.getZipCode() == null) return null;

        if (!request.getZipCode().isEmpty()) {
            return EsQueryBuilder.inQuery("zipCode", request.getZipCode());
        }
        return null;
    }
}
