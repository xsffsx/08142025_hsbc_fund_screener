package com.dummy.wmd.wpc.graphql.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class EnumValidatorTest {
    @Mock
    private ValidationContext context;
    private EnumValidator enumValidator;

    @Before
    public void setUp() throws Exception {
        enumValidator = new EnumValidator(Arrays.asList("value"));
    }

    @Test
    public void testSupport_givenString_returnsTrue() {
        assertTrue(enumValidator.support("value"));
    }

    @Test
    public void testSupport_givenNull_returnsFalse() {
        assertFalse(enumValidator.support(null));
    }

    @Test
    public void testSupport_givenObject_returnsFalse() {
        assertFalse(enumValidator.support(new Date()));
    }

    @Test
    public void testGetName_givenNull_returnsString() {
        assertEquals("enum", enumValidator.getName());
    }

    @Test
    public void testGetDefaultMessage_givenValue_returnsString() {
        String message = enumValidator.getDefaultMessage("value");
        assertNotNull(message);
    }

    @Test
    public void testValidate_givenNull_returnsNull() {
        try {
            enumValidator.validate(null, context);
        } catch (Exception e) {
            Assert.fail();
        }


    }

    @Test
    public void testValidate_givenValue_returnsNull() {
        try {
            enumValidator.validate("value", context);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testValidate_givenValue2_returnsNull() {
        try {
            enumValidator.validate("value2", context);
        } catch (Exception e) {
            Assert.fail();
        }
    }


    @Test
    public void testToString() {
        assertNotNull(enumValidator.toString());
    }
}
