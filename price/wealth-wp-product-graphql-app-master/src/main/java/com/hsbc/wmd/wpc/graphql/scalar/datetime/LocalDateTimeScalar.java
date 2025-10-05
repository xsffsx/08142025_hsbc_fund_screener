package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.PublicApi;
import graphql.schema.GraphQLScalarType;

import java.time.format.DateTimeFormatter;

@PublicApi
public final class LocalDateTimeScalar {

    private LocalDateTimeScalar() {
    }

    public static GraphQLScalarType create(String name, boolean zoneConversionEnabled, DateTimeFormatter formatter) {
        return GraphQLScalarType.newScalar()
                .name(name != null ? name : "LocalDateTime")
                .description("Local Date Time type")
                .coercing(new GraphqlLocalDateTimeCoercing(zoneConversionEnabled, formatter != null ? formatter : DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

}
