package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;

public class DateTermQueryBuilder implements EsQueryBuilder {

    public static Query getQuery(String date) {
        if (!date.isEmpty())
            return EsQueryBuilder.inQuery("date.keyword", date);
        return null;
    }
}
