/*
 *
 */
package com.hhhh.group.secwealth.mktdata.wmds_entitlement.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

public class EntitlementCacheKeyGenerator implements KeyGenerator {

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.cache.interceptor.KeyGenerator#generate(java.lang.
     * Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    public Object generate(final Object target, final Method method, final Object... params) {

        if (params == null || params.length == 0) {
            return SimpleKey.EMPTY;
        }

        String serviceName = null;
        List<String> keyList = new ArrayList<String>();

        if (params.length >= 3) {
            serviceName = String.valueOf(params[0]);
            keyList.add(serviceName);
            List<String> checklist = (List<String>) params[1];
            Map<String, String> headerMap = (Map<String, String>) params[2];
            List<String> list = getInputHeaderValue(checklist, headerMap);
            if (list != null && !list.isEmpty()) {
                keyList.addAll(list);
            }
        }
        return new SimpleKey(keyList.toArray());
    }

    private List<String> getInputHeaderValue(final List<String> checklist, final Map<String, String> headerMap) {
        List<String> inputHeaderValue = new ArrayList<String>();
        if (checklist != null) {
            for (String key : checklist) {
                inputHeaderValue.add(headerMap.get(key));
            }
        }
        return inputHeaderValue;
    }

}
