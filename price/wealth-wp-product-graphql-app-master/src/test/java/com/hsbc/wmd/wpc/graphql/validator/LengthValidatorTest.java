package com.dummy.wmd.wpc.graphql.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LengthValidatorTest {
    @Mock
    private ValidationContext context;

    private LengthValidator lengthValidator;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("min", 3);
        param.put("max", 10);
        lengthValidator = new LengthValidator(param);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLengthValidator_givenNewMap_throwsException() {
        new LengthValidator(new HashMap<>());
    }

    @Test
    public void testSupport_givenString_returnsTrue() {
        assertTrue(lengthValidator.support("value"));
    }

    @Test
    public void testSupport_givenNull_returnsFalse() {
        assertFalse(lengthValidator.support(null));
    }

    @Test
    public void testSupport_givenObject_returnsFalse() {
        assertFalse(lengthValidator.support(new Date()));
    }

    @Test
    public void testGetName_givenNull_returnsString() {
        assertEquals("length", lengthValidator.getName());
    }

    @Test
    public void testGetDefaultMessage_givenObject_returnsString() {
        assertNotNull(lengthValidator.getDefaultMessage("value"));
    }

    @Test
    public void testValidate_givenLongString_returnsNull() {
        // Run the test
        try {
            lengthValidator.validate("test LengthValidator", context);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testValidate_givenString_returnsNull() {
        // Run the test
        try {
            lengthValidator.validate("test", context);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testValidate_givenShortString_returnsNull() {
        // Run the test
        try {
            lengthValidator.validate("t", context);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testValidate_givenNull_returnsNull() {
        // Run the test
        try {
            lengthValidator.validate(null, context);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void testToString() {
        assertNotNull(lengthValidator.toString());
    }
}
