/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_prototype.exception;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApplicationException() {
        super(ExCodeConstant.EX_CODE_UNDEFINED);
    }

    public ApplicationException(final Throwable e) {
        super(ExCodeConstant.EX_CODE_UNDEFINED, e);
    }

    public ApplicationException(final String exCode) {
        super(exCode);
    }

    public ApplicationException(final String exCode, final Throwable e) {
        super(exCode, e);
    }

}
