package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;

import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchCriteriaRequest;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.DetailedCriteriaUtil;
import org.junit.Assert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FundSearchCriteriaDaoImplTest {
    @InjectMocks
    private FundSearchCriteriaDaoImpl underTest;

    @Mock
    private Query query;
    @Mock
    private EntityManager entityManager;
    @Mock
    private BaseDao baseDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private DetailedCriteriaUtil detailedCriteriaUtil;
//    @Mock
//    private FundCriteriaKeyMapper criteriaKeyMapper;

    @BeforeEach
    public void setup() throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        String[] dbResultLine = new String[6];
        dbResultLine[0] = "dbResultLine1";
        dbResultLine[1] = "dbResultLine2";
        dbResultLine[2] = "dbResultLine3";
        dbResultLine[3] = "dbResultLine3";
        dbResultLine[4] = "dbResultLine3";
        dbResultLine[5] = "dbResultLine3";
        List<Object> list = new ArrayList<>();
        list.add(dbResultLine);
        when(query.getResultList()).thenReturn(list);
    }

    @Test
    void searchMinMaxCriteria()throws Exception  {
      //  when(criteriaKeyMapper.getDbFieldName(anyString(),anyString(),anyString())).thenReturn(null);
        when(detailedCriteriaUtil.toString(any(),any(),any(),any(),any(),any())).thenReturn("select * from table");
        List<SearchCriteria> li = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("NAME");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        li.add(searchCriteria);
        FundSearchCriteriaRequest request = new FundSearchCriteriaRequest();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.setProductType("UT");
        request.setPredefinedCriterias(li);
        Assert.assertNotNull(underTest.searchMinMaxCriteria(request,"allowSellMipProdInd","HK","HBAP"));

    }

    @Test
    void searchMinMaxCriteria2()throws Exception  {
        //  when(criteriaKeyMapper.getDbFieldName(anyString(),anyString(),anyString())).thenReturn(null);
        when(detailedCriteriaUtil.toString(any(),any(),any(),any(),any(),any())).thenReturn("select * from table");
        FundSearchCriteriaRequest request = new FundSearchCriteriaRequest();
        request.setChannelRestrictCode("");
        request.setRestrOnlScribInd("");
        request.setProductType(null);
        request.setPredefinedCriterias(null);
        Assert.assertNotNull(underTest.searchMinMaxCriteria(request,"allowSellMipProdInd","HK","HBAP"));

    }

    @Test
    void searchMinMaxCriteria3()throws Exception  {
        //  when(criteriaKeyMapper.getDbFieldName(anyString(),anyString(),anyString())).thenReturn(null);
        when(detailedCriteriaUtil.toString(any(),any(),any(),any(),any(),any())).thenReturn("select * from table");
        List<SearchCriteria> li = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("NAME");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        li.add(searchCriteria);
        FundSearchCriteriaRequest request = new FundSearchCriteriaRequest();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.setProductType("UT");
        request.setPredefinedCriterias(li);
        Assert.assertNotNull(underTest.searchMinMaxCriteria(request,null,"HK","HBAP"));

    }

    @Test
    void searchListCriteria() throws Exception {

        List<SearchCriteria> li = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("NAME");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        li.add(searchCriteria);
        FundSearchCriteriaRequest request = new FundSearchCriteriaRequest();
        request.setPredefinedCriterias(li);
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.setProductType("UT");
        Assert.assertNotNull(underTest.searchListCriteria(request,null,"fd","Y","Y"));
    }

    @Test
    void searchListCriteria2() throws Exception {
        FundSearchCriteriaRequest request = new FundSearchCriteriaRequest();
        request.setChannelRestrictCode("");
        request.setRestrOnlScribInd("");
        request.setProductType("");
        Assert.assertNotNull(underTest.searchListCriteria(request,null,"fd","Y","Y"));
    }

    @Test
    void searchListCriteriaFAM() throws Exception {

        when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(1);
        List<SearchCriteria> li = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("NAME");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        li.add(searchCriteria);
        FundSearchCriteriaRequest request = new FundSearchCriteriaRequest();
        request.setPredefinedCriterias(li);
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.setProductType("UT");
        Assert.assertNotNull(underTest.searchListCriteria(request,"FAM","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaFAM2() throws Exception {
        when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(2);
        List<SearchCriteria> li = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("NAME");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        li.add(searchCriteria);
        FundSearchCriteriaRequest request = new FundSearchCriteriaRequest();
        request.setPredefinedCriterias(li);
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.setProductType("UT");
        Assert.assertNotNull(underTest.searchListCriteria(request,"FAM","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaFAM3() throws Exception {
        FundSearchCriteriaRequest request = buildSearchListRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"FAM","fd","Y","Y"));
    }

    public FundSearchCriteriaRequest buildSearchListRequest() throws Exception {

        when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(0);
        List<SearchCriteria> li = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("NAME");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        li.add(searchCriteria);
        FundSearchCriteriaRequest request = new FundSearchCriteriaRequest();
        request.setPredefinedCriterias(li);
        String channelRestrictCode = "SI_I";
        String restrOnlScribInd = "Y";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.setProductType("UT");
        return request;

    }

    @Test
    void searchListCriteriaCAT() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"CAT","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaCAT2() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"CAT","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaRISK() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"RISK","fd","Y","Y"));
    }
    @Test
    void searchListCriteriaCCY() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"CCY","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaDF() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"DF","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaY1QTL() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"Y1QTL","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaY3QTL() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"Y3QTL","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaY5QTL() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"Y5QTL","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaY10QTL() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"Y10QTL","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaACQN() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"ACQN","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaCATLV1() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"CATLV1","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaINVSTRG() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"INVSTRG","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaAMCM() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"AMCM","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaPSC() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"PSC","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaPCDI() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"PCDI","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaGBAA() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"GBAA","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaGNRA() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"GNRA","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaSIFC() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"SIFC","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaSRLN() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"SRLN","fd","Y","Y"));
    }

    @Test
    void searchListCriteriaESG() throws Exception {
        FundSearchCriteriaRequest request = buildRequest();
        Assert.assertNotNull(underTest.searchListCriteria(request,"ESG","fd","Y","Y"));
    }
     public FundSearchCriteriaRequest buildRequest(){
         List<SearchCriteria> li = new ArrayList<>();
         SearchCriteria searchCriteria = new SearchCriteria();
         searchCriteria.setCriteriaKey("NAME");
         searchCriteria.setCriteriaValue("hhhhA");
         searchCriteria.setOperator("eq");
         li.add(searchCriteria);
         FundSearchCriteriaRequest request = new FundSearchCriteriaRequest();
         request.setPredefinedCriterias(li);
         String channelRestrictCode ="SI_I";
         String restrOnlScribInd ="Y";
         request.setChannelRestrictCode(channelRestrictCode);
         request.setRestrOnlScribInd(restrOnlScribInd);
         request.setProductType("UT");
        return request;
     }

    @Test
    void appendPredefinedCriterias()throws Exception  {
    //    when(detailedCriteriaUtil.toString(any(),any(),any(),any(),any(),any())).thenReturn(any());
        List<SearchCriteria> li = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("NAME");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        li.add(searchCriteria);
        Assert.assertNotNull(underTest.appendPredefinedCriterias(new StringBuilder("slect"),li,"HK","HBAP"));
    }

    @Test
    void validatedCriteriaString() throws Exception {
        Assert.assertNotNull(underTest.validatedCriteriaString("CAT"));
    }

    @Test
    void format() throws Exception {
        List<String> li = new ArrayList<>();
        li.add("hk");
        underTest.format(new StringBuilder("SELECT"),"TP",li,"HK","HBAP");
        Assert.assertNotNull(li);
    }

    @Test
    void getMappedCriteriaKey() throws Exception {
        Assert.assertNotNull(underTest.getMappedCriteriaKey("CAT","HK","HBAP"));

    }

    @Test
    void prefixTableAlias()throws Exception  {
        Assert.assertNotNull(underTest.prefixTableAlias("CAT","HK"));

    }
}