package com.dummy.wmd.wpc.graphql.validator;

import java.util.List;
import java.util.Map;

/**
 * Validate String or List length.
 */
public class SizeValidator implements Validator {
    private Integer min = 0;
    private final Integer max;
    private String message = "size";

    public SizeValidator(Map<String, Object> param){
        this.min = (Integer)param.get("min");
        this.max = (Integer)param.get("max");
        if(null == min && null == max) {
            throw new IllegalArgumentException("min and max can't be both null: " + param);
        }
    }

    @Override
    public boolean support(Object value) {
        if(null == value) return false;
        return value instanceof List;
    }

    @Override
    public String getName() {
        return "size";
    }

    @Override
    public String getDefaultMessage(Object value) {
        int len = ((List)value).size();
        return String.format("size(min=%d, max=%d) exceed, size=%d", min, max, len);
    }

    @Override
    public void validate(Object value, ValidationContext context) {
        if(null == value) { // in case null, handled by required rule
            return;
        }

        boolean valid = true;
        if(value instanceof List) {
            List<? extends Object> list = (List)value;
            if(null != min && list.size() < min) {
                valid = false;
            }
            if(null != max && list.size() > max) {
                valid = false;
            }
        }
        if(!valid){
            context.addError(getName(), getDefaultMessage(value));
        }
    }

    @Override
    public String toString() {
        return "SizeValidator{" +
                "min=" + min +
                ", max=" + max +
                ", message='" + message + '\'' +
                '}';
    }
}
