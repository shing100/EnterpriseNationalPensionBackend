package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;

import java.util.List;

public class ScaleQueryBuilder implements EsQueryBuilder {

    public static Query getQuery(List<String> scale) {
        if (!scale.isEmpty())
            return EsQueryBuilder.termsQuery("scale", scale);
        return null;
    }
}
