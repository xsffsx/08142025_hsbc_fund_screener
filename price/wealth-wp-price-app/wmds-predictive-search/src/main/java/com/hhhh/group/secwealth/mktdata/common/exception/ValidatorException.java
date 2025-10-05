package com.hhhh.group.secwealth.mktdata.common.exception;

import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

public class ValidatorException extends BaseException {
    /**
	 * 
	 */
    private static final long serialVersionUID = 351367917736948700L;

    /** The exception code. */
    private String exceptionCode;

    /** The err message. */
    private String errMessage;

    /** The err messages. */
    private List<ValidatorError> errMessages;

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ValidatorException)) {
            return false;
        }
        ValidatorException castOther = (ValidatorException) other;
        return new EqualsBuilder().append(this.exceptionCode, castOther.exceptionCode).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.exceptionCode).toHashCode();
    }

    @Override
    public int compareTo(final Object other) {
        ValidatorException castOther = (ValidatorException) other;
        return new CompareToBuilder().append(this.exceptionCode, castOther.exceptionCode)
            .append(this.errMessage, castOther.errMessage).append(this.errMessages, castOther.errMessages).toComparison();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("exceptionCode", this.exceptionCode)
            .append("errMessage", this.errMessage).toString();
    }

    /**
     * Instantiates a new Validator Exception.
     */
    public ValidatorException() {
        super();
    }

    public ValidatorException(final String exceptionCode) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
    }

    public ValidatorException(final String exceptionCode, final String message) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
        this.errMessage = message;
    }

    public ValidatorException(final String exceptionCode, final List<ValidatorError> errMessages) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
        this.errMessages = errMessages;
    }

    public ValidatorException(final String exceptionCode, final Exception e) {
        super(exceptionCode, e);
        this.exceptionCode = exceptionCode;
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

    /**
     * @return the errMessages
     */
    public List<ValidatorError> getErrMessages() {
        return this.errMessages;
    }

    /**
     * @param errMessages
     *            the errMessages to set
     */
    public void setErrMessages(final List<ValidatorError> errMessages) {
        this.errMessages = errMessages;
    }

}
