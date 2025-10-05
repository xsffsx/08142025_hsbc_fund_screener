/*
 */
package com.hhhh.group.secwealth.mktdata.common.svc.httpclient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;

/**
 * <p>
 * <b> Send method for post. </b>
 * </p>
 */
public class HttpPostMethod implements RequestMethod {

    public HttpUriRequest sendRequest(final String url, final String params) throws Exception {
        StringEntity entity = new StringEntity(params, URLEncodedUtils.CONTENT_TYPE, CommonConstants.CODING_UTF8);
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);
        return httppost;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hhhh.group.secwealth.mktdata.common.svc.httpclient.RequestMethod#sendRequest(java.lang.String)
     */
    @Override
    public HttpUriRequest sendRequest(final String url) throws Exception {

        return null;
    }

}