package com.dummy.wmd.wpc.graphql.error;

import com.google.common.collect.ImmutableMap;
import graphql.GraphqlErrorException;

import java.util.Collections;
import java.util.Map;

@SuppressWarnings("java:S110")
public class productErrorException extends GraphqlErrorException {
    public static final String product_ERROR_CODE = "productErrorCode";

    public productErrorException(productErrors error, String message) {
        this(newproductErrorException()
                .message(message)
                .extensions(Collections.singletonMap(product_ERROR_CODE, error)));
    }

    public productErrorException(productErrors error, String message, Map<String, Object> extensions) {
        this(newproductErrorException()
                .message(message)
                .extensions(
                        new ImmutableMap.Builder<String, Object>()
                                .put(product_ERROR_CODE, error)
                                .putAll(extensions)
                                .build()
                ));
    }

    public productErrorException(Map<String, Object> extensions, String message) {
        this(newproductErrorException()
                .message(message)
                .extensions(extensions));
    }

    public productErrorException(productErrors error) {
        this(newproductErrorException()
                .extensions(Collections.singletonMap(product_ERROR_CODE, error)));
    }

    public productErrorException(Throwable cause) {
        this(newproductErrorException()
                .message(cause.getMessage())
                .cause(cause)
                .extensions(Collections.singletonMap(product_ERROR_CODE, productErrors.RuntimeException)));
    }

    private productErrorException(productErrorException.Builder builder) {
        super(builder);
    }

    public static productErrorException.Builder newproductErrorException() {
        return new productErrorException.Builder();
    }

    public static class Builder extends BuilderBase<productErrorException.Builder, productErrorException> {
        public productErrorException build() {
            return new productErrorException(this);
        }
    }
}
