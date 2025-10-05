package com.dummy.wpb.product.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnConditionalOnJobParameters.class)
public @interface ConditionalOnJobParameters {
    String[] value();
}
