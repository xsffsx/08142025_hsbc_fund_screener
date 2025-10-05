package com.dummy.wpb.product.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;
@Slf4j
public class OnCalculatedFieldCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String args = context.getEnvironment().getProperty("nonOptionArgs");
        if (!StringUtils.hasText(args)) {
            return false;
        }
        Properties properties = StringUtils.splitArrayElementsIntoProperties(args.split(","), "=");
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnCalculatedField.class.getName());
        if(annotationAttributes == null){
            return false;
        }
        String requiredCalculatedField = (String) annotationAttributes.get("value");
        if (!StringUtils.hasText(requiredCalculatedField) || properties == null) {
            return false;
        }
        return Objects.equals(properties.getProperty("calculatedField"), requiredCalculatedField);
    }
}
