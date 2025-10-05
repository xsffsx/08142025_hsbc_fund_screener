package com.hhhh.group.secwealth.mktdata.wmds_prototype.exception;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;

public class ParameterException extends RuntimeException {

    private static final long serialVersionUID = 5645860225137027465L;

    public ParameterException() {
        super(ExCodeConstant.EX_CODE_REQUEST_PARAMETER_ERROR);
    }

    public ParameterException(final Throwable e) {
        super(ExCodeConstant.EX_CODE_REQUEST_PARAMETER_ERROR, e);
    }

    public ParameterException(final String exCode) {
        super(exCode);
    }

    public ParameterException(final String exCode, final Throwable e) {
        super(exCode, e);
    }
}
