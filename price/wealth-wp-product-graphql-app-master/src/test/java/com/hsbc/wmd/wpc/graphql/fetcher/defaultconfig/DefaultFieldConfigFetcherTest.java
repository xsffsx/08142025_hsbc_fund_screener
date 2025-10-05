package com.dummy.wmd.wpc.graphql.fetcher.defaultconfig;

import com.dummy.wmd.wpc.graphql.config.DefaultFieldConfig;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.ProdTypeCde;
import com.dummy.wmd.wpc.graphql.model.CustEligConfig;
import com.dummy.wmd.wpc.graphql.service.ConfigurationService;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class DefaultFieldConfigFetcherTest {

    private DefaultFieldConfigFetcher defaultFieldConfigFetcher;
    private final DataFetchingEnvironment environment = Mockito.mock(DataFetchingEnvironment.class);
    private final ConfigurationService configurationService = Mockito.mock(ConfigurationService.class);

    @Before
    public void before() {
        DefaultFieldConfig defaultConfig = new DefaultFieldConfig(configurationService);
        defaultConfig.setCustEligByType(initCustEligConfig());
        defaultFieldConfigFetcher = new DefaultFieldConfigFetcher(defaultConfig);
    }

    private Map<String, CustEligConfig> initCustEligConfig() {
        Map<String, CustEligConfig> custEligConfigMap = new HashMap<>();
        CustEligConfig custEligConfig = new CustEligConfig();
        custEligConfig.setRestrCustCtry(Collections.singletonList(new HashMap<>()));
        custEligConfig.setRestrCustGroup(Collections.singletonList(new HashMap<>()));
        custEligConfig.setFormReqmt(Collections.singletonList(new HashMap<>()));
        custEligConfigMap.put(ProdTypeCde.BOND_CD, custEligConfig);
        return custEligConfigMap;
    }

    @Test
    public void test_get() throws Exception {
        when(environment.getArgument(Field.docType)).thenReturn(DocType.product.name());
        Document doc = defaultFieldConfigFetcher.get(environment);
        Assert.assertTrue(doc.isEmpty());

        when(environment.getArgument(Field.docType)).thenReturn(DocType.product_customer_eligibility.name());
        when(environment.getArgument("filter")).thenReturn(Collections.singletonMap("prodTypeCde", "ELI"));
        doc = defaultFieldConfigFetcher.get(environment);
        Assert.assertTrue(doc.isEmpty());

        when(environment.getArgument("filter")).thenReturn(Collections.singletonMap("prodTypeCde", "BOND"));
        doc = defaultFieldConfigFetcher.get(environment);
        Assert.assertFalse(doc.isEmpty());
    }
}