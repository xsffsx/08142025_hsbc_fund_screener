package com.dummy.wmd.wpc.graphql.error;


import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S1948")
public class ProductValidationError implements GraphQLError {
    private String message;
    private Map<String, Object> reason;

    public ProductValidationError(String message, Map<String, Object> reason) {
        this.message = message;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return Collections.emptyList();
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.ValidationError;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return reason;
    }
}
