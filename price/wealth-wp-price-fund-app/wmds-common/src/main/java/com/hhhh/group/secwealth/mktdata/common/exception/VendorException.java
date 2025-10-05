/*
 */
package com.hhhh.group.secwealth.mktdata.common.exception;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The Class VendorException.
 */

public class VendorException extends BaseException {
    private static final long serialVersionUID = 2512598511201210949L;

    /** Error response. */
    private ErrResponse errResponse;

    /**
     * Instantiates a new vendor exception.
     */
    public VendorException() {
        super();
    }

    public VendorException(final String msg, final String responseCode, final String reasonCode, final String text) {
        super(msg);
        this.errResponse = new ErrResponse(responseCode, reasonCode, text);
    }

    public ErrResponse getErrResponse() {
        return this.errResponse;
    }

    public void setErrResponse(final ErrResponse errResponse) {
        this.errResponse = errResponse;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof VendorException)) {
            return false;
        }
        VendorException castOther = (VendorException) other;
        return new EqualsBuilder().append(this.errResponse.getReasonCode(), castOther.getErrResponse().getReasonCode()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.errResponse.getReasonCode()).toHashCode();
    }

    @Override
    public int compareTo(final Object other) {
        VendorException castOther = (VendorException) other;
        return new CompareToBuilder().append(this.errResponse, castOther.errResponse).toComparison();
    }

}
