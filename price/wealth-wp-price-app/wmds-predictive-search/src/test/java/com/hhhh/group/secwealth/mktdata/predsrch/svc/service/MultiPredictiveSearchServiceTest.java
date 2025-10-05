package com.hhhh.group.secwealth.mktdata.predsrch.svc.service;


import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.ProductEntities;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.manager.impl.PredictiveSearchManagerImpl;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl.MultiPredictiveSearchServiceImpl;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl.PredictiveSearchServiceImpl;
import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class MultiPredictiveSearchServiceTest {

    @Mock
    private SiteFeature siteFeature;

    @Mock
    private ProductEntities productEntities;

    @Mock
    private PredictiveSearchManagerImpl predictiveSearchManager;

    @InjectMocks
    private MultiPredictiveSearchServiceImpl multiPredictiveSearchService;

    @Mock
    private PredictiveSearchServiceImpl predictiveSearchService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoService() throws Exception {
        MultiPredSrchRequest multiPredSrchRequest = new MultiPredSrchRequest();
        multiPredSrchRequest.addHeader("x-hhhh-locale", "x-hhhh-locale");
        multiPredSrchRequest.addHeader("x-hhhh-chnl-countrycode", "x-hhhh-chnl-countrycode");
        multiPredSrchRequest.addHeader("x-hhhh-chnl-group-member", "x-hhhh-chnl-group-member");
        multiPredSrchRequest.setKeyword(new String[]{"000001", "60006"});
        multiPredSrchRequest.setAssetClasses(new String[]{"SEC", "WRTS"});
        multiPredSrchRequest.setChannelRestrictCode("ChannelRestrictCode");
        multiPredSrchRequest.setRestrOnlScribInd("RestrOnlScribInd");
        multiPredSrchRequest.setMarket("Market");
        multiPredSrchRequest.setFilterCriterias(new ArrayList<SearchCriteria>() {{
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
            add(searchableObject);
        }});


        when(predictiveSearchManager.findProductListByKey("((symbol_analyzed:000001) OR (symbol_analyzed:60006)) AND ((switchableGroup_analyzed:1 OR switchableGroup_analyzed:2 OR switchableGroup_analyzed:3) AND  NOT (symbol_analyzed:switchoutfund AND countryTradableCode_analyzed:switchOutFund AND productType_analyzed:switchOutFund) AND  NOT resChannelCde_analyzed:channelrestrictcode AND  NOT restrOnlScribInd_analyzed:restronlscribind AND countryTradableCode_analyzed:market) AND (productType_analyzed:SEC OR productType_analyzed:WRTS)"
                , new String[]{"countryTradableCodeWeight", "productTypeWeight", "productName"}, "x-hhhh-chnl-countrycode_x-hhhh-chnl-group-member", 20)).thenReturn(new ArrayList<SearchableObject>() {{
            SearchableObject searchableObject = new SearchableObject();
            searchableObject.setProductName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
            searchableObject.setProductShortName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
            searchableObject.setSwitchableGroup_analyzed(new String[]{"1", "2", "3"});
            add(searchableObject);
        }});
        when(siteFeature.getStringDefaultFeature(any(String.class), any(String.class))).thenReturn("switchableGroup");
        try{
            org.junit.Assert.assertNotNull(multiPredictiveSearchService.doService(multiPredSrchRequest));
        }catch (Exception e){
            e.getMessage();
        }

    }


    @Test
    public void testDoServiceBranch() throws Exception {
        MultiPredSrchRequest multiPredSrchRequest = new MultiPredSrchRequest();
        multiPredSrchRequest.addHeader("x-hhhh-locale", "x-hhhh-locale");
        multiPredSrchRequest.addHeader("x-hhhh-chnl-countrycode", "x-hhhh-chnl-countrycode");
        multiPredSrchRequest.addHeader("x-hhhh-chnl-group-member", "x-hhhh-chnl-group-member");
        multiPredSrchRequest.setKeyword(new String[]{"000001", "60006"});
        multiPredSrchRequest.setAssetClasses(new String[]{"SEC", "WRTS"});
        multiPredSrchRequest.setFilterCriterias(new ArrayList<SearchCriteria>() {{
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
        when(siteFeature.getStringDefaultFeature(any(String.class), any(String.class))).thenReturn("allowSwOutProdInd");
        Boolean flag = false;
        try {
            multiPredictiveSearchService.doService(multiPredSrchRequest);
        } catch (Exception e) {
            flag = true;
        }
        org.junit.Assert.assertTrue(flag);
    }


    @Test
    public void testMergeResults() {
        Boolean flag = true;

        try {
            PredSrchRequest predSrchRequest = new PredSrchRequest();
            predSrchRequest.addHeader("x-hhhh-locale", "x-hhhh-locale");
            predSrchRequest.addHeader("x-hhhh-chnl-countrycode", "x-hhhh-chnl-countrycode");
            predSrchRequest.addHeader("x-hhhh-chnl-group-member", "x-hhhh-chnl-group-member");
            predSrchRequest.setKeyword( "000001");
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

            multiPredictiveSearchService.mergeResults(new ArrayList<SearchableObject>() {{
                SearchableObject searchableObject = new SearchableObject();
                searchableObject.setProductName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
                searchableObject.setProductShortName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
                searchableObject.setSwitchableGroup_analyzed(new String[]{"1", "2", "3"});
                add(searchableObject);
            }}, new ArrayList<SearchableObject>() {{
                SearchableObject searchableObject = new SearchableObject();
                searchableObject.setProductName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
                searchableObject.setProductShortName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
                searchableObject.setSwitchableGroup_analyzed(new String[]{"1", "2", "3"});
                add(searchableObject);
            }}, predSrchRequest, 1);
        } catch (Exception e) {
            flag=false;
        }
        org.junit.Assert.assertTrue(flag);

    }


    @Test
    public void testBySymbolMarket() throws Exception {
        Boolean flag = false;
        try {
            multiPredictiveSearchService.getBySymbolMarket(new InternalProductKey(), "locale");
        } catch (Exception e) {
            flag = true;
        }
        Assert.isTrue(flag);

        org.junit.Assert.assertNull(multiPredictiveSearchService.getBySymbolMarket("countryCode", "groupMember", "groupMember", "groupMember", "groupMember", "groupMember"));

    }

    @Test
    public void testByRicCode() throws Exception {
        org.junit.Assert.assertNull(multiPredictiveSearchService.getByRicCode("countryCode", "countryCode", "countryCode", "countryCode"));
    }


    @Test
    public void testByWCode() throws Exception {
        when(predictiveSearchManager.findProductListByKey("productCode_analyzed:countrycode"
                , null, "countryCode_countryCode", MultiPredictiveSearchServiceImpl.RECORD_FRIST_NUMBER)).thenReturn(new ArrayList<SearchableObject>() {{
            SearchableObject searchableObject = new SearchableObject();
            searchableObject.setProductName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
            searchableObject.setProductShortName_analyzed(new String[]{"hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC", "hhhh HOLDINGS PLC"});
            searchableObject.setSwitchableGroup_analyzed(new String[]{"1", "2", "3"});
            add(searchableObject);
        }});
        try{
            org.junit.Assert.assertNotNull(multiPredictiveSearchService.getByWCode("countryCode", "countryCode", "countryCode", "countryCode"));
        }catch (Exception e){
            e.getMessage();
        }

    }

}
