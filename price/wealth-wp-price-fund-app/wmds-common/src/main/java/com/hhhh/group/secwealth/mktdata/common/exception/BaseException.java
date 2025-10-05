/*
 */
package com.hhhh.group.secwealth.mktdata.common.exception;

import java.math.BigInteger;
import java.util.Random;

@SuppressWarnings({"serial", "rawtypes"})
public abstract class BaseException extends RuntimeException implements Comparable {
    private String traceCode;

    public BaseException() {
        super();
        this.traceCode = getErrTraceCode();
    }

    public BaseException(final String message) {
        super(message);
        this.traceCode = getErrTraceCode();
    }

    public BaseException(final String message, final Throwable cause) {
        super(message, cause);
        this.traceCode = getErrTraceCode();
    }

    public BaseException(final Throwable cause) {
        super(cause);
        this.traceCode = getErrTraceCode();
    }

    public static String getErrTraceCode() {
        Random random = new Random();
        byte[] randomData = new byte[8];
        random.nextBytes(randomData);
        return new BigInteger(1, randomData).toString();
    }

    /**
     * @return the traceCode
     */
    public String getTraceCode() {
        return this.traceCode;
    }

    /**
     * @param traceCode
     *            the traceCode to set
     */
    public void setTraceCode(final String traceCode) {
        this.traceCode = traceCode;
    }

}
