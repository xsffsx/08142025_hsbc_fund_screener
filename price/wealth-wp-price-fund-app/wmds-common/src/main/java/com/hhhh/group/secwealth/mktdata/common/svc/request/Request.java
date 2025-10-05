/*
 */
package com.hhhh.group.secwealth.mktdata.common.svc.request;

import java.util.HashMap;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;

/**
 * <p>
 * <b> Base Request </b>
 * </p>
 */
public abstract class Request {

    private Map<String, String> headers = new HashMap<String, String>();

    public void addHeader(final String name, final String value) {
        this.headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void putAllHeaders(final Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            this.headers.putAll(headers);
        }
    }

    public String getSiteKey() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_COUNTRYCODE) + CommonConstants.SYMBOL_UNDERLINE
            + this.headers.get(CommonConstants.REQUEST_HEADER_GROUPMEMBER);
    }

    public String getLocale() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_LOCALE);
    }


    public String getCountryCode() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_COUNTRYCODE);
    }

    public String getGroupMember() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_GROUPMEMBER);
    }

    public String getChannelId() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_CHANNELID);
    }


    public String getAppCode() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_APPCODE);
    }

    public String getLineOfBusiness() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_LINEOFBUSINESS);
    }

    public String getE2ETrustWealthSaml() {
        return this.headers.get(CommonConstants.E2ETRUST_HEADER_WEALTHSAML);
    }

    public String getE2ETrustSaml() {
        return this.headers.get(CommonConstants.E2ETRUST_HEADER_SAML);
    }
}
