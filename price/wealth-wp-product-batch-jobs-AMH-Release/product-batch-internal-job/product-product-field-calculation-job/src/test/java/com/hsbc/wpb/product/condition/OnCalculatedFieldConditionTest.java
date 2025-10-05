package com.dummy.wpb.product.condition;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class OnCalculatedFieldConditionTest {
    @Mock
    private ConditionContext context;
    @Mock
    private AnnotatedTypeMetadata metadata;

    @Test
    public void testMathes_true() {
        Environment environment = Mockito.mock(Environment.class);
        Mockito.when(context.getEnvironment()).thenReturn(environment);

        String args =  "ctryRecCde=HK,grpMembrRecCde=HBAP,calculatedField=eliRiskLvlCde";
        Mockito.when(environment.getProperty(anyString())).thenReturn(args);

        Map<String, Object> annotationAttributes = new HashMap<>();
        annotationAttributes.put("value","eliRiskLvlCde");
        Mockito.when(metadata.getAnnotationAttributes(anyString())).thenReturn(annotationAttributes);
        OnCalculatedFieldCondition condition = new OnCalculatedFieldCondition();
        Assertions.assertTrue(condition.matches(context,metadata));
    }

    @Test
    public void testMathes_false() {
        Environment environment = Mockito.mock(Environment.class);
        Mockito.when(context.getEnvironment()).thenReturn(environment);

        String args =  "ctryRecCde=HK,grpMembrRecCde=HBAP,calculatedField=eliRiskLvlCde";
        Mockito.when(environment.getProperty(anyString())).thenReturn(args);

        Map<String, Object> annotationAttributes = new HashMap<>();
        Mockito.when(metadata.getAnnotationAttributes(anyString())).thenReturn(annotationAttributes);
        OnCalculatedFieldCondition condition = new OnCalculatedFieldCondition();
        Assertions.assertFalse(condition.matches(context,metadata));
    }


}