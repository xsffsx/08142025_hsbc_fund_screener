/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_entitlement.properties;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "entitlement")
public class EntitlementProperties {

    @Value("${entitlement.enable}")
    private boolean entitlementEnabled;

    @Value("${entitlement.interceptor-url}")
    private String interceptorUrl;

    private LinkedHashMap<String, String> serviceUrlMapping = new LinkedHashMap<String, String>();

    private Map<String, EntitlementConfigurationBean> serviceApi = new HashMap<>();

    public EntitlementConfigurationBean getEntitlementConfigurationBeanByServerName(final String serviceName) {

        return this.serviceApi.get(serviceName);
    }

    /**
     * <p>
     * <b>Get checklist by service name. </b>
     * </p>
     *
     * @param serviceName
     * @return
     */
    public List<String> getChecklistByServiceName(final String serviceName) {

        EntitlementConfigurationBean bean = getEntitlementConfigurationBeanByServerName(serviceName);
        if (bean != null) {
            return bean.getChecklist();
        }
        return null;
    }

    /**
     * <p>
     * <b> Get rules by service name. </b>
     * </p>
     *
     * @param serviceName
     * @return
     */
    public LinkedHashMap<String, List<String>> getWhiteListRulesByServiceName(final String serviceName) {

        EntitlementConfigurationBean bean = getEntitlementConfigurationBeanByServerName(serviceName);
        if (bean != null) {
            return bean.getWhiteListRules();
        }
        return null;
    }
}
