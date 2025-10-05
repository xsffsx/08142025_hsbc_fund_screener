/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CoExistValidator.class})
@Documented
public @interface CoExist {

    String message() default "{CoExist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String exist();

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        CoExist[] value();
    }

}
