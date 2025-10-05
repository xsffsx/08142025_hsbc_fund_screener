package com.dummy.wmd.wpc.graphql.validator;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * required(true | false)
 */
public class RequiredValidator implements Validator {
    private final boolean indicator;

    public RequiredValidator(boolean indicator){
        this.indicator = indicator;
    }

    @Override
    public boolean support(Object value) {
        return true;
    }

    @Override
    public String getName() {
        return "required";
    }

    @Override
    public String getDefaultMessage(Object value) {
        return "Required";
    }

    @Override
    public void validate(Object value, ValidationContext context) {
        if(!indicator) {
            return;
        }
        boolean valid = true;
        if(null == value) {
            valid = false;
        } else if(value instanceof String) {       // a string should have text
            valid = StringUtils.hasText((String)value);
        } else if(value instanceof List) {  // a list should have at least 1 element
            valid = !((List)value).isEmpty();
        }
        if(!valid){
            context.addError(getName(), getDefaultMessage(value));
        }
    }

    @Override
    public String toString() {
        return "RequiredValidator{" +
                "indicator=" + indicator +
                '}';
    }
}
