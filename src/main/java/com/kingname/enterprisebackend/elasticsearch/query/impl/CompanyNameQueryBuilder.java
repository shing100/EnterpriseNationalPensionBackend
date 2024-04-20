package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyNameQueryBuilder implements EsQueryBuilder {

    public static Query getQuery(String company) {
        if (StringUtils.isEmpty(company)) {
            return null;
        }

        // 기업명을 이용해서 토큰 분석을 하여 검색어를 생성
        List<String> tokens = Arrays.stream(company.split(" "))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        
        // 아직 개선이 필요함
        return EsQueryBuilder.shouldQuery(
                EsQueryBuilder.inQuery("companyName.keyword", tokens),
                EsQueryBuilder.inQuery("originalCompanyName.keyword", tokens),
                EsQueryBuilder.inQuery("synCompanyName", tokens)
        );
    }
}
