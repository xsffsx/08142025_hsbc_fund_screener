package com.dummy.wmd.wpc.graphql.validator;

import org.junit.Before;
import org.junit.Test;

import static graphql.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ErrorTests {

    private Error error;

    @Before
    public void setUp() throws Exception {
        error = new Error("jsonPath", "code", "message");
    }
    @Test
    public void testError(){
        new Error();
        assertNotNull(error.getCode());
        assertNotNull(error.getMessage());
        assertNotNull(error.toString());
    }

    @Test
    public void testSetJsonPath_givenRootJsonPath_returnsNull() {
        String jsonPath = "[ROOT].jsonPath";
        // Run the test
        error.setJsonPath(jsonPath);
        assertEquals("jsonPath", error.getJsonPath());
    }
    @Test
    public void testSetJsonPath_givenJsonPath_returnsNull() {
        String jsonPath = "jsonPath";
        // Run the test
        error.setJsonPath(jsonPath);
        assertEquals("jsonPath", error.getJsonPath());
    }

}
