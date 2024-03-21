package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;
import org.apache.commons.lang3.StringUtils;

public class CompanyNameQueryBuilder implements EsQueryBuilder {

    public static Query getQuery(String company) {
        if (StringUtils.isEmpty(company)) {
            return null;
        }
        return EsQueryBuilder.shouldQuery(
                EsQueryBuilder.inQuery("companyName", company),
                EsQueryBuilder.inQuery("originalCompanyName", company),
                EsQueryBuilder.inQuery("synCompanyName", company)
        );
    }
}
