package com.dummy.wpb.product.model.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

@Data
@Slf4j
public class LegacyConfigInfo {
    private String name;
    private String forEntity;
    private String description;
    private List<LegacyConfigItem> config;

    /**
     * Convert a list like below into a map
     * [
     * {
     * "key" : "SID.Default.CustClass",
     * "value" : "PFS"
     * },
     * {
     * "key" : "DPS.defaultFinDocCustClass",
     * "value" : "PFS"
     * }
     * ]
     *
     * @return
     */
    public Properties getProperties() {
        Properties props = new Properties();
        config.forEach(item -> {
            if (null == item.getValue()) {
                // in a properties file, "a=" result in a="", not a=null
                props.put(item.getKey(), "");
            } else {
                // for Properties, both key and value are String
                if (item.getValue() instanceof LinkedHashMap || item.getValue() instanceof List) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        props.put(item.getKey(), mapper.writeValueAsString(item.getValue()));
                    } catch (JsonProcessingException e) {
                        String errorMsg = String.format("encounter JsonProcessingException: %s", e.getMessage());
                        log.error(errorMsg, e);
                    }
                } else {
                    props.put(item.getKey(), item.getValue().toString());
                }
            }
        });
        return props;
    }
}
