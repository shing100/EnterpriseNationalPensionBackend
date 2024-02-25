package com.kingname.enterprisebackend.elasticsearch.query;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface EsQueryBuilder {
    static Query inQuery(String fieldName, Object... fieldValues) {
        Set<String> values = Arrays.stream(fieldValues)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .flatMap(value -> Arrays.stream(value.split(",")))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        return inQuery(fieldName, values);
    }

    static Query inQuery(String fieldName, Collection<String> fieldValueList) {
        if(ObjectUtils.isEmpty(fieldValueList)) return null;
        return new TermsQuery.Builder()
                .field(fieldName)
                .terms(new TermsQueryField.Builder().value(toFieldValue(fieldValueList)).build())
                .build()
                ._toQuery();
    }

    static Query termsQuery(String fieldName, Collection<String> fieldValueList) {
        return termsQuery(fieldName, fieldValueList, Operator.Or);
    }

    static Query termsQuery(String fieldName, Collection<String> fieldValueList, Operator operator) {
        if(Operator.And.equals(operator)) {
            return EsQueryBuilder.mustQuery(
                    fieldValueList.stream()
                            .map(value -> EsQueryBuilder.inQuery(fieldName, value))
                            .toArray(Query[]::new)
            );
        }else {
            return inQuery(fieldName, fieldValueList);
        }
    }

    static Query greaterThenEqualQuery(String fieldName, String minValue) {
        Long minLongValue = StringUtils.isNotBlank(minValue) ? Long.parseLong(minValue) : null;
        if(Objects.isNull(minLongValue)) return null;
        return new RangeQuery.Builder()
                .field(fieldName)
                .gte(JsonData.of(minLongValue))
                .build()
                ._toQuery();
    }

    static Query greaterThenQuery(String fieldName, String minValue) {
        Long minLongValue = StringUtils.isNotBlank(minValue) ? Long.parseLong(minValue) : null;
        if(Objects.isNull(minLongValue)) return null;
        return new RangeQuery.Builder()
                .field(fieldName)
                .gt(JsonData.of(minLongValue))
                .build()
                ._toQuery();
    }

    static Query lessThanEqualQuery(String fieldName, String maxValue) {
        Long maxLongValue = StringUtils.isNotBlank(maxValue) ? Long.parseLong(maxValue) : null;
        if(Objects.isNull(maxLongValue)) return null;
        return new RangeQuery.Builder()
                .field(fieldName)
                .lte(JsonData.of(maxLongValue))
                .build()
                ._toQuery();
    }

    static Query greaterThanAndSmallerThanQuery(String fieldName, String minValue, String maxValue) {
        Long minLongValue = StringUtils.isNotBlank(minValue) ? Long.parseLong(minValue) : null;
        Long maxLongValue = StringUtils.isNotBlank(maxValue) ? Long.parseLong(maxValue) : null;
        return greaterThanAndSmallerThanQuery(fieldName, minLongValue, maxLongValue);
    }

    static Query greaterThanAndSmallerThanQuery(String fieldName, Long minValue, Long maxValue) {
        if(Objects.isNull(minValue) && Objects.isNull(maxValue)) return null;
        return new RangeQuery.Builder()
                .field(fieldName)
                .gt(Objects.nonNull(minValue) ? JsonData.of(minValue) : null)
                .lt(Objects.nonNull(maxValue) ? JsonData.of(maxValue) : null)
                .build()
                ._toQuery();
    }

    static Query betweenQuery(String fieldName, String minValue, String maxValue) {
        Long minLongValue = StringUtils.isNotBlank(minValue) ? Long.parseLong(minValue) : null;
        Long maxLongValue = StringUtils.isNotBlank(maxValue) ? Long.parseLong(maxValue) : null;
        return betweenQuery(fieldName, minLongValue, maxLongValue);
    }

    static Query betweenQuery(String fieldName, Long minValue, Long maxValue) {
        if(Objects.isNull(minValue) && Objects.isNull(maxValue)) return null;
        return new RangeQuery.Builder()
                .field(fieldName)
                .gte(Objects.nonNull(minValue) ? JsonData.of(minValue) : null)
                .lte(Objects.nonNull(maxValue) ? JsonData.of(maxValue) : null)
                .build()
                ._toQuery();
    }

    static List<FieldValue> toFieldValue(Collection<String> values) {
        return values.stream()
                .map(value -> new FieldValue.Builder().stringValue(value).build())
                .collect(Collectors.toList());
    }

    static Query shouldQuery(Query... queries) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        builder.minimumShouldMatch(QueryConstant.CONST_STRING_1);
        return EsQueryBuilder.getQuery(builder, builder::should, queries);
    }

    static Query shouldQuery(String minimumShouldMatch, Query... queries) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        builder.minimumShouldMatch(minimumShouldMatch);
        return EsQueryBuilder.getQuery(builder, builder::should, queries);
    }

    static Query shouldQuery(List<Query> queries) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        builder.minimumShouldMatch(QueryConstant.CONST_STRING_1);
        return EsQueryBuilder.getQuery(builder, builder::should, queries);
    }

    static Query mustQuery(Query... queries) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        return EsQueryBuilder.getQuery(builder, builder::must, queries);
    }

    static Query mustQuery(List<Query> queries) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        return EsQueryBuilder.getQuery(builder, builder::must, queries);
    }

    static Query mustNotQuery(Query... queries) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        return EsQueryBuilder.getQuery(builder, builder::mustNot, queries);
    }

    static Query mustNotQuery(List<Query> queries) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        return EsQueryBuilder.getQuery(builder, builder::mustNot, queries);
    }

    static Query filterQuery(Query... queries) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        return EsQueryBuilder.getQuery(builder, builder::filter, queries);
    }

    private static Query getQuery(BoolQuery.Builder builder, Consumer<Query> action, Query... queries) {
        return getQuery(builder, action, Arrays.stream(queries));
    }

    private static Query getQuery(BoolQuery.Builder builder, Consumer<Query> action, List<Query> queries) {
        return getQuery(builder, action, queries.stream());
    }

    private static Query getQuery(BoolQuery.Builder builder, Consumer<Query> action, Stream<Query> queryStream) {
        List<Query> queryList = queryStream
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if(ObjectUtils.isEmpty(queryList)) {
            return null;
        } else {
            queryList.forEach(action);
            return builder.build()._toQuery();
        }
    }

    static FunctionScore valueFunctionScore(String field, Double factor) {
        return new FieldValueFactorScoreFunction.Builder()
                .field(field)
                .factor(factor)
                .modifier(FieldValueFactorModifier.None)
                .missing(QueryConstant.CONST_DOUBLE_1)
                .build()
                ._toFunctionScore();
    }

    static FunctionScore functionScore(Double weight, Query query) {
        return new FunctionScore.Builder()
                .filter(query)
                .weight(weight)
                .build();
    }

    static FunctionScore termsFunctionScore(String field, Double weight, String... values) {
        return new FunctionScore.Builder()
                .filter(EsQueryBuilder.inQuery(field, (Object[]) values))
                .weight(weight)
                .build();
    }

    static FunctionScore scriptFunctionScore(String source, String lang, Map<String, JsonData> params, Double weight) {
        return new FunctionScore.Builder()
                .filter(new ScriptQuery.Builder()
                        .script(new Script.Builder()
                                .inline(new InlineScript.Builder()
                                        .lang(lang)
                                        .source(source)
                                        .params(params)
                                        .build())
                                .build())
                        .build()
                        ._toQuery())
                .weight(weight)
                .build();
    }

    static FunctionScore scriptFunctionScore(String source, String lang, String key, String value, Double weight) {
        return new FunctionScore.Builder()
                .filter(new ScriptQuery.Builder()
                        .script(new Script.Builder()
                                .inline(new InlineScript.Builder()
                                        .lang(lang)
                                        .source(source)
                                        .params(key, JsonData.of(value))
                                        .build())
                                .build())
                        .build()
                        ._toQuery())
                .weight(weight)
                .build();
    }

    static FunctionScore scriptScore(String source, String lang, Map<String, JsonData> params, Double embeddingVectorWeight) {
        return new FunctionScore.Builder()
                .scriptScore(new ScriptScoreFunction.Builder()
                        .script(new Script.Builder()
                                .inline(new InlineScript.Builder()
                                        .lang(lang)
                                        .source(source)
                                        .params(params)
                                        .build())
                                .build())
                        .build())
                .weight(embeddingVectorWeight)
                .build();
    }
}
