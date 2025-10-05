/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidResult {

    private String annotationName;

    private String message;

    public InvalidResult(final String annotationName, final String message) {
        this.annotationName = annotationName;
        this.message = message;
    }

}
