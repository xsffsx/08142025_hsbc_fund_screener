/*
 */
package com.hhhh.group.secwealth.mktdata.common.svc.response;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hhhh.group.secwealth.mktdata.common.exception.ErrResponse;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * <p>
 * <b> error message Response bean. </b>
 * </p>
 */
@JsonInclude(Include.NON_NULL)
public class Response {

    /**
     * error detail
     */
    protected String text = null;

    protected List<ValidatorError> texts = null;

    // Denote warning or error
    protected String responseCode = null;
    // Denote the detail
    protected String reasonCode = null;
    // The unique id for per F/E request
    protected String traceCode = null;

    public Response() {}

    public Response(final String responseCode, final String reasonCode, final String traceCode, final String text) {
        this.responseCode = responseCode;
        this.reasonCode = reasonCode;
        this.traceCode = traceCode;
        this.text = text;
    }

    public Response(final String responseCode, final String reasonCode, final String traceCode, final List<ValidatorError> texts) {
        this.responseCode = responseCode;
        this.reasonCode = reasonCode;
        this.traceCode = traceCode;
        this.texts = texts;
    }

    public Response(final ErrResponse errResponse, final String traceCode) {
        this.responseCode = errResponse.getResponseCode();
        this.reasonCode = errResponse.getReasonCode();
        this.traceCode = traceCode;
        this.text = errResponse.getText();
    }

    public Response(final ErrResponse errResponse, final String traceCode, final List<ValidatorError> texts) {
        this.responseCode = errResponse.getResponseCode();
        this.reasonCode = errResponse.getReasonCode();
        this.traceCode = traceCode;
        this.texts = texts;

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("responseCode", this.responseCode).append("reasonCode", this.reasonCode).append("traceCode", this.traceCode)
            .toString();
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * @return the texts
     */
    public List<ValidatorError> getTexts() {
        return this.texts;
    }

    /**
     * @param texts
     *            the texts to set
     */
    public void setTexts(final List<ValidatorError> texts) {
        this.texts = texts;
    }

    /**
     * @return the responseCode
     */
    public String getResponseCode() {
        return this.responseCode;
    }

    /**
     * @param responseCode
     *            the responseCode to set
     */
    public void setResponseCode(final String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the reasonCode
     */
    public String getReasonCode() {
        return this.reasonCode;
    }

    /**
     * @param reasonCode
     *            the reasonCode to set
     */
    public void setReasonCode(final String reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * @return the traceCode
     */
    public String getTraceCode() {
        return this.traceCode;
    }

    /**
     * @param traceCode
     *            the traceCode to set
     */
    public void setTraceCode(final String traceCode) {
        this.traceCode = traceCode;
    }

}
