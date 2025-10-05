package com.dummy.wpb.wpc.utils.validation;

import com.dummy.wpb.wpc.utils.CommonUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class JsonSchemaValidatorTests {

    private JsonSchemaValidator jsonSchemaValidatorUnderTest;

    @Before
    public void setUp() throws Exception {
        String schemaContent = CommonUtils.readResource("/data/json-schema.json");
        jsonSchemaValidatorUnderTest = new JsonSchemaValidator("name", schemaContent, "configContent");
    }

    @Test
    public void testValidate() {
        // Run the test
        List<Error> result = jsonSchemaValidatorUnderTest.validate();
        assertNotNull(result);
    }
}
