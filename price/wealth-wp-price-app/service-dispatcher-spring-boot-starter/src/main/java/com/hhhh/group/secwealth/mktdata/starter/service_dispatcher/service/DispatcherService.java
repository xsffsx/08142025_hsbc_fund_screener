/*
 */
package com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;

import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.bean.InputParametersInvoker;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.constant.Constant;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.properties.DispatcherProperties;

import lombok.Setter;


public class DispatcherService {

    @Setter
    private DispatcherProperties prop;

    @Setter
    private InputParametersInvoker invoker;

    /**
     * <p>
     * <b> Get service id by matching configuration rule.</b>
     * </p>
     *
     * @param serviceApi
     * @param header
     * @param request
     * @return serviceId
     */
    @Cacheable(cacheNames = "dispatcherCache", keyGenerator = "dispatcherCacheKeyGenerator", unless = "#result == null")
    public String getServiceId(final String serviceApi, final Object header, final Object request) {

        Map<String, String> configEndPointMap = this.prop.getConfigEndpointMapByServiceApi(serviceApi);
        String defaultServiceId = configEndPointMap.get(Constant.DISPATCHER_ENDPOINT_BY_DEFAULT);

        List<String> configParamslist = this.prop.getConfigRequestParameterListByServiceApi(serviceApi);
        LinkedHashMap<String, List<String>> configRuleMap = this.prop.getConfigDispatchRuleMapByServiceApi(serviceApi);

        if (isUndefinedConfigurationRule(configParamslist, configRuleMap)) {
            return defaultServiceId;
        }

        Map<String, String> clientParamsMap = this.invoker.getClientInputParamsMap(configParamslist, header, request);
        String targetServiceId = matchTargetServiceId(configRuleMap, configEndPointMap, clientParamsMap);

        if (StringUtils.isEmpty(targetServiceId)) {
            targetServiceId = defaultServiceId;
        }

        return targetServiceId;
    }

    /**
     * <p>
     * <b> Match target service id based on dispatcher configuration rule. </b>
     * </p>
     *
     * @param ruleMap
     * @param clientParamsMap
     * @param endPointMap
     * @return targetServiceId
     */
    private String matchTargetServiceId(final LinkedHashMap<String, List<String>> ruleMap, final Map<String, String> endPointMap,
        final Map<String, String> clientParamsMap) {

        Iterator<String> it = ruleMap.keySet().iterator();
        String clientParamValue = null;
        while (it.hasNext()) {
            String ruleKey = (String) it.next();
            if (ruleKey.indexOf(Constant.SYMBOL_UNDERLINE) == -1) {
                clientParamValue = clientParamsMap.get(ruleKey);
            } else {
                clientParamValue = ruleKey;
                String[] keyArray = ruleKey.split(Constant.SYMBOL_UNDERLINE);
                for (int index = 0; index < keyArray.length; index++) {
                    String tempStr = clientParamsMap.get(keyArray[index]);
                    if (tempStr != null) {
                        clientParamValue = clientParamValue.replace(keyArray[index], tempStr);
                    }
                }
            }

            String endPointValue = endPointMap.get(clientParamValue);
            if (StringUtils.isNotEmpty(endPointValue)) {
                return endPointValue;
            }
        }
        return null;
    }

    /**
     * <p>
     * <b> Check the rlue and request parameters is undefined or not. </b>
     * </p>
     *
     * @param configParamslist
     * @param configRuleMap
     * @return true: undefined; false: defined
     */
    private boolean isUndefinedConfigurationRule(final List<String> configParamslist,
        final Map<String, List<String>> configRuleMap) {

        return configParamslist == null || configParamslist.isEmpty() || configRuleMap == null || configRuleMap.isEmpty();
    }
}
