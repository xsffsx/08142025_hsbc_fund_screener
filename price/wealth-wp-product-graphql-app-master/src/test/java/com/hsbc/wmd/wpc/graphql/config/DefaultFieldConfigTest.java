package com.dummy.wmd.wpc.graphql.config;

import com.dummy.wmd.wpc.graphql.constant.ProdTypeCde;
import com.dummy.wmd.wpc.graphql.service.ConfigurationService;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DefaultFieldConfigTest {

    private final ConfigurationService configurationService = Mockito.mock(ConfigurationService.class);
    private final DefaultFieldConfig defaultConfig = new DefaultFieldConfig(configurationService);

    @Test
    public void test_init() {
        when(configurationService.getConfigurationsByFilter(any(Bson.class))).thenReturn(Collections.emptyList());
        defaultConfig.init();
        Assert.assertNull(defaultConfig.custElig(ProdTypeCde.BOND_CD));

        Document document = new Document();
        when(configurationService.getConfigurationsByFilter(any(Bson.class)))
                .thenReturn(Collections.singletonList(document));
        defaultConfig.init();
        Assert.assertNull(defaultConfig.custElig(ProdTypeCde.BOND_CD));

        document.put("config", new Document());
        document.get("config", Document.class).put("defaultConfig", new Document());
        document.get("config", Document.class)
                .get("defaultConfig", Document.class)
                .put("custEligByType", new Document());
        document.get("config", Document.class).get("defaultConfig", Document.class)
                .get("custEligByType", Document.class).put(ProdTypeCde.BOND_CD, new Document());
        defaultConfig.init();
        Assert.assertTrue(defaultConfig.custElig(ProdTypeCde.BOND_CD).getRestrCustCtry().isEmpty());
    }
}