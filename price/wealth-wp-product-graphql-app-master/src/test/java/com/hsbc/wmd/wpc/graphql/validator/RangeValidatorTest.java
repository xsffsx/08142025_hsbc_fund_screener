package com.dummy.wmd.wpc.graphql.validator;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("java:S5778")
public class RangeValidatorTest {
    @Test
    public void testIsNumberic() {
        Map<String, Object> param = new HashMap<>();
        param.put("min", 1);
        param.put("max", 10);

        RangeValidator validator = new RangeValidator(param);

        Map<String, Object> object = new HashMap<>();
        object.put("case 1", 1);
        object.put("case 2", 10);
        object.put("case 3", "10");
        object.put("case 4", +10);
        object.put("case 5", -10);
        object.put("case 6", "+10");
        object.put("case 7", "-10");
        object.put("case 8", "+10.0");
        object.put("case 9", "-10.0");
        object.put("case 10", "10.");
        object.put("case 11", "abc");
        object.put("case 12", "abc123");
        object.put("case 13", "123abc");
        object.put("case 14", "+-123");
        object.put("case 15", "+-123.00");
        object.put("case 16", "123.000");
        object.put("case 17", "123.001");
        object.put("case 18", "123.001+");
        object.put("case 19", "123.001-");
        object.put("case 20", "0.000");
        object.put("case 21", "0.0001");

        assertEquals(true, validator.isNumeric(object.get("case 1")));
        assertEquals(true, validator.isNumeric(object.get("case 2")));
        assertEquals(true, validator.isNumeric(object.get("case 3")));
        assertEquals(true, validator.isNumeric(object.get("case 4")));
        assertEquals(true, validator.isNumeric(object.get("case 5")));
        assertEquals(true, validator.isNumeric(object.get("case 6")));
        assertEquals(true, validator.isNumeric(object.get("case 7")));
        assertEquals(true, validator.isNumeric(object.get("case 8")));
        assertEquals(true, validator.isNumeric(object.get("case 9")));
        assertEquals(false, validator.isNumeric(object.get("case 10")));
        assertEquals(false, validator.isNumeric(object.get("case 11")));
        assertEquals(false, validator.isNumeric(object.get("case 12")));
        assertEquals(false, validator.isNumeric(object.get("case 13")));
        assertEquals(false, validator.isNumeric(object.get("case 14")));
        assertEquals(false, validator.isNumeric(object.get("case 15")));
        assertEquals(true, validator.isNumeric(object.get("case 16")));
        assertEquals(true, validator.isNumeric(object.get("case 17")));
        assertEquals(false, validator.isNumeric(object.get("case 18")));
        assertEquals(false, validator.isNumeric(object.get("case 19")));
        assertEquals(true, validator.isNumeric(object.get("case 20")));
        assertEquals(true, validator.isNumeric(object.get("case 21")));
    }

    @Test
    public void testSupport() {
        Map<String, Object> param = new HashMap<>();
        param.put("min", 1);
        param.put("max", 10);
        RangeValidator validator = new RangeValidator(param);

        Map<String, Object> object = new HashMap<>();
        object.put("case 1", 1);
        object.put("case 2", "abc");
        object.put("case 3", null);

        assertEquals(false, validator.support(null));
        assertEquals(true, validator.support(object.get("case 1")));
        assertEquals(false, validator.support(object.get("case 2")));
        assertEquals(false, validator.support(object.get("case 3")));
    }

    @Test
    public void testValidateWithNumberValue() {
        Map<String, Object> param = new HashMap<>();
        param.put("min", 1);
        param.put("max", 10);
        RangeValidator validator = new RangeValidator(param);

        ValidationContext ctx = mock(ValidationContext.class);
        doNothing().when(ctx).addError(isA(String.class), isA(String.class));

        Map<String, Object> object = new HashMap<>();
        object.put("case 1", 1);
        object.put("case 2", 10);
        object.put("case 3", 1.00);
        object.put("case 4", 66);
        object.put("case 5", -200);
        object.put("case 6", "abc");

        try {
            validator.validate(object.get("case 1"), ctx);
            validator.validate(object.get("case 2"), ctx);
            validator.validate(object.get("case 3"), ctx);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }

        validator.validate(object.get("case 4"), ctx);
        verify(ctx, times(1)).addError("range", "range(min=1, max=10) exceed, value=66");

        validator.validate(object.get("case 5"), ctx);
        verify(ctx, times(1)).addError("range", "range(min=1, max=10) exceed, value=-200");

        try {
            validator.validate(object.get("case 6"), ctx);
            fail("validator.validate didn't throw when I expected it to");
        } catch (IllegalArgumentException expectedException) {
            assertEquals("Expected numeric value", expectedException.getMessage());
        }
    }

    @Test
    public void testValidateWithDateValue() {
        Map<String, Object> param = new HashMap<>();
        param.put("min", "2021-10-01");
        param.put("max", "2021-10-31");
        RangeValidator validator = new RangeValidator(param);

        ValidationContext ctx = mock(ValidationContext.class);
        doNothing().when(ctx).addError(isA(String.class), isA(String.class));

        Map<String, Object> object = new HashMap<>();
        object.put("case 1", "2021-10-19");
        object.put("case 2", "2021-10-01");
        object.put("case 3", "2021-10-31");
        object.put("case 4", "2021-01-01");

        try {
            validator.validate(object.get("case 1"), ctx);
            validator.validate(object.get("case 2"), ctx);
            validator.validate(object.get("case 3"), ctx);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }

        validator.validate(object.get("case 4"), ctx);
        verify(ctx, times(1)).addError("range", "range(min=2021-10-01, max=2021-10-31) exceed, value=2021-01-01");
    }

    @Test
    public void testValidateDateWithNumberRange() {
        Map<String, Object> param = new HashMap<>();
        param.put("min", "1");
        param.put("max", "10");
        RangeValidator validator = new RangeValidator(param);

        ValidationContext ctx = mock(ValidationContext.class);
        doNothing().when(ctx).addError(isA(String.class), isA(String.class));

        Map<String, Object> object = new HashMap<>();
        object.put("case 1", "2021-10-19");

        try {
            validator.validate(object.get("case 1"), ctx);
        } catch (Exception e) {
            // fail("Should not have thrown any exception");
            assertEquals("Text '1' could not be parsed at index 0", e.getMessage());
        }

        // validator.validate(object.get("case 4"), ctx);
        // verify(ctx, times(1)).addError("range", "range(min=2021-10-01, max=2021-10-31) exceed, value=2021-01-01");
    }

    @Test
    public void testValidateNumberWithDateRange() {
        Map<String, Object> param = new HashMap<>();
        param.put("min", "2021-10-01");
        param.put("max", "2021-10-31");
        RangeValidator validator = new RangeValidator(param);

        ValidationContext ctx = mock(ValidationContext.class);
        doNothing().when(ctx).addError(isA(String.class), isA(String.class));

        Map<String, Object> object = new HashMap<>();
        object.put("case 1", "1");

        try {
            validator.validate(object.get("case 1"), ctx);
        } catch (Exception e) {
            // fail("Should not have thrown any exception");
            assertEquals("For input string: \"2021-10-01\"", e.getMessage());
        }

        // validator.validate(object.get("case 4"), ctx);
        // verify(ctx, times(1)).addError("range", "range(min=2021-10-01, max=2021-10-31) exceed, value=2021-01-01");
    }
}
