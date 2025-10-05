package com.dummy.wmd.wpc.graphql.validator;

import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Validate against SpEL expression.
 */
public class SpELValidator implements Validator {
    private String expression;
    private String message;

    public SpELValidator(Map<String, Object> param){
        this.expression = (String)param.get("expression");
        this.message = (String)param.get("message");
        if(!StringUtils.hasText(expression) || !StringUtils.hasText(message)) {
            throw new IllegalArgumentException("expression and message are mandatory");
        }
    }

    @Override
    public boolean support(Object value) {
        return true;
    }

    @Override
    public String getName() {
        return "spel";
    }

    @Override
    public String getDefaultMessage(Object value) {
        return null;
    }

    @Override
    public void validate(Object value, ValidationContext context) {
        if(null == value) { // in case null, handled by required rule
            return;
        }

        boolean valid = context.evaluateCondition(expression);
        if(!valid){
            String msg = context.evaluateTemplate(message);
            context.addError(getName(), msg);
        }
    }

    @Override
    public String toString() {
        return "SpELValidator{" +
                "expression='" + expression + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
