package com.dummy.wmd.wpc.graphql.validator;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BytesValidator implements Validator {

    private final Integer min;
    private final Integer max;

    public BytesValidator(Map<String, Object> param) {
        this.min = (Integer) param.get("min");
        this.max = (Integer) param.get("max");
        if (null == min && null == max) {
            throw new IllegalArgumentException("min and max can't be both null: " + param);
        }
    }

    @Override
    public boolean support(Object value) {
        return value instanceof String;
    }

    @Override
    public String getName() {
        return "bytes";
    }

    @Override
    public String getDefaultMessage(Object value) {
        return String.format("bytes(min=%d, max=%d) exceed, value=%s", min, max, value);
    }

    @Override
    public void validate(Object value, ValidationContext context) {
        if (value instanceof String) {
            int bytesLength = ((String) value).getBytes(StandardCharsets.UTF_8).length;
            if (null != min && bytesLength < min) {
                context.addError(getName(), getDefaultMessage(value));
            }
            if (null != max && bytesLength > max) {
                context.addError(getName(), getDefaultMessage(value));
            }
        }
    }
}
