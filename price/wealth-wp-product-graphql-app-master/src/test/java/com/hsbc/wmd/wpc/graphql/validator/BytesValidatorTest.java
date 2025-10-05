package com.dummy.wmd.wpc.graphql.validator;

import com.google.common.collect.ImmutableMap;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

public class BytesValidatorTest {

    BytesValidator bytesValidator = new BytesValidator(
            new ImmutableMap.Builder<String, Object>()
                    .put("min", 5)
                    .put("max", 10)
                    .build());


    @Test
    public void testValidate_givenNoString() {
        Object object = new Object();
        Assert.assertFalse(bytesValidator.support(object));

        ValidationContext context = new ValidationContext(new Document());
        bytesValidator.validate(object, context);
        Assert.assertTrue(context.getViolations().isEmpty());
    }

    @Test
    public void testValidate_givenLessThanMinValue() {
        String str = "abc";
        Assert.assertTrue(bytesValidator.support(str));

        ValidationContext context = new ValidationContext(new Document());
        context.pushNestedPath("field");
        bytesValidator.validate(str, context);
        Assert.assertFalse(context.getViolations().isEmpty());
        Error error = context.getViolations().get(0);
        Assert.assertEquals("field@bytes", error.getCode());
    }

    @Test
    public void testValidate_givenLongerThanMinValue() {
        String str = "abcdefghijklmno";
        Assert.assertTrue(bytesValidator.support(str));

        ValidationContext context = new ValidationContext(new Document());
        context.pushNestedPath("field");
        bytesValidator.validate(str, context);
        Assert.assertFalse(context.getViolations().isEmpty());
        Error error = context.getViolations().get(0);
        Assert.assertEquals("field@bytes", error.getCode());
    }

    @Test
    public void testNewInstance() {
        bytesValidator = new BytesValidator(
                new ImmutableMap.Builder<String, Object>()
                        .put("min", 5)
                        .build());
        bytesValidator = new BytesValidator(
                new ImmutableMap.Builder<String, Object>()
                        .put("max", 5)
                        .build());
        Map<String, Object> emptyMap = Collections.emptyMap();
        Assert.assertThrows(IllegalArgumentException.class, () -> bytesValidator = new BytesValidator(emptyMap));

    }
}
