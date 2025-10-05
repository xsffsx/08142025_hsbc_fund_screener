package com.dummy.wpb.product.configuration;

import com.dummy.wpb.product.model.SystemDefaultValues;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.dummy.wpb.product.constant.BatchConstants.SEPARATOR_OF_PROD_TYPE_AND_MARKET;

public class SystemDefaultValuesHolder {

    private List<SystemDefaultValues> systemDefaultValues;

    public void setSystemDefaultValues(List<SystemDefaultValues> systemDefaultValues) {
        this.systemDefaultValues = systemDefaultValues;
    }


    public Map<String, Object> getDefaultValues(String systemCde, String typeCde) {
        Map<String, Object> defaultValues = new LinkedHashMap<>();

        String prodTypeCde = typeCde.split(SEPARATOR_OF_PROD_TYPE_AND_MARKET)[0];
        systemDefaultValues.stream()
                .filter(item -> Objects.equals(item.getSystemCde(), systemCde)
                        && (null == item.getTypeCde() || Objects.equals(item.getTypeCde(), prodTypeCde)))
                .findFirst()
                .ifPresent(item -> defaultValues.putAll(item.getDefaultValues()));

        if (typeCde.contains(SEPARATOR_OF_PROD_TYPE_AND_MARKET)) {
            systemDefaultValues.stream()
                    .filter(item -> Objects.equals(item.getSystemCde(), systemCde)
                            && Objects.equals(item.getTypeCde(), typeCde))
                    .findFirst()
                    .ifPresent(item -> defaultValues.putAll(item.getDefaultValues()));
        }
        return defaultValues;
    }

    public Map<String, Object> getDefaultValues(String systemCde) {
        Map<String, Object> defaultValues = new HashMap<>();
        systemDefaultValues.stream()
                .filter(item -> StringUtils.equals(item.getSystemCde(), systemCde))
                .forEach(item -> defaultValues.putAll(item.getDefaultValues()));
        return defaultValues;
    }

    public boolean isSupport(String systemCde) {
        return systemDefaultValues.stream().anyMatch(item -> StringUtils.equals(item.getSystemCde(), systemCde));
    }
}
