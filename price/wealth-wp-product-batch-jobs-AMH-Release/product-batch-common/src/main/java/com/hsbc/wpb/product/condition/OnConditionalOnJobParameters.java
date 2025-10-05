package com.dummy.wpb.product.condition;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Properties;

@Slf4j
public class OnConditionalOnJobParameters implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String args = context.getEnvironment().getProperty("nonOptionArgs");
        if (!StringUtils.hasText(args)) {
            return false;
        }

        Properties properties = StringUtils.splitArrayElementsIntoProperties(args.split(","), "=");
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnJobParameters.class.getName());
        if (null == annotationAttributes || null == properties) {
            return false;
        }

        String[] parameters = (String[]) annotationAttributes.get("value");
        if (ArrayUtils.isEmpty(parameters) || (parameters.length % 2) != 0) {
            return false;
        }

        return match(parameters, properties);
    }

    private boolean match(String[] parameters, Properties properties) {
        for (int i = 0; i < parameters.length; i = i + 2) {
            String parmKey = parameters[i];
            String parmValue = parameters[i + 1];
            if (!properties.getProperty(parmKey).matches(parmValue)) {
                return false;
            }
        }
        return true;
    }
}
