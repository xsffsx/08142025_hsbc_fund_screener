package com.dummy.wmd.wpc.graphql.validator;


import java.time.LocalDate;
import java.util.Map;

/**
 * Validate String or List length.
 */
public class RangeValidator implements Validator {
    private String min;
    private String max;
    private boolean minInclusive = true;
    private boolean maxInclusive = true;

    public RangeValidator(Map<String, Object> param){
        if(null != param.get("min")) this.min = param.get("min").toString();
        if(null != param.get("max")) this.max = param.get("max").toString();
        if(null != param.get("minInclusive")) this.minInclusive = (Boolean) param.get("minInclusive");
        if(null != param.get("maxInclusive")) this.maxInclusive = (Boolean) param.get("maxInclusive");
        if(null == min && null == max) {
            throw new IllegalArgumentException("min and max can't be both null: " + param);
        }
    }

    @Override
    public boolean support(Object value) {
        if(null == value) return false;

        if(isNumeric(value)) {
            return true;
        } else {
            // yyyy-MM-dd to LocalDate (in case from JSON directly, mainly for testing)
            if(value instanceof String && ((String)value).matches("\\d{4}-\\d{2}-\\d{2}")){
                return true;
            }
            if(value instanceof LocalDate) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "range";
    }

    @Override
    public String getDefaultMessage(Object value) {
        return String.format("range(min=%s, max=%s) exceed, value=%s", min, max, value);
    }

    @Override
    public void validate(Object value, ValidationContext context) {
        if(null == value) { // in case null, handled by required rule
            return;
        }

        boolean valid = true;
        if(isNumeric(value)) {
            valid = isValidNumeric(value.toString());
        } else {
            // yyyy-MM-dd to LocalDate (in case from JSON directly, mainly for testing)
            if(value instanceof String && ((String)value).matches("\\d{4}-\\d{2}-\\d{2}")){
                value = LocalDate.parse((String)value);
            }
            if(value instanceof LocalDate) {
                valid = isValidDate((LocalDate)value);
            } else {
                throw new IllegalArgumentException("Expected numeric value");
                // handle DateTime compare if has use case
            }
        }
        if(!valid){
            context.addError(getName(), getDefaultMessage(value));
        }
    }

    /**
     * Check if the object is a number or float
     *
     * @param value
     * @return
     */
    public boolean isNumeric(Object value) {
        return value.toString().matches("[+-]?\\d+(\\.\\d+)?");
    }

    private boolean isValidDate(LocalDate value) {
        boolean valid = true;
        if(null != min){
            valid = minDateIsValid(value, valid);
        }
        if(null != max){
            valid = maxDateIsValid(value, valid);
        }
        return valid;
    }

    private boolean maxDateIsValid(LocalDate value, boolean valid) {
        LocalDate dmax = LocalDate.parse(max);
        if(maxInclusive){
            if(value.isAfter(dmax)) valid = false;
        } else {
            if(value.isEqual(dmax) || value.isAfter(dmax)) valid = false;
        }
        return valid;
    }

    private boolean minDateIsValid(LocalDate value, boolean valid) {
        LocalDate dmin = LocalDate.parse(min);
        if(minInclusive){
            if(value.isBefore(dmin)) valid = false;
        } else {
            if(value.isEqual(dmin) || value.isBefore(dmin)) valid = false;
        }
        return valid;
    }

    private boolean isValidNumeric(String value) {
        boolean valid = true;
        double dvalue = Double.parseDouble(value);
        if(null != min){
            valid = minNumberIsValid(dvalue, valid);
        }
        if(null != max){
            valid = maxNumberIsValid(dvalue, valid);
        }
        return valid;
    }

    private boolean maxNumberIsValid(double dvalue, boolean valid) {
        double dmax = Double.parseDouble(max);
        if(maxInclusive){
            if(dvalue > dmax) valid = false;
        } else {
            if(dvalue >= dmax) valid = false;
        }
        return valid;
    }

    private boolean minNumberIsValid(double dvalue, boolean valid) {
        double dmin = Double.parseDouble(min);
        if(minInclusive){
            if(dvalue < dmin) valid = false;
        } else {
            if(dvalue <= dmin) valid = false;
        }
        return valid;
    }

    @Override
    public String toString() {
        return "RangeValidator{" +
                "min='" + min + '\'' +
                ", max='" + max + '\'' +
                ", minInclusive=" + minInclusive +
                ", maxInclusive=" + maxInclusive +
                '}';
    }
}
