package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.criteria.ListValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.MinMaxValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchCriteriaRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundSearchCriteriaResponse;
import com.hhhh.group.secwealth.mktdata.fund.converter.BaseValidateConverter;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundSearchCriteriaDao;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundSearchCriteriaServiceImplTest {


    @Mock
    private Map<String, String> fundSearchCriteria;
    @Mock
    private FundSearchCriteriaDao fundSearchCriteriaDao;
    @Mock
    private BaseValidateConverter baseValidateConverter;
    @InjectMocks
    private FundSearchCriteriaServiceImpl underTest;

    @Nested
    class WhenExecuting {
        @Mock
        private FundSearchCriteriaRequest request;

        @BeforeEach
        void setup() throws Exception {

            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getLocale()).thenReturn("en_US");

            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            //Mockito.when(request.getProductType()).thenReturn("UT");
            Mockito.when(fundSearchCriteriaDao.searchListCriteria(any(),any(), any(), any(),any())).thenReturn(new ArrayList<ListValue>(){{add(new ListValue());}});
            Mockito.when(request.getMinMaxCriteriaKeys()).thenReturn(new String[]{"ALL"});
            Mockito.when(request.getListCriteriaKeys()).thenReturn(new String[]{"ALL"});
            List<SearchCriteria> searchCriteriaList =  new ArrayList<>();
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("PCDI");
            searchCriteria.setCriteriaValue("Y");
            searchCriteriaList.add(searchCriteria);
            Mockito.when(request.getPredefinedCriterias()).thenReturn(searchCriteriaList);
        }

        @Test
         void test_WhenExecuting() throws Exception {
            Mockito.when(fundSearchCriteria.get(anyString())).thenReturn("allListCriteriaKeys");
            FundSearchCriteriaResponse response =  (FundSearchCriteriaResponse)underTest.execute(request);
            Assert.assertNotNull(response);
        }
        @Test
         void test_WhenExecuting1() throws Exception {
            Mockito.when(fundSearchCriteria.get(anyString())).thenReturn("allMinMaxCriteriaKeys");
            Mockito.when(fundSearchCriteriaDao.searchMinMaxCriteria(any(),
                    any(), any(), any())).thenReturn(new ArrayList<MinMaxValue>(){{add(new MinMaxValue());}});
            Mockito.when(fundSearchCriteria.get(anyString())).thenReturn("allMinMaxCriteriaKeys");
            FundSearchCriteriaResponse response =  (FundSearchCriteriaResponse)underTest.execute(request);
            Assert.assertNotNull(response);
        }
    }
}