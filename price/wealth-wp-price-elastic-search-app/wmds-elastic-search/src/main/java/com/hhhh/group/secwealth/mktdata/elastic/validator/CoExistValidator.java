/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import org.springframework.util.ReflectionUtils;

public class CoExistValidator implements ConstraintValidator<CoExist, Object> {

    private static final Logger logger = LoggerFactory.getLogger(CoExistValidator.class);

    private String exist;

    @Override
    public void initialize(final CoExist constraintAnnotation) {
        this.exist = constraintAnnotation.exist();
    }

    public boolean isValid(final Object obj, final ConstraintValidatorContext constraintContext) {
        boolean isValid = false;
        if (obj != null && StringUtil.isValid(this.exist)) {
            List<Boolean> result = new ArrayList<>();
            for (String parameter : this.exist.split(CommonConstants.SYMBOL_SEPARATOR)) {
                try {
                    Field field = obj.getClass().getDeclaredField(parameter);
                    ReflectionUtils.makeAccessible(field);
                    if (field.getType().isAssignableFrom(String.class)) {
                        String value = String.valueOf(field.get(obj));
                        result.add(StringUtil.isValid(value));
                    }
                } catch (Exception e) {
                    CoExistValidator.logger.error("Please check your configuration: {}", this.exist);
                }
            }
            if (new HashSet<>(result).size() == 1) {
                isValid = true;
            }
        }
        if (!isValid) {
            setMessage(constraintContext);
        }
        return isValid;
    }

    private void setMessage(final ConstraintValidatorContext constraintContext) {
        String message = constraintContext.getDefaultConstraintMessageTemplate();
        constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate(message + " [" + this.exist + "]").addConstraintViolation();
    }


}
