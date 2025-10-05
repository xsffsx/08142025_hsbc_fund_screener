/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import org.springframework.util.ReflectionUtils;

public class AtLeastOneGroupValidator implements ConstraintValidator<AtLeastOneGroup, Object> {

    private static final Logger logger = LoggerFactory.getLogger(AtLeastOneGroupValidator.class);

    private String exist;

    @Override
    public void initialize(final AtLeastOneGroup constraintAnnotation) {
        this.exist = constraintAnnotation.exist();
    }

    public boolean isValid(final Object obj, final ConstraintValidatorContext constraintContext) {
        boolean isValid = false;
        if (obj != null && StringUtil.isValid(this.exist)) {
            for (String group : this.exist.split(CommonConstants.SYMBOL_COMMA)) {
                if (isGroupValid(obj, group)) {
                    isValid = true;
                    break;
                }
            }
        }
        if (!isValid) {
            setMessage(constraintContext);
        }
        return isValid;
    }

    private boolean isGroupValid(final Object obj, final String group) {
        boolean isGroupValid = false;
        if (StringUtil.isValid(group)) {
            List<Boolean> result = new ArrayList<>();
            for (String item : group.split(CommonConstants.SYMBOL_SEPARATOR)) {
                try {
                    this.getFieldResult(obj, result, item);
                } catch (Exception e) {
                    AtLeastOneGroupValidator.logger.error("Please check your configuration: {}", this.exist);
                }
            }
            Set<Boolean> notRepeated = new HashSet<>(result);
            if (notRepeated.size() == 1 && Boolean.TRUE.equals(notRepeated.iterator().next())) {
                isGroupValid = true;
            }
        }
        return isGroupValid;
    }

    private void getFieldResult(Object obj, List<Boolean> result, String item) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(item);
        ReflectionUtils.makeAccessible(field);
        if (field.getType().isAssignableFrom(String.class)) {
            String value = String.valueOf(field.get(obj));
            result.add(StringUtil.isValid(value));
        } else if (field.getType().isAssignableFrom(String[].class)) {
            String[] array = (String[]) field.get(obj);
            result.add(array != null && array.length != 0);
        }
    }

    private void setMessage(final ConstraintValidatorContext constraintContext) {
        String message = constraintContext.getDefaultConstraintMessageTemplate();
        constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate(message + " [" + this.exist + "]").addConstraintViolation();
    }


}
