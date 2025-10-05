/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_prototype.exception;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;

public class IllegalInitializationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IllegalInitializationException() {
        super(ExCodeConstant.EX_CODE_ILLEGAL_INITIALIZATION);
    }

    public IllegalInitializationException(final Throwable e) {
        super(ExCodeConstant.EX_CODE_ILLEGAL_INITIALIZATION, e);
    }

    public IllegalInitializationException(final String exCode) {
        super(exCode);
    }

    public IllegalInitializationException(final String exCode, final Throwable e) {
        super(exCode, e);
    }

}
