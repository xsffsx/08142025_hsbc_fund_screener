package com.hhhh.group.secwealth.mktdata.wmds_prototype.exception;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;

public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public SystemException() {
        super(ExCodeConstant.EX_CODE_SYSTEM_ERROR);
    }

    public SystemException(final Throwable e) {
        super(ExCodeConstant.EX_CODE_SYSTEM_ERROR, e);
    }

    public SystemException(final String exCode) {
        super(exCode);
    }

    public SystemException(final String exCode, final Throwable e) {
        super(exCode, e);
    }

}
