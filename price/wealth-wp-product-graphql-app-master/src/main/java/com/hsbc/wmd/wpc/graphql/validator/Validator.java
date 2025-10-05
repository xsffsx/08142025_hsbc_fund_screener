package com.dummy.wmd.wpc.graphql.validator;

public interface Validator {
    boolean support(Object value);
    String getName();
    String getDefaultMessage(Object value);

    void validate(Object value, ValidationContext context);
}
