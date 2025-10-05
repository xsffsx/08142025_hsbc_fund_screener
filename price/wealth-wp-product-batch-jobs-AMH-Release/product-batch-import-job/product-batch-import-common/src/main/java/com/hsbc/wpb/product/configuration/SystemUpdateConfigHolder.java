package com.dummy.wpb.product.configuration;

import com.dummy.wpb.product.model.SystemUpdateConfig;
import com.dummy.wpb.product.model.SystemUpdateConfig.UpdateConfig;

import java.util.*;

import static com.dummy.wpb.product.constant.BatchConstants.SEPARATOR_OF_PROD_TYPE_AND_MARKET;

public class SystemUpdateConfigHolder {

    private final Map<String, SystemUpdateConfig> systemUpdateConfigMap = new HashMap<>();

    public void setSystemUpdateConfig(List<SystemUpdateConfig> systemUpdateConfigList) {
        systemUpdateConfigList.forEach(item -> systemUpdateConfigMap.put(item.getSystemCde(), item));
    }

    public List<String> getUpdateAttrs(String systemCde, String collection) {
        SystemUpdateConfig systemUpdateConfig = systemUpdateConfigMap.get(systemCde);
        if (null == systemUpdateConfig) {
            return Collections.emptyList();
        }

        UpdateConfig updateConfig = systemUpdateConfig.getConfig()
                .stream()
                .filter(config -> config.getCollection().equals(collection))
                .findFirst()
                .orElse(null);

        if (null == updateConfig) {
            return Collections.emptyList();
        }

        return new ArrayList<>(updateConfig.getUpdateAttrs());
    }

    /**
     * Find update config by:
     * 1. If the typeCde does not contains "~", then find update config by systemCde. <br/>
     * Like: AMHCUTAS, AMHGSOPS <br/>
     * <br/>
     * 2.the typeCde contains "~", then find config by systemCde and prodTypeCde and marketCde<br/>
     * Like: AMHGSOPS.AS.SEC~HK, AMHGSOPS.AS.SEC~CN, AMHGSOPS.AS.SEC~US
     */
    public List<String> getUpdateAttrs(String systemCde, String typeCde, String collection) {
        SystemUpdateConfig systemUpdateConfig = systemUpdateConfigMap.get(systemCde);
        if (null == systemUpdateConfig) {
            return Collections.emptyList();
        }


        UpdateConfig updateConfig = systemUpdateConfig.getConfig()
                .stream()
                .filter(config ->
                        Objects.equals(config.getCollection(), collection) &&
                                (!typeCde.contains(SEPARATOR_OF_PROD_TYPE_AND_MARKET) || Objects.equals(config.getTypeCde(), typeCde))
                )
                .findFirst()
                .orElse(null);

        if (null == updateConfig) {
            return Collections.emptyList();
        }

        return new ArrayList<>(updateConfig.getUpdateAttrs());
    }

    public boolean isSupport(String systemCde) {
        return systemUpdateConfigMap.containsKey(systemCde);
    }
}
