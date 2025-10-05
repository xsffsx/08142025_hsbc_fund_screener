package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.schema.GraphQLScalarType;
import org.junit.Assert;
import org.junit.Test;

public class LocalDateTimeScalarTest {

    @Test
    public void testCreate_omitsNameAndFormatter_returnsGraphQLScalarType() {
        GraphQLScalarType graphQLScalarType = LocalDateTimeScalar.create(null, true, null);
        Assert.assertNotNull(graphQLScalarType);
    }
}
