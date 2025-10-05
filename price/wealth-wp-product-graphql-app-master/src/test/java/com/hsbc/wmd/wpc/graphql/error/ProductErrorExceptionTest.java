package com.dummy.wmd.wpc.graphql.error;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class productErrorExceptionTest {

    @Before
    public void setUp() {
        productErrorException productErrorException1 = new productErrorException(productErrors.ValidationError, "message");
        productErrorException productErrorException2 = new productErrorException(productErrors.ValidationError, "message", new HashMap<>());
        productErrorException productErrorException3 = new productErrorException(new HashMap<>(), "message");
        productErrorException productErrorException4 = new productErrorException(productErrors.ValidationError);
        productErrorException productErrorException5 = new productErrorException(new RuntimeException());
    }

    @Test
    public void testNewproductErrorException_NoArgs_returnsproductErrorExceptionBuilder() {
        productErrorException.Builder builder = productErrorException.newproductErrorException();
        Assert.assertNotNull(builder);
    }

    @Test
    public void testproductErrorExceptionBuilderbuild_NoArgs_returnsproductErrorException() {
        productErrorException.Builder builder = productErrorException.newproductErrorException();
        productErrorException build = builder.build();
        Assert.assertNotNull(build);
    }

    @Test
    public void testBuilder_NoArgs_doesNotThrow() {
        productErrorException.Builder builder = new productErrorException.Builder();
        productErrorException build = builder.build();
        Assert.assertNotNull(build);
    }
}
