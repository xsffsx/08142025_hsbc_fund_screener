/*
 */
package com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.bean.InputParametersInvoker;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.properties.DispatcherProperties;

import lombok.Setter;

public class DispatcherCacheKeyGenerator implements KeyGenerator {

    @Setter
    private InputParametersInvoker invoker;

    @Setter
    private DispatcherProperties prop;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.cache.interceptor.KeyGenerator#generate(java.lang.
     * Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object generate(final Object target, final Method method, final Object... params) {

        if (params == null || params.length == 0) {
            return SimpleKey.EMPTY;
        }

        String serviceApi = null;
        Object header = null;
        Object request = null;

        if (params.length >= 3) {
            serviceApi = String.valueOf(params[0]);
            header = params[1];
            request = params[2];
        }
        List<String> keyList = new ArrayList<>();
        keyList.add(serviceApi);
        List<String> list = getClientInputParamsValues(serviceApi, header, request);
        if (list != null && !list.isEmpty()) {
            keyList.addAll(list);
        }

        return new SimpleKey(keyList.toArray());
    }

    private List<String> getClientInputParamsValues(final String serviceApi, final Object header, final Object request) {

        List<String> configParamsList = this.prop.getConfigRequestParameterListByServiceApi(serviceApi);
        return this.invoker.getClientInputParamsList(configParamsList, header, request);
    }
}