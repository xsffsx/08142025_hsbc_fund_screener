package com.dummy.wmd.wpc.graphql.error;

import graphql.ErrorClassification;
import graphql.language.SourceLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MutationErrorTest {

    private MutationError mutationError;

    @Before
    public void setUp() {
        mutationError = new MutationError("message");
    }

    @Test
    public void getMessage_NoArgs_returnMessage() {
        String message = mutationError.getMessage();
        Assert.assertNotNull(message);
    }

    @Test
    public void getLocations() {
        List<SourceLocation> locations = mutationError.getLocations();
        Assert.assertNotNull(locations);
    }

    @Test
    public void getErrorType() {
        ErrorClassification errorType = mutationError.getErrorType();
        Assert.assertNotNull(errorType);

    }
}
