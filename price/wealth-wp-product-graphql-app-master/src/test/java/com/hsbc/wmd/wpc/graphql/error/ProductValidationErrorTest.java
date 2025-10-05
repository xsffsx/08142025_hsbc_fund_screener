package com.dummy.wmd.wpc.graphql.error;

import graphql.ErrorClassification;
import graphql.language.SourceLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductValidationErrorTest {

    private ProductValidationError productValidationError;

    @Before
    public void setUp() {
        productValidationError = new ProductValidationError("message", new HashMap<>());
    }

    @Test
    public void getMessage_NoArgs_returnMessage() {
        String message = productValidationError.getMessage();
        Assert.assertEquals("message", message);
    }

    @Test
    public void getLocations_NoArgs_returnSourceLocationList() {
        List<SourceLocation> locations = productValidationError.getLocations();
        Assert.assertNotNull(locations);
    }

    @Test
    public void getErrorType_NoArgs_returnErrorClassification() {
        ErrorClassification errorType = productValidationError.getErrorType();
        Assert.assertNotNull(errorType);

    }

    @Test
    public void getExtensions_NoArgs_returnExtensionsMao() {
        Map<String, Object> extensions = productValidationError.getExtensions();
        Assert.assertNotNull(extensions);
    }
}
