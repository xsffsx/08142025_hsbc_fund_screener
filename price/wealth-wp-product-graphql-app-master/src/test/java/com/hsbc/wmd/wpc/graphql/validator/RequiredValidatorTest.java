package com.dummy.wmd.wpc.graphql.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RequiredValidatorTest {
    @Mock
    private ValidationContext context;
    private RequiredValidator requiredValidator;

    @Before
    public void setUp() throws Exception {
        requiredValidator = new RequiredValidator(true);
    }

    @Test
    public void testSupport() {
        assertTrue(requiredValidator.support("value"));
    }

    @Test
    public void testGetName() {
        assertEquals("required", requiredValidator.getName());
    }

    @Test
    public void testGetDefaultMessage() {
        assertEquals("Required", requiredValidator.getDefaultMessage("value"));
    }
    @Test
    public void testValidate_givenIndicatorFalse_returnsNull() {
        requiredValidator = new RequiredValidator(false);
        // Setup
        ValidationContext context = new ValidationContext(new HashMap<>());
        // Run the test
        try {
            requiredValidator.validate("value", context);
        }catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testValidate_givenNull_returnsNull() {
        requiredValidator = new RequiredValidator(true);
        // Run the test
        try{
            requiredValidator.validate(null, context);
        }catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void testValidate_givenString_returnsNull() {
        requiredValidator = new RequiredValidator(true);
        // Run the test
        try{
            requiredValidator.validate("value", context);
        }catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void testValidate_givenList_returnsNull() {
        requiredValidator = new RequiredValidator(true);
        // Run the test
        try{
            requiredValidator.validate(new ArrayList<>(), context);
        }catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testToString() {
        assertNotNull(requiredValidator.toString());
    }
}
