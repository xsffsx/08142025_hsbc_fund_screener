package com.dummy.wmd.wpc.graphql.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.CustEligConfig;
import com.dummy.wmd.wpc.graphql.service.ConfigurationService;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.dummy.wmd.wpc.graphql.utils.ObjectMapperUtils.convertValue;

@Configuration
public class DefaultFieldConfig {
    private static final String CONF_ID = "ALL/wps-main-config";
    private Map<String, CustEligConfig> custEligByType;

    private final ConfigurationService configurationService;

    public DefaultFieldConfig(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @PostConstruct
    public void init() {
        Bson filter = Filters.eq(Field._id, CONF_ID);
        List<Document> ctrySpecificConfigs = configurationService.getConfigurationsByFilter(filter);
        if (ctrySpecificConfigs.isEmpty()) {
            custEligByType = Collections.emptyMap();
            return;
        }
        Document ctrySpecificConfig = ctrySpecificConfigs.get(0);

        Document empty = new Document();
        Document defaultCustElig = ctrySpecificConfig.get("config", empty)
                                                     .get("defaultConfig", empty)
                                                     .get("custEligByType", empty);
        custEligByType = defaultCustElig.isEmpty() ? Collections.emptyMap() :
                convertValue(defaultCustElig, new TypeReference<Map<String, CustEligConfig>>() {});
    }


    public CustEligConfig custElig(String prodTypeCde) {
        return custEligByType.get(prodTypeCde);
    }

    public void setCustEligByType(Map<String, CustEligConfig> custEligByType) {
        this.custEligByType = custEligByType;
    }
}
