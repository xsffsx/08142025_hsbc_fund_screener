/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response;

import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class ExResponse {

    private String responseCode;
    private String reasonCode;
    private String text;
    private String traceCode;
    @JsonIgnore
    private String exception;
    @JsonIgnore
    private String exCode;

    public ExResponse(final ExResponseEntity exResponse, final String traceCode, final String exception, final String exCode) {
        this.responseCode = exResponse.getResponseCode();
        this.reasonCode = exResponse.getReasonCode();
        this.text = exResponse.getText();
        this.traceCode = traceCode;
        this.exception = exception;
        this.exCode = exCode;
    }

}
