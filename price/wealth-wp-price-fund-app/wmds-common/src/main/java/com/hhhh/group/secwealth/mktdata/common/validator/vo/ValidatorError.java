package com.hhhh.group.secwealth.mktdata.common.validator.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ValidatorError implements Serializable {

    private static final long serialVersionUID = -5364968019372402607L;

    private String reasonCode;
    private String field;
    private String text;
    private String validatorMethod;

    public ValidatorError() {}

    public ValidatorError(final String reasonCode, final String field, final String text, final String validatorMethod) {
        this.reasonCode = reasonCode;
        this.field = field;
        this.text = text;
        this.validatorMethod = validatorMethod;
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
     * @return the field
     */
    public String getField() {
        return this.field;
    }

    /**
     * @param field
     *            the field to set
     */
    public void setField(final String field) {
        this.field = field;
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
     * @return the validatorMethod
     */
    public String getValidatorMethod() {
        return this.validatorMethod;
    }

    /**
     * @param validatorMethod
     *            the validatorMethod to set
     */
    public void setValidatorMethod(final String validatorMethod) {
        this.validatorMethod = validatorMethod;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("reasonCode", this.reasonCode).append("field", this.field).append("text", this.text)
            .append("validatorMethod", this.validatorMethod).toString();
    }
}
