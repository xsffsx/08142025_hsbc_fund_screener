/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.hhhh.group.secwealth.mktdata.starter.validation.validator.SupportSiteValidator;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SupportSiteValidator.class})
@Documented
public @interface SupportSite {

    String message() default "{SupportSite}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String exist();

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        SupportSite[] value();
    }

}
