/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseUtils<T> {

    private ResponseUtils() {}

    public static <T> ResponseBuilder<T> newResponse() {
        return new ResponseBuilder<T>();
    }

    public static <T> ResponseEntity<T> success(final T response) {
        return ResponseUtils.<T> newResponse().httpStatus(HttpStatus.OK).response(response).build();
    }

    public static <T> ResponseEntity<T> failure(final HttpStatus httpStatus, final T response) {
        return ResponseUtils.<T> newResponse().httpStatus(httpStatus).response(response).build();
    }

}
