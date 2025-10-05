/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExResponseEntity {

    private String responseCode;

    private String reasonCode;

    private String text;

    private String status;

    public ExResponseEntity() {}

    public ExResponseEntity(final String responseCode, final String reasonCode, final String text, final String status) {
        this.responseCode = responseCode;
        this.reasonCode = reasonCode;
        this.text = text;
        this.status = status;
    }

}
