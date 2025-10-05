/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_prototype.exception;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;

/**
 * <p>
 * <b> Common Exception</b>
 * </p>
 */
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CommonException() {
        super(ExCodeConstant.EX_CODE_UNDEFINED);
    }

    public CommonException(final Throwable e) {
        super(ExCodeConstant.EX_CODE_UNDEFINED, e);
    }

    public CommonException(final String exCode) {
        super(exCode);
    }

    public CommonException(final String exCode, final Throwable e) {
        super(exCode, e);
    }
}
