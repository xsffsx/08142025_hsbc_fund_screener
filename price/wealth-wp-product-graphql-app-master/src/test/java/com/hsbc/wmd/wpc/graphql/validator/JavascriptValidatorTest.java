package com.dummy.wmd.wpc.graphql.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class JavascriptValidatorTest {
    @Mock
    private ValidationContext context;
    private JavascriptValidator javascriptValidator;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("script", "script");
        param.put("message", "message");
        javascriptValidator = new JavascriptValidator(param);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavascriptValidator_givenNewMap_throwsException() {
        javascriptValidator = new JavascriptValidator(new HashMap<>());
    }

    @Test
    public void testSupport_givenObject_returnsBoolean() {
        assertTrue(javascriptValidator.support("value"));
    }

    @Test
    public void testGetName_givenNull_returnsString() {
        assertEquals("javascript", javascriptValidator.getName());
    }

    @Test
    public void testGetDefaultMessage_givenObject_returnsNull() {
        assertNull(javascriptValidator.getDefaultMessage("value"));
    }

    @Test
    public void testToString() {
        assertNotNull(javascriptValidator.toString());
    }

    @Test
    public void testValidate_givenNull_returnsNull() {
        try {
            javascriptValidator.validate(null, context);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testValidate_givenObjectAndContext_returnsNull() {
        try {
            MockedStatic<JavascriptEngine> engine = Mockito.mockStatic(JavascriptEngine.class);
            engine.when(() -> JavascriptEngine.eval(anyString(), anyMap())).thenReturn(false);
            javascriptValidator.validate(new Object(), context);
            engine.close();
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
