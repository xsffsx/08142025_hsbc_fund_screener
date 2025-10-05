/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_prototype.exception;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;

public class IllegalConfigurationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IllegalConfigurationException() {
        super(ExCodeConstant.EX_CODE_ILLEGAL_CONFIGURATION);
    }

    public IllegalConfigurationException(final Throwable e) {
        super(ExCodeConstant.EX_CODE_ILLEGAL_CONFIGURATION, e);
    }

    public IllegalConfigurationException(final String exCode) {
        super(exCode);
    }

    public IllegalConfigurationException(final String exCode, final Throwable e) {
        super(exCode, e);
    }

}
