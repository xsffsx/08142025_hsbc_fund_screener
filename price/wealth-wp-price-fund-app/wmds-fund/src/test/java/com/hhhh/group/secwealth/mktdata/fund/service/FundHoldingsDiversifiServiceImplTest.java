package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtCatAsetAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtCatAsetAllocId;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstmId;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundHoldingsDiversifiRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundHoldingsDiversifiResponse;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundHoldingsDiversifiDao;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundHoldingsDiversifiServiceImplTest {


    @Mock
    private FundHoldingsDiversifiDao fundHoldingsDiversifiDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @InjectMocks
    private FundHoldingsDiversifiServiceImpl underTest;

    @Nested
    class WhenExecuting {
        @Mock
        private FundHoldingsDiversifiRequest request;
        @BeforeEach
        void setup() throws Exception {
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getLocale()).thenReturn("en_US");
            List<Integer> listInt = new ArrayList<>();
            listInt.add(2);
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = "UT";
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = "UT";
            obj1[8] = 12.3;
            obj1[9] = 12.3;
            obj1[10] = 12.3;
            obj1[11] = 12.3;
            obj1[12] = 12.3;
            li.add(obj1);
            List<UtProdInstm> utProdInstmList  = new ArrayList<>();
            UtProdInstm utProdInstm =  new UtProdInstm();
            UtProdInstmId utProdInstmId = new UtProdInstmId();
            utProdInstmId.setProductId(2);
            utProdInstmId.setBatchId(4);
            utProdInstm.setAllowReguCntbInd("Y");
            utProdInstm.setUtProdInstmId(utProdInstmId);
            utProdInstmList.add(utProdInstm);

            List<UtCatAsetAlloc> utCatAsetAllocs = new ArrayList<>();
            UtCatAsetAlloc utCatAsetAlloc = new UtCatAsetAlloc();
            UtCatAsetAllocId utCatAsetAllocId = new UtCatAsetAllocId();
            utCatAsetAllocId.setProductId(2);
            utCatAsetAllocId.setAssetClsCde("M");
            utCatAsetAllocId.setClassTypeCode("fd");
            utCatAsetAlloc.setUtCatAsetAllocId(utCatAsetAllocId);
            utCatAsetAllocs.add(utCatAsetAlloc);
            Mockito.when(fundHoldingsDiversifiDao.searchProductId(any(FundHoldingsDiversifiRequest.class))).thenReturn(listInt);
            Mockito.when(fundHoldingsDiversifiDao.searchHoldingDiversification(ArgumentMatchers.<Integer>anyList())).thenReturn(utProdInstmList);
            Mockito.when(fundHoldingsDiversifiDao.searchAllocation(ArgumentMatchers.<Integer>anyList(),any(String.class))).thenReturn(utCatAsetAllocs);
        }
        @Test
         void test_WhenExecuting() throws Exception {
            FundHoldingsDiversifiResponse response  = (FundHoldingsDiversifiResponse)underTest.execute(request);
            Assert.assertNotNull(response.getHoldingsDiversifications());
        }
    }
}