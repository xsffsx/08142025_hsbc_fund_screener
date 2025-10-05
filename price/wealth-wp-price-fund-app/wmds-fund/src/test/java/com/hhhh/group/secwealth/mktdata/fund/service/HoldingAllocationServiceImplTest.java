package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAllocId;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundQuoteSummaryRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.HoldingAllocationRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.HoldingAllocationResponse;
import com.hhhh.group.secwealth.mktdata.fund.dao.HoldingAllocationDao;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HoldingAllocationServiceImplTest {


    @Mock
    private HoldingAllocationDao holdingAllocationDao;
    @InjectMocks
    private HoldingAllocationServiceImpl underTest;

    @Nested
    class WhenExecuting {
        @Mock
        private HoldingAllocationRequest request;

        @BeforeEach
        void setup() throws Exception {

            List<Object[]> li = new ArrayList<>();
            Object[] obj1 = new Object[4];
            obj1[0] = "0P0001H6J1";
            obj1[1] = 12.3;
            obj1[2] = 23.6;
            obj1[3] = "2021-07-21";
            li.add(obj1);
            Mockito.when(holdingAllocationDao.searchHoldingAllocList(any(HoldingAllocationRequest.class))).thenReturn(li);
            List<UTHoldingAlloc> utHoldingAllocList = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId  = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassName("ASET_ALLOC");
            utHoldingAllocId.setHoldingAllocClassType("ASET_ALLOC");
            utHoldingAllocId.setBatchId(1l);
            utHoldingAllocId.setPerformanceId("0P0001H6J1");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            utHoldingAlloc.setHoldingAllocWeight( new BigDecimal(12.3));
            utHoldingAlloc.setHoldingAllocWeight(new BigDecimal(8.2));
            utHoldingAlloc.setPortfolioDate(new Date());
            utHoldingAlloc.setUpdatedOn(new Date());
            utHoldingAllocList.add(utHoldingAlloc);
            Mockito.when(holdingAllocationDao.searchHoldingAllocation(any(String.class))).thenReturn(utHoldingAllocList);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            HoldingAllocationResponse response =  (HoldingAllocationResponse)underTest.execute(request);
            Assertions.assertNotNull(response.getHoldingAllocation());


        }
    }

    @Nested
    class WhenExecuting1 {
        @Mock
        private HoldingAllocationRequest request;

        @BeforeEach
        void setup() throws Exception {

            List<Object[]> li = new ArrayList<>();
            Object[] obj1 = new Object[4];
            obj1[0] = "0P0001H6J1";
            obj1[1] = 12.3;
            obj1[2] = 23.6;
            obj1[3] = "2021-07-21";
            li.add(obj1);
            Mockito.when(holdingAllocationDao.searchHoldingAllocList(any(HoldingAllocationRequest.class))).thenReturn(li);
            List<UTHoldingAlloc> utHoldingAllocList = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId  = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassName("STOCK_SEC");
            utHoldingAllocId.setHoldingAllocClassType("STOCK_SEC");
            utHoldingAllocId.setBatchId(1l);
            utHoldingAllocId.setPerformanceId("0P0001H6J1");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            utHoldingAlloc.setHoldingAllocWeight( new BigDecimal(12.3));
            utHoldingAlloc.setHoldingAllocWeight(new BigDecimal(8.2));
            utHoldingAlloc.setPortfolioDate(new Date());
            utHoldingAlloc.setUpdatedOn(new Date());
            utHoldingAllocList.add(utHoldingAlloc);
            Mockito.when(holdingAllocationDao.searchHoldingAllocation(any(String.class))).thenReturn(utHoldingAllocList);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            underTest.execute(request);
            Assert.assertTrue(true);
        }
    }

    @Nested
    class WhenExecuting2 {
        @Mock
        private HoldingAllocationRequest request;

        @BeforeEach
        void setup() throws Exception {

            List<Object[]> li = new ArrayList<>();
            Object[] obj1 = new Object[4];
            obj1[0] = "0P0001H6J1";
            obj1[1] = 12.3;
            obj1[2] = 23.6;
            obj1[3] = "2021-07-21";
            li.add(obj1);
            Mockito.when(holdingAllocationDao.searchHoldingAllocList(any(HoldingAllocationRequest.class))).thenReturn(li);
            List<UTHoldingAlloc> utHoldingAllocList = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId  = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassName("STOCK_GEO");
            utHoldingAllocId.setHoldingAllocClassType("STOCK_GEO");
            utHoldingAllocId.setBatchId(1l);
            utHoldingAllocId.setPerformanceId("0P0001H6J1");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            utHoldingAlloc.setHoldingAllocWeight( new BigDecimal(12.3));
            utHoldingAlloc.setHoldingAllocWeight(new BigDecimal(8.2));
            utHoldingAlloc.setPortfolioDate(new Date());
            utHoldingAlloc.setUpdatedOn(new Date());
            utHoldingAllocList.add(utHoldingAlloc);
            Mockito.when(holdingAllocationDao.searchHoldingAllocation(any(String.class))).thenReturn(utHoldingAllocList);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            HoldingAllocationResponse response=(HoldingAllocationResponse) underTest.execute(request);
            Assert.assertNotNull(response.getLastUpdatedDate());
        }
    }

    @Nested
    class WhenExecuting3 {
        @Mock
        private HoldingAllocationRequest request;

        @BeforeEach
        void setup() throws Exception {

            List<Object[]> li = new ArrayList<>();
            Object[] obj1 = new Object[4];
            obj1[0] = "0P0001H6J1";
            obj1[1] = 12.3;
            obj1[2] = 23.6;
            obj1[3] = "2021-07-21";
            li.add(obj1);
            Mockito.when(holdingAllocationDao.searchHoldingAllocList(any(HoldingAllocationRequest.class))).thenReturn(li);
            List<UTHoldingAlloc> utHoldingAllocList = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId  = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassName("BOND_SEC");
            utHoldingAllocId.setHoldingAllocClassType("BOND_SEC");
            utHoldingAllocId.setBatchId(1l);
            utHoldingAllocId.setPerformanceId("0P0001H6J1");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            utHoldingAlloc.setHoldingAllocWeight( new BigDecimal(12.3));
            utHoldingAlloc.setHoldingAllocWeight(new BigDecimal(8.2));
            utHoldingAlloc.setPortfolioDate(new Date());
            utHoldingAlloc.setUpdatedOn(new Date());
            utHoldingAllocList.add(utHoldingAlloc);
            Mockito.when(holdingAllocationDao.searchHoldingAllocation(any(String.class))).thenReturn(utHoldingAllocList);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            HoldingAllocationResponse response=(HoldingAllocationResponse) underTest.execute(request);
            Assert.assertNotNull(response.getLastUpdatedDate());

        }
    }

    @Nested
    class WhenExecuting4 {
        @Mock
        private HoldingAllocationRequest request;

        @BeforeEach
        void setup() throws Exception {

            List<Object[]> li = new ArrayList<>();
            Object[] obj1 = new Object[4];
            obj1[0] = "0P0001H6J1";
            obj1[1] = 12.3;
            obj1[2] = 23.6;
            obj1[3] = "2021-07-21";
            li.add(obj1);
            Mockito.when(holdingAllocationDao.searchHoldingAllocList(any(HoldingAllocationRequest.class))).thenReturn(li);
            List<UTHoldingAlloc> utHoldingAllocList = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId  = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassName("BOND_GEO");
            utHoldingAllocId.setHoldingAllocClassType("BOND_GEO");
            utHoldingAllocId.setBatchId(1l);
            utHoldingAllocId.setPerformanceId("0P0001H6J1");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            utHoldingAlloc.setHoldingAllocWeight( new BigDecimal(12.3));
            utHoldingAlloc.setHoldingAllocWeight(new BigDecimal(8.2));
            utHoldingAlloc.setPortfolioDate(new Date());
            utHoldingAlloc.setUpdatedOn(new Date());
            utHoldingAllocList.add(utHoldingAlloc);
            Mockito.when(holdingAllocationDao.searchHoldingAllocation(any(String.class))).thenReturn(utHoldingAllocList);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            HoldingAllocationResponse response=(HoldingAllocationResponse) underTest.execute(request);
            Assert.assertNotNull(response.getLastUpdatedDate());

        }
    }
}