package com.hhhh.group.secwealth.mktdata.predsrch.svc.service;


import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.Product;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.ProductEntities;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.manager.PredictiveSearchManager;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl.PredictiveSearchServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class PredictiveSearchServiceTest {
    @Mock
    private PredictiveSearchManager predictiveSearchManager;

    @Mock
    private ProductEntities productEntities;

    @Mock
    private SiteFeature siteFeature;

    @InjectMocks
    private PredictiveSearchServiceImpl predictiveSearchService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoService() throws Exception {
        PredSrchRequest predSrchRequest = new PredSrchRequest();
        predSrchRequest.addHeader("x-hhhh-locale", "x-hhhh-locale");
        predSrchRequest.addHeader("x-hhhh-chnl-countrycode", "x-hhhh-chnl-countrycode");
        predSrchRequest.addHeader("x-hhhh-chnl-group-member", "x-hhhh-chnl-group-member");
        predSrchRequest.setKeyword("ChannelRestrictCode");
        predSrchRequest.setAssetClasses(new String[]{"SEC", "WRTS"});
        predSrchRequest.setChannelRestrictCode("ChannelRestrictCode");
        predSrchRequest.setRestrOnlScribInd("RestrOnlScribInd");
        predSrchRequest.setMarket("Market");
        predSrchRequest.setFilterCriterias(new ArrayList<SearchCriteria>() {{
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setCriteriaKey("switchOutFund");
            searchCriteria.setOperator("o");
            searchCriteria.setCriteriaValue("switchOutFund:switchOutFund:switchOutFund:switchOutFund:switchOutFund");
            add(searchCriteria);
        }});

        when(predictiveSearchManager.findProductListByKey("symbol_analyzed:switchoutfund AND countryTradableCode_analyzed:switchOutFund AND productType_analyzed:switchOutFund", null, "x-hhhh-chnl-countrycode_x-hhhh-chnl-group-member", 1)).thenReturn(new ArrayList<SearchableObject>() {{
            SearchableObject searchableObject = new SearchableObject();
            searchableObject.setProductName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
            searchableObject.setProductShortName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
            searchableObject.setSwitchableGroup_analyzed(new String[]{"1", "2", "3"});
            searchableObject.setAllowSwOutProdInd_analyzed("Y");
            add(searchableObject);
        }});

        ReflectionTestUtils.setField(predictiveSearchService, "localeMapping", new HashMap<String, String>() {{
            put("x-hhhh-chnl-countrycode.x-hhhh-locale", "1");
        }});

        when(siteFeature.getStringDefaultFeature(any(String.class), any(String.class))).thenReturn("allowSwOutProdInd");

        Boolean flag = false;
        try {
            predictiveSearchService.doService(predSrchRequest);
        } catch (Exception e) {
            flag = true;
        }
         org.junit.Assert.assertTrue(flag);
    }


    @Test
    public void testDoServiceBranch() throws Exception {

        PredSrchRequest predSrchRequest = new PredSrchRequest();
        predSrchRequest.addHeader("x-hhhh-locale", "x-hhhh-locale");
        predSrchRequest.addHeader("x-hhhh-chnl-countrycode", "x-hhhh-chnl-countrycode");
        predSrchRequest.addHeader("x-hhhh-chnl-group-member", "x-hhhh-chnl-group-member");
        predSrchRequest.setKeyword("ChannelRestrictCode");
        predSrchRequest.setAssetClasses(new String[]{"SEC", "WRTS"});
        predSrchRequest.setChannelRestrictCode("ChannelRestrictCode");
        predSrchRequest.setRestrOnlScribInd("RestrOnlScribInd");
        predSrchRequest.setMarket("Market");
        predSrchRequest.setFilterCriterias(new ArrayList<SearchCriteria>() {{
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setCriteriaKey("switchOutFund");
            searchCriteria.setOperator("o");
            searchCriteria.setCriteriaValue("switchOutFund:switchOutFund:switchOutFund:switchOutFund:switchOutFund");
            add(searchCriteria);
        }});

        when(predictiveSearchManager.findProductListByKey("symbol_analyzed:switchoutfund AND countryTradableCode_analyzed:switchOutFund AND productType_analyzed:switchOutFund", null, "x-hhhh-chnl-countrycode_x-hhhh-chnl-group-member", 1)).thenReturn(new ArrayList<SearchableObject>() {{
            SearchableObject searchableObject = new SearchableObject();
            searchableObject.setProductName_analyzed(new String[]{"x", "X", "x", "x", "X", "x"});
            searchableObject.setProductShortName_analyzed(new String[]{"x", "X", "x", "x", "X", "x"});
            searchableObject.setSwitchableGroup_analyzed(new String[]{"x", "2", "4"});
            searchableObject.setAllowSwOutProdInd_analyzed("Y");
            add(searchableObject);
        }});

        ReflectionTestUtils.setField(predictiveSearchService, "localeMapping", new HashMap<String, String>() {{
            put("x-hhhh-chnl-countrycode.x-hhhh-locale", "1");
        }});

        when(siteFeature.getStringDefaultFeature(any(String.class), any(String.class))).thenReturn("switchableGroup");


        when(predictiveSearchManager.findProductListByKey("(symbol_analyzed:channelrestrictcode) AND ((switchableGroup_analyzed:x OR switchableGroup_analyzed:2 OR switchableGroup_analyzed:4) AND  NOT (symbol_analyzed:switchoutfund AND countryTradableCode_analyzed:switchOutFund AND productType_analyzed:switchOutFund) AND  NOT resChannelCde_analyzed:channelrestrictcode AND  NOT restrOnlScribInd_analyzed:restronlscribind AND countryTradableCode_analyzed:market) AND (productType_analyzed:SEC OR productType_analyzed:WRTS)"
                , new String[]{"countryTradableCodeWeight", "productTypeWeight", "productName"}, "x-hhhh-chnl-countrycode_x-hhhh-chnl-group-member", 10)).thenReturn(new ArrayList<SearchableObject>() {{
            SearchableObject searchableObject = new SearchableObject();
            searchableObject.setProductName_analyzed(new String[]{"x", "X", "x", "x", "X", "x"});
            searchableObject.setProductShortName_analyzed(new String[]{"x", "X", "x", "x", "X", "x"});
            searchableObject.setSwitchableGroup_analyzed(new String[]{"x", "2", "4"});
            add(searchableObject);
        }});

        Product product = new Product();
        product.setProductDescriptor("descriptor");
        product.setText("Text");
        Map<String, Product> productEntities = this.productEntities.getProductEntities();
        productEntities.put("x-hhhh-chnl-countrycode_x-hhhh-chnl-group-member",product);
        when(this.productEntities.getProductEntities()).thenReturn(productEntities);
       Assert.assertNotNull(predictiveSearchService.doService(predSrchRequest));
    }


    @Test
    public void testBySymbolMarket() throws Exception {
        Boolean flag = false;
        try {
            predictiveSearchService.getBySymbolMarket(new InternalProductKey(), "locale");
        } catch (Exception e) {
            flag = true;
        }
         org.junit.Assert.assertTrue(flag);

        org.junit.Assert.assertNull(predictiveSearchService.getBySymbolMarket("countryCode", "groupMember", "groupMember", "groupMember", "groupMember", "groupMember"));

    }

    @Test
    public void testByRicCode() throws Exception {
        org.junit.Assert.assertNull(predictiveSearchService.getByRicCode("countryCode", "countryCode", "countryCode", "countryCode"));
    }


    @Test
    public void testByWCode() throws Exception {
        org.junit.Assert.assertNull(predictiveSearchService.getByWCode("countryCode", "countryCode", "countryCode", "countryCode"));
    }

    @Test
    public void testIsSpecialChar() throws Exception {
        ReflectionTestUtils.setField(predictiveSearchService, "pattern", Pattern.compile("xx"));
         org.junit.Assert.assertTrue(!predictiveSearchService.isSpecialChar("test"));
    }
}
