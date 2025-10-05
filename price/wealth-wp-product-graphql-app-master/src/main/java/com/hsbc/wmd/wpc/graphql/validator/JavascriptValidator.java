package com.dummy.wmd.wpc.graphql.validator;

import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Validate against Javascript expression.
 */
public class JavascriptValidator implements Validator {
    private String script;
    private String message;

    public JavascriptValidator(Map<String, Object> param){
        this.script = (String)param.get("script");
        this.message = (String)param.get("message");
        if(!StringUtils.hasText(script) || !StringUtils.hasText(message)) {
            throw new IllegalArgumentException("script and message are mandatory");
        }
    }

    @Override
    public boolean support(Object value) {
        return true;
    }

    @Override
    public String getName() {
        return "javascript";
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

        boolean valid = (Boolean)JavascriptEngine.eval(script, context.getProduct());
        if(!valid){
            String msg = context.evaluateTemplate(message);
            context.addError(getName(), msg);
        }
    }

    @Override
    public String toString() {
        return "JavascriptValidator{" +
                "script='" + script + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
