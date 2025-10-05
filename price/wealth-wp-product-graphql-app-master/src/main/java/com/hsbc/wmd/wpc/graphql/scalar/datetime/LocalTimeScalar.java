package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.PublicApi;
import graphql.schema.GraphQLScalarType;

@PublicApi
public final class LocalTimeScalar {

    private LocalTimeScalar() {
    }

    public static GraphQLScalarType create(String name) {
        return GraphQLScalarType.newScalar()
                .name(name != null ? name : "LocalTime")
                .description("Local Time type")
                .coercing(new GraphqlLocalTimeCoercing())
                .build();
    }
}
