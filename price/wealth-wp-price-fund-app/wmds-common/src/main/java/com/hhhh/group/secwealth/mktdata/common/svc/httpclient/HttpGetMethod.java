/*
 */
package com.hhhh.group.secwealth.mktdata.common.svc.httpclient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;

/**
 * <p>
 * <b> Send method for get. </b>
 * </p>
 */
public class HttpGetMethod implements RequestMethod {

    public HttpUriRequest sendRequest(final String url, final String params) throws Exception {
        StringBuilder urlBuilder = new StringBuilder(url).append(CommonConstants.SYMBOL_INTERROGATION).append(params);
        HttpGet httpget = new HttpGet(urlBuilder.toString());
        return httpget;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.hhhh.group.secwealth.mktdata.common.svc.httpclient.RequestMethod#sendRequest(java.lang.String)
     */
    @Override
    public HttpUriRequest sendRequest(final String url) throws Exception {
        HttpGet httpget = new HttpGet(url);
        return httpget;
    }

}