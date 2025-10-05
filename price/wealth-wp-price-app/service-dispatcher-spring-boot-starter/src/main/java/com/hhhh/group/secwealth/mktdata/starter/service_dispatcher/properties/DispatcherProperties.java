/*
 */
package com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.properties;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.bean.DispatcherConfigurationBean;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "dispatcher")
@Getter
@Setter
public class DispatcherProperties {

    private Map<String, DispatcherConfigurationBean> serviceApi = new HashMap<>();

    private DispatcherConfigurationBean getDispatchConfigurationBeanByServiceApi(final String serviceApi) {
        return this.serviceApi.get(serviceApi);
    }

    /**
     * <p>
     * <b> Get configuration request parameter list by service api. </b>
     * </p>
     *
     * @param serviceApi
     * @return
     */
    public List<String> getConfigRequestParameterListByServiceApi(final String serviceApi) {

        DispatcherConfigurationBean bean = getDispatchConfigurationBeanByServiceApi(serviceApi);
        List<String> requestList = null;
        if (bean != null) {
            requestList = bean.getRequestParameters();
        }
        return requestList;
    }

    /**
     * <p>
     * <b> Get configuration endpoint map by service api. </b>
     * </p>
     *
     * @param serviceApi
     * @return
     */
    public Map<String, String> getConfigEndpointMapByServiceApi(final String serviceApi) {

        DispatcherConfigurationBean bean = getDispatchConfigurationBeanByServiceApi(serviceApi);
        Map<String, String> endPointMap = null;
        if (bean != null) {
            endPointMap = bean.getEndPoint();
        }
        return endPointMap;
    }

    /**
     * <p>
     * <b> Get configuration dispatch rule map by service api. </b>
     * </p>
     *
     * @param serviceApi
     * @return
     */
    public LinkedHashMap<String, List<String>> getConfigDispatchRuleMapByServiceApi(final String serviceApi) {

        DispatcherConfigurationBean bean = getDispatchConfigurationBeanByServiceApi(serviceApi);
        LinkedHashMap<String, List<String>> ruleMap = null;
        if (bean != null) {
            ruleMap = bean.getRules();
        }
        return ruleMap;
    }

}