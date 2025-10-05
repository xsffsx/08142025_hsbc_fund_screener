/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_prototype.exception;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;

public class VendorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VendorException() {
        super(ExCodeConstant.EX_CODE_ACCESS_VENDOR_ERROR);
    }

    public VendorException(final Throwable e) {
        super(ExCodeConstant.EX_CODE_ACCESS_VENDOR_ERROR, e);
    }

    public VendorException(final String exCode) {
        super(exCode);
    }

    public VendorException(final String exCode, final Throwable e) {
        super(exCode, e);
    }

}
