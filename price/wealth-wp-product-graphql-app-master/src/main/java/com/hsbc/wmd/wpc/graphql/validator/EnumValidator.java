package com.dummy.wmd.wpc.graphql.validator;

import java.util.*;

/**
 * required(true | false)
 */
public class EnumValidator implements Validator {
    private final List<String> values;

    public EnumValidator(List<String> values){
        this.values = values;
    }

    @Override
    public boolean support(Object value) {
        if(null == value) return false;
        return (value instanceof String);
    }

    @Override
    public String getName() {
        return "enum";
    }

    @Override
    public String getDefaultMessage(Object value) {
        return String.format("Value should be one of the enum values %s, value=%s", values, value);
    }

    @Override
    public void validate(Object value, ValidationContext context) {
        if(null == value) { // null case handled by required rule
            return;
        }
        if(!values.contains(value)){
            context.addError(getName(), getDefaultMessage(value));
        }
    }

    @Override
    public String toString() {
        return "EnumValidator{" +
                "values=" + values +
                '}';
    }
}
