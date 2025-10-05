package com.dummy.wmd.wpc.graphql.scalar.datetime;

import graphql.Assert;
import graphql.schema.GraphQLScalarType;
import org.junit.Test;

public class LocalTimeScalarTest {

    @Test
    public void testCreate_givenStringName_returnsGraphQLScalarType() {
        GraphQLScalarType graphQLScalarType = LocalTimeScalar.create("LocalTime");
        Assert.assertNotNull(graphQLScalarType);
    }
}
