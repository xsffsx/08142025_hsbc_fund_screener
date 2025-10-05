/*
 */
package com.hhhh.group.secwealth.mktdata.common.dao.impl;

import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;


/*
 * Not putting impl suffix since this is special case and used to store
 * multiple entitymanager to support multi entity.
 */
public class CustomerContextHolder {

    private static ThreadLocal<Map<String, String>> contextHolder = new ThreadLocal<Map<String, String>>();

    public static void setHeaderMap(final Map<String, String> headerMap) {
        CustomerContextHolder.contextHolder.set(headerMap);
    }

    public static Map<String, String> getHeaderMap() {
        return CustomerContextHolder.contextHolder.get();
    }

    public static String getSiteKeyFromHeaderMap() {
        String siteKey = null;
        Map<String, String> headerMap = CustomerContextHolder.contextHolder.get();
        if (headerMap != null) {
            siteKey = headerMap.get(CommonConstants.REQUEST_HEADER_COUNTRYCODE) + CommonConstants.SYMBOL_UNDERLINE
                + headerMap.get(CommonConstants.REQUEST_HEADER_GROUPMEMBER);
        }
        return siteKey;
    }

    public static String getAppCodeFromHeaderMap() {
        String appCode = null;
        Map<String, String> headerMap = CustomerContextHolder.contextHolder.get();
        if (headerMap != null) {
            appCode = headerMap.get(CommonConstants.REQUEST_HEADER_APPCODE);
        }
        return appCode;
    }

    public static void remove() {
        CustomerContextHolder.contextHolder.remove();
    }
}
