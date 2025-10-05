/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.RegEx;
import com.hhhh.group.secwealth.mktdata.starter.validation.validator.util.ValidatorHelper;

public class RegExValidator implements ConstraintValidator<RegEx, String> {

    @Autowired
    private ValidatorHelper helper;

    private String regexp;

    private String ignoreEmpty;

    @Override
    public void initialize(final RegEx constraintAnnotation) {
        this.regexp = this.helper.resolve(constraintAnnotation.regexp());
        this.ignoreEmpty = this.helper.resolve(constraintAnnotation.ignoreEmpty());
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext constraintContext) {
        if (StringUtils.isEmpty(this.regexp)) {
            return true;
        }
        boolean isValid;
        if (StringUtils.isEmpty(value)) {
            if ("Y".equals(this.ignoreEmpty)) {
                isValid = true;
            } else {
                isValid = false;
            }
        } else {
            isValid = value.matches(this.regexp);
        }
        if (!isValid) {
            this.helper.setMessage(constraintContext, this.regexp);
        }
        return isValid;
    }

}
