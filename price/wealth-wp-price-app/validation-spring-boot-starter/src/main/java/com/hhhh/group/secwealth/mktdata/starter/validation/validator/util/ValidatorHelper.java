/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.validator.util;

import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.starter.validation.constant.Constant;

import lombok.Setter;

public class ValidatorHelper {

    private static final Logger logger = LoggerFactory.getLogger(ValidatorHelper.class);

    @Setter
    private Environment env;

    public String resolve(final String value) {
        String result;
        if (isQuote(value)) {
            final String propertyKey = getPropertyKey(value);
            if (isContainsDefaultValue(propertyKey)) {
                final int index = propertyKey.indexOf(Constant.SYMBOL_COLON);
                final String propertyName = propertyKey.substring(0, index);
                final String defaultValue = propertyKey.substring(index + 1);
                result = getProperty(propertyName);
                if (StringUtils.isEmpty(result)) {
                    ValidatorHelper.logger.error(propertyKey + " use default value: " + defaultValue);
                    result = defaultValue;
                }
            } else {
                result = getProperty(propertyKey);
            }
        } else {
            result = value;
        }
        return result;
    }

    private boolean isQuote(final String value) {
        return value.startsWith(Constant.SYMBOL_LEFT_BRACES) && value.endsWith(Constant.SYMBOL_RIGHT_BRACES);
    }

    private String getPropertyKey(final String value) {
        return value.replace(Constant.SYMBOL_LEFT_BRACES, Constant.EMPTY).replace(Constant.SYMBOL_RIGHT_BRACES, Constant.EMPTY);
    }

    private boolean isContainsDefaultValue(final String propertyKey) {
        return propertyKey.contains(Constant.SYMBOL_COLON);
    }

    private String getProperty(final String propertyKey) {
        final String result = this.env.getProperty(propertyKey);
        if (StringUtils.isEmpty(result)) {
            ValidatorHelper.logger.error("Please check your configuration, " + propertyKey + " not defined");
        }
        return result;
    }

    public void setMessage(final ConstraintValidatorContext constraintContext, final String value) {
        final String message = constraintContext.getDefaultConstraintMessageTemplate();
        constraintContext.disableDefaultConstraintViolation();
        constraintContext
            .buildConstraintViolationWithTemplate(
                message + Constant.BLANK + Constant.SYMBOL_LEFT_SQUARE_BRACKET + value + Constant.SYMBOL_RIGHT_SQUARE_BRACKET)
            .addConstraintViolation();
    }

}
