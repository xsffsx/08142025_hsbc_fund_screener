package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Deque;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class ValidationContextTest {
    private Document prod;
    @Mock
    private Deque<String> nestedPathStack ;
    @Mock
    private StandardEvaluationContext evaluationContext;
    @Mock
    private SpelExpressionParser spelExpressionParser;
    @Mock
    private Expression expression;

    private ValidationContext validationContext;

    @Before
    public void setUp() throws Exception {
        prod = CommonUtils.readResourceAsDocument("/files/UT-40004996.json");
        validationContext = new ValidationContext(prod);
        ReflectionTestUtils.setField(validationContext, "nestedPathStack", nestedPathStack);
        ReflectionTestUtils.setField(validationContext, "spelExpressionParser", spelExpressionParser);
        ReflectionTestUtils.setField(validationContext, "evaluationContext", evaluationContext);
        Mockito.when(nestedPathStack.getFirst()).thenReturn("jsonPath");
    }

    @Test
    public void testGetCtryRecCde_givenNull_returnsString() {
        String result = validationContext.getCtryRecCde();
        assertNotNull(result);
    }

    @Test
    public void testGetGrpMembrRecCde_givenNull_returnsString() {
        String result = validationContext.getGrpMembrRecCde();
        assertNotNull(result);
    }

    @Test
    public void testAddError_givenString_returnsNull() {

        try {
            validationContext.addError("rule", "defaultMessage");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetNestedPath_givenNull_returnsString() {
        String result = validationContext.getNestedPath();
        assertNotNull(result);
    }

    @Test
    public void testPushNestedPath_givenString_returnsNull() {
        try {
             Mockito.when(nestedPathStack.size()).thenReturn(2);
            Mockito.when(nestedPathStack.getFirst()).thenReturn("parent");
            validationContext.pushNestedPath("subPath");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testPushNestedPath_givenString2_returnsNull() {
        try {
            validationContext.pushNestedPath("sub.Path");
        } catch (Exception e) {
            Assert.fail();
        }
    }
    @Test
    public void testPushNestedPath_givenInteger_returnsNull() {
        try {
            validationContext.pushNestedPath(1);
        } catch (Exception e) {
            Assert.fail();
        }
    }
    @Test
    public void testPopNestedPath_givenNull_returnsString() {
        Mockito.when(nestedPathStack.pop()).thenReturn("path");
        String result = validationContext.popNestedPath();
        // Verify the results
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopNestedPath_ThrowsIllegalStateException() {
        Mockito.when(nestedPathStack.pop()).thenThrow(NoSuchElementException.class);
        validationContext.popNestedPath();
    }

    @Test
    public void testEvaluateCondition_givenEmptyStr_returnsTrue() {
        boolean result = validationContext.evaluateCondition("");
        assertTrue(result);
    }
    @Test
    public void testEvaluateCondition_givenString_returnsFalse() {
        Mockito.when(spelExpressionParser.parseExpression(anyString())).thenReturn(expression);
        Mockito.when(expression.getValue(evaluationContext,Boolean.class)).thenReturn(false);
        boolean result = validationContext.evaluateCondition("condition");
        assertFalse(result);
    }
    @Test
    public void testEvaluateCondition_givenString_returnsTrue() {
        Mockito.when(spelExpressionParser.parseExpression(anyString())).thenReturn(expression);
        Mockito.when(expression.getValue(evaluationContext,Boolean.class)).thenThrow(SpelEvaluationException.class);
        boolean result = validationContext.evaluateCondition("condition");
        assertTrue(result);
    }


    @Test
    public void testResolveVariable_givenString_returnsObject() {
        Object result = validationContext.resolveVariable("variable");
        assertNotNull(result);
    }

    @Test
    public void testEvaluateTemplate_givenString_returnsString() {
        Mockito.when(spelExpressionParser.parseExpression(anyString(),any(ParserContext.class))).thenReturn(expression);
        Mockito.when(expression.getValue(evaluationContext,String.class)).thenReturn("msg");
        String result = validationContext.evaluateTemplate("template");
        assertNotNull(result);
    }
}
