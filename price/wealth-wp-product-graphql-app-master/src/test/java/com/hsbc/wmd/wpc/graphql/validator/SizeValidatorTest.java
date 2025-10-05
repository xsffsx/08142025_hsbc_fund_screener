package com.dummy.wmd.wpc.graphql.validator;

import org.junit.Assert;
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
public class SizeValidatorTest {
    @Mock
    private ValidationContext context;
    private SizeValidator sizeValidator;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("min",2);
        param.put("max",3);
        sizeValidator = new SizeValidator(param);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSizeValidator_givenNewMap_throwsException() {
        new SizeValidator(new HashMap<>());
    }
    @Test
    public void testSupport_givenNull_returnsFalse() {
        assertFalse(sizeValidator.support(null));
    }
    @Test
    public void testSupport_givenString_returnsFalse() {
        assertFalse(sizeValidator.support("value"));
    }
    @Test
    public void testSupport_givenList_returnsTrue() {
        assertTrue(sizeValidator.support(new ArrayList<>()));
    }

    @Test
    public void testGetName_givenNull_returnsString() {
        assertEquals("size", sizeValidator.getName());
    }

    @Test
    public void testGetDefaultMessage_givenList_returnsString() {
        assertNotNull(sizeValidator.getDefaultMessage(Arrays.asList("value1","value2")));
    }

    @Test
    public void testValidate_givenNull_returnsNull() {
        try {
            sizeValidator.validate(null, context);
        }catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void testValidate_givenList_returnsNull() {
        try {
            sizeValidator.validate(Arrays.asList("value1","value2"), context);
        }catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void testValidate_givenMinList_returnsNull() {
        try {
            sizeValidator.validate(Arrays.asList("value1"), context);
        }catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void testValidate_givenMaxList_returnsNull() {
        try {
            sizeValidator.validate(Arrays.asList("value1","value2","value3","value4"), context);
        }catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testToString() {
        assertNotNull( sizeValidator.toString());
    }
}
