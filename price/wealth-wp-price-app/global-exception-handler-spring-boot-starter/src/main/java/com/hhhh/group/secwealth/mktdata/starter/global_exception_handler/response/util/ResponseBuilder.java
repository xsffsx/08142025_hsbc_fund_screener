/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder<T> {

    private HttpStatus status;
    private T responseBody;

    public ResponseBuilder<T> httpStatus(final HttpStatus status) {
        this.status = status;
        return this;
    }

    public ResponseBuilder<T> response(final T responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public ResponseEntity<T> build() {
        return new ResponseEntity<T>(this.responseBody, this.status);
    }

}
