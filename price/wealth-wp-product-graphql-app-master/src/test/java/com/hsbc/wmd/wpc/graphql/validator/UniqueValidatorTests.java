package com.dummy.wmd.wpc.graphql.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UniqueValidatorTests {
    @Mock
    private ValidationContext context;
    private UniqueValidator uniqueValidator;

    @Before
    public void setUp() throws Exception {
        uniqueValidator = new UniqueValidator(Arrays.asList("value"));
    }
    @Test
    public void testSupport_givenValue_returnsBoolean() {
        assertFalse(uniqueValidator.support(null));
        assertTrue(uniqueValidator.support(new ArrayList<>()));
        assertFalse(uniqueValidator.support("value"));
    }


    @Test
    public void testGetName_givenNull_returnsString() {
        assertEquals("unique", uniqueValidator.getName());
    }

    @Test
    public void testGetDefaultMessage_givenValue_returnString() {
        assertNotNull(uniqueValidator.getDefaultMessage("value"));
    }

    @Test
    public void testValidate() {
        // Run the test
        uniqueValidator.validate(null, context);
        Map<String, Object> items = new HashMap<>();
        items.put("key","value");
        uniqueValidator.validate(Arrays.asList(items), context);
        uniqueValidator.validate(Arrays.asList(items,items), context);
        UniqueValidator uniqueValidator2 = new UniqueValidator();
        uniqueValidator2.validate(Arrays.asList(items,items), context);

        // Verify the results
    }

    @Test
    public void testToString() {
        assertNotNull(uniqueValidator.toString());
    }
}
