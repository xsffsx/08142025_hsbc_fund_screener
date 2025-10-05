/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.validator;

import java.lang.reflect.Field;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.ReflectionUtils;

public class XssSecurityValidator implements ConstraintValidator<XssSecurity, Object> {

    private static final Logger logger = LoggerFactory.getLogger(XssSecurityValidator.class);

    @Autowired
    @Qualifier("xssValidator")
    private ContainerValidator xssValidator;

    public boolean isValid(final Object obj, final ConstraintValidatorContext constraintContext) {
        boolean isValid = true;
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                if (field.getType().isAssignableFrom(String.class)) {
                    String value = String.valueOf(field.get(obj));
                    isValid = !this.xssValidator.isContain(value, true);
                    if (!isValid) {
                        this.setMessage(constraintContext, field.getName());
                        return isValid;
                    }
                }
            }
        } catch (Exception e) {
            XssSecurityValidator.logger.error("XssValidator Constraint Violated");
        }
        return isValid;
    }

    private void setMessage(final ConstraintValidatorContext constraintContext, final String param) {
        String message = constraintContext.getDefaultConstraintMessageTemplate();
        constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate(message + " [" + param + "]").addConstraintViolation();
    }

}
