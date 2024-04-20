package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;
import com.kingname.enterprisebackend.vo.SearchQuery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsnQueryBuilder implements EsQueryBuilder {

        public static Query getQuery(SearchQuery.Request request) {
            if (request.getCsn() == null) return null;

            List<String> csn = Arrays.stream(request.getCsn().split(",")).collect(Collectors.toList());
            if (!csn.isEmpty()) {
                return EsQueryBuilder.termsQuery("csn", csn);
            }
            return null;
        }
}
