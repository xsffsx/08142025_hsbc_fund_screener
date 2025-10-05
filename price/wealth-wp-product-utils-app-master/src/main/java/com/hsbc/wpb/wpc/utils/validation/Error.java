package com.dummy.wpb.wpc.utils.validation;

import lombok.Data;

@Data
public class Error {
    private String subject;
    private String message;
    private Object data;

    public Error(String subject, String message) {
        this.subject = subject;
        this.message = message;
    }

    public Error(String subject, String message, Object data) {
        this.subject = subject;
        this.message = message;
        this.data = data;
    }
}
