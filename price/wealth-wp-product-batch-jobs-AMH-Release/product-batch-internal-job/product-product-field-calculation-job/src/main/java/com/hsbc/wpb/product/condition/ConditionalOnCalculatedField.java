package com.dummy.wpb.product.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnCalculatedFieldCondition.class)
public @interface ConditionalOnCalculatedField {
    String value();
}
