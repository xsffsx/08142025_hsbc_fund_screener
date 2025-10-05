package com.dummy.wpb.product.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentField {
    String value() default "";

}
