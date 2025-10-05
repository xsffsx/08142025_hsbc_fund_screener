/*
 */
package com.hhhh.group.secwealth.mktdata.common.exception;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * <p>
 * <b> ErrResponseHolder. </b>
 * </p>
 */
public class ErrResponseHolder {

    private Map<String, ErrResponse> errResponses = new ConcurrentHashMap<String, ErrResponse>();

    public Map<String, ErrResponse> getErrResponses() {
        return this.errResponses;
    }

    public void setErrResponses(final Map<String, ErrResponse> errResponses) {
        this.errResponses = errResponses;
    }
}
