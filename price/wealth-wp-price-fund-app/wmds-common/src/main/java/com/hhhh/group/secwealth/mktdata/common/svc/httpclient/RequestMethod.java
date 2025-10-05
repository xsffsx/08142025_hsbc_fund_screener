/*
 */
package com.hhhh.group.secwealth.mktdata.common.svc.httpclient;

import org.apache.http.client.methods.HttpUriRequest;

/**
 * <p>
 * <b> interface RequestMethod. </b>
 * </p>
 */
public interface RequestMethod {

    public HttpUriRequest sendRequest(final String url, final String params) throws Exception;

    public HttpUriRequest sendRequest(final String url) throws Exception;
}
