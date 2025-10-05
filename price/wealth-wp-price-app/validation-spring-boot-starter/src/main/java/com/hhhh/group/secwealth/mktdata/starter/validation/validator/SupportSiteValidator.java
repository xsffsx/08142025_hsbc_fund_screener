/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.validator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.SupportSite;
import com.hhhh.group.secwealth.mktdata.starter.validation.constant.Constant;
import com.hhhh.group.secwealth.mktdata.starter.validation.validator.util.ValidatorHelper;

public class SupportSiteValidator implements ConstraintValidator<SupportSite, Object> {

    @Autowired
    private ValidatorHelper helper;

    private String exist;

    @Override
    public void initialize(final SupportSite constraintAnnotation) {
        this.exist = this.helper.resolve(constraintAnnotation.exist());
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext constraintContext) {
        boolean isValid = false;
        final List<String> supportSite = Arrays.asList(this.exist.split(Constant.SYMBOL_COMMA));
        final String currentSite = String.valueOf(ArgsHolder.getArgs(Constant.ARGS_SITE));
        if (supportSite.contains(Constant.SUPPORT_SITE_ALL)) {
            isValid = true;
        } else if (supportSite.contains(currentSite)) {
            isValid = true;
        }
        if (!isValid) {
            this.helper.setMessage(constraintContext, currentSite);
        }
        return isValid;
    }

}
