package com.dummy.wpb.product.condition;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.mock.env.MockEnvironment;

import java.util.Collections;
import java.util.Map;

class OnConditionalOnJobParametersTest {

    @ParameterizedTest
    @CsvSource(value = {
            "''|''|false",
            "ctryRecCde=HK, grpMembrRecCde=HBAP|ctryRecCde,HK,grpMembrRecCde,dummy|false",
            "ctryRecCde=HK, grpMembrRecCde=HBAP|ctryRecCde,HK,grpMembrRecCde|false",
            "ctryRecCde=HK, grpMembrRecCde=HBAP|''|false",
            "ctryRecCde=HK, grpMembrRecCde=HBAP|ctryRecCde,HK,grpMembrRecCde,HBAP|true"

    }, delimiter = '|')
    void testOnConditionalOnJobParameters(final String jobParams, final String conditionParams, final boolean expectedResult) {
        ConditionContext context = Mockito.mock(ConditionContext.class);
        MockEnvironment env = new MockEnvironment();
        env.setProperty("nonOptionArgs", jobParams);
        Mockito.when(context.getEnvironment()).thenReturn(env);

        AnnotatedTypeMetadata metadata = Mockito.mock(AnnotatedTypeMetadata.class);

        Map<String, Object> annotationAttributes = Collections.singletonMap("value", conditionParams.split(","));
        Mockito.when(metadata.getAnnotationAttributes(ConditionalOnJobParameters.class.getName())).thenReturn(annotationAttributes);

        OnConditionalOnJobParameters conditional = new OnConditionalOnJobParameters();
        Assertions.assertEquals(expectedResult, conditional.matches(context, metadata));
    }
}
