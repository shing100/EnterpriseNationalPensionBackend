package com.kingname.enterprisebackend.elasticsearch.query.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.kingname.enterprisebackend.elasticsearch.query.EsQueryBuilder;

import java.util.List;

public class MemberFilterQueryBuilder implements EsQueryBuilder {

    public static Query getQuery(int maxMemberCount, int minMemberCount) {
        if (maxMemberCount > 0 && minMemberCount > 0)
            return EsQueryBuilder.betweenQuery("totalMemberCount", (long) minMemberCount, (long) maxMemberCount);
        return null;
    }
}
