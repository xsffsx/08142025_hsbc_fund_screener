package com.hhhh.group.secwealth.mktdata.common.svc.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder<T> {
    private HttpStatus httpStatus;
    private Response response;
    private List<Response> responses;

    public ResponseBuilder<T> httpStatus(final HttpStatus status) {
        this.httpStatus = status;
        return this;
    }

    public ResponseBuilder<T> response(final Response response) {
        this.response = response;
        return this;
    }

    public ResponseBuilder<T> responses(final List<Response> responses) {
        this.responses = responses;
        return this;
    }

    public ResponseEntity<?> build() {
        if (null != this.response) {
            return new ResponseEntity<Response>(this.response, this.httpStatus);
        } else if (null != this.responses) {
            return new ResponseEntity<List<Response>>(this.responses, this.httpStatus);
        } else {
            return new ResponseEntity<Object>(this.httpStatus);
        }
    }

    public ResponseEntity<?> buildObject(final Object object) {
        return new ResponseEntity<>(object, this.httpStatus);
    }
}
