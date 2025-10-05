/*
 */
package com.hhhh.group.secwealth.mktdata.common.exception;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The Class CommonException.
 */
public class CommonException extends BaseException {
    private static final long serialVersionUID = 3521397434037375607L;

    /** The exception code. */
    private String exceptionCode;

    /** The err message. */
    private String errMessage;

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof CommonException)) {
            return false;
        }
        CommonException castOther = (CommonException) other;
        return new EqualsBuilder().append(this.exceptionCode, castOther.exceptionCode).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.exceptionCode).toHashCode();
    }

    @Override
    public int compareTo(final Object other) {
        CommonException castOther = (CommonException) other;
        return new CompareToBuilder().append(this.exceptionCode, castOther.exceptionCode)
            .append(this.errMessage, castOther.errMessage).toComparison();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("exceptionCode", this.exceptionCode)
            .append("errMessage", this.errMessage).toString();
    }

    /**
     * Instantiates a new common exception.
     */
    public CommonException() {
        super();
    }

    public CommonException(final String exceptionCode) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
    }

    public CommonException(final String exceptionCode, final String message) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
        this.errMessage = message;
    }

    /**
     * Gets the exception code.
     * 
     * @return the exception code
     */
    public String getExceptionCode() {
        return this.exceptionCode;
    }

    /**
     * Sets the exception code.
     * 
     * @param exceptionCode
     *            the new exception code
     */
    public void setExceptionCode(final String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    /**
     * Gets the err message.
     * 
     * @return the err message
     */
    public String getErrMessage() {
        return this.errMessage;
    }

    /**
     * Sets the err message.
     * 
     * @param errMessage
     *            the new err message
     */
    public void setErrMessage(final String errMessage) {
        this.errMessage = errMessage;
    }
}
