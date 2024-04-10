package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;
import com.kingname.enterprisebackend.vo.SearchQuery;

public class LocationFilterQueryBuilder implements EsQueryBuilder {

        public static Query getQuery(SearchQuery.Request request) {
            if (!request.getLocation().isEmpty())
                return EsQueryBuilder.inQuery("location.keyword", request.getLocation());
            return null;
        }
}
