package com.dummy.wmd.wpc.graphql.validator;

import java.util.Map;

/**
 * Validate String or List length.
 */
public class LengthValidator implements Validator {
    private Integer min = 0;
    private final Integer max;

    public LengthValidator(Map<String, Object> param){
        this.min = (Integer)param.get("min");
        this.max = (Integer)param.get("max");
        if(null == min && null == max) {
            throw new IllegalArgumentException("min and max can't be both null: " + param);
        }
    }

    @Override
    public boolean support(Object value) {
        if(null == value) return false;
        return value instanceof String;
    }

    @Override
    public String getName() {
        return "length";
    }

    @Override
    public String getDefaultMessage(Object value) {
        return String.format("length(min=%d, max=%d) exceed, value=%s", min, max, value);
    }

    @Override
    public void validate(Object value, ValidationContext context) {
        if(null == value) { // in case null, handled by required rule
            return;
        }

        boolean valid = true;
        if(value instanceof String) {
            String s = (String)value;
            if(null != min && s.length() < min) {
                valid = false;
            }
            if(null != max && s.length() > max) {
                valid = false;
            }
        }
        if(!valid){
            context.addError(getName(), getDefaultMessage(value));
        }
    }

    @Override
    public String toString() {
        return "LengthValidator{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
