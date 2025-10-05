package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.criteria.Criterias;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.*;
import com.hhhh.group.secwealth.mktdata.common.service.ServiceExecutor;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundListRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundListResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundCalendarYearReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundCumulativeReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundListDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FundListServiceImplTest {

    private static final String STRING = "STRING";
    @Mock
    private ServiceExecutor fundListServiceExecutor;
    @Mock
    private FundListDao fundListDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private FundListResponse fundListResponse;
    @Mock
    private List<String> tradableCurrencyProdUsingMap;
    @InjectMocks
    private FundListServiceImpl fundListService;

    @Nested
    class WhenExecuting {
        @Mock
        private FundListRequest request;

        @BeforeEach
        void setup() throws Exception{
            Mockito.when(request.getChannelRestrictCode()).thenReturn("SRBPI");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(fundListResponse.getProducts()).thenReturn(DataBuilder.buildFundListProduct());
            Mockito.when(fundListResponse.getSearchProductList()).thenReturn(DataBuilder.buildSearchProductList());

            Mockito.when(request.getProductKeys()).thenReturn(DataBuilder.buildProductKeyList());
            Mockito.when(fundListServiceExecutor.execute(request)).thenReturn(DataBuilder.buildFundListResponse());
            Mockito.when(tradableCurrencyProdUsingMap.contains(anyString())).thenReturn(true);
        }

        @Test
         void test_WhenExecuting() throws Exception {
            Map<Integer, List<String>> utProdChanl = new HashMap<>();
            Mockito.when(fundListDao.searchChanlFund(anyString())).thenReturn(utProdChanl);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList());
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }

        @Test
         void test_WhenExecuting2() throws Exception {
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(0);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList2());
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }

        @Test
        void test_WhenExecuting3() throws Exception {
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(1);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList2());
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }

        @Test
        void test_WhenExecuting4() throws Exception {
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(2);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList2());
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }

        @Test
        void test_WhenExecuting5() throws Exception {
            Map<Integer, List<String>> utProdChanl = new HashMap<>();
            List<String> indicate = new ArrayList<>();
            indicate.add("s1");
            indicate.add("s2");
            indicate.add("s3");
            indicate.add("s4");
            utProdChanl.put(2,indicate);
            Mockito.when(fundListDao.searchChanlFund(anyString())).thenReturn(utProdChanl);
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(3);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList2());

            Map<String, List<UtHoldings>> fundHoldingMap = new HashMap<>();
            List<UtHoldings> list = new ArrayList<>();
            UtHoldings utHoldings = new UtHoldings();
            UtHoldingsId utHoldingsId = new UtHoldingsId();
            utHoldingsId.setHoldingId(2);
            utHoldings.setUtHoldingsId(utHoldingsId);
            list.add(utHoldings);
            fundHoldingMap.put("id", list);
            Mockito.when(fundListDao.searchTopTenHldgMap(any(FundListRequest.class),anyString())).thenReturn(fundHoldingMap);

            Map<String, List<UTHoldingAlloc>> holdingAllocMap = new HashMap<>();
            List<UTHoldingAlloc> list1 = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassType("ASET_ALLOC");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            list1.add(utHoldingAlloc);
            holdingAllocMap.put("id",list1);
            Mockito.when(fundListDao.searchHoldingAllocation(any(FundListRequest.class))).thenReturn(holdingAllocMap);
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }

        @Test
        void test_WhenExecuting7() throws Exception {
            Map<Integer, List<String>> utProdChanl = new HashMap<>();
            List<String> indicate = new ArrayList<>();
            indicate.add("s1");
            indicate.add("s2");
            indicate.add("s3");
            indicate.add("s4");
            utProdChanl.put(2,indicate);
            Mockito.when(fundListDao.searchChanlFund(anyString())).thenReturn(utProdChanl);
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(3);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList2());

            Map<String, List<UtHoldings>> fundHoldingMap = new HashMap<>();
            List<UtHoldings> list = new ArrayList<>();
            UtHoldings utHoldings = new UtHoldings();
            UtHoldingsId utHoldingsId = new UtHoldingsId();
            utHoldingsId.setHoldingId(2);
            utHoldings.setUtHoldingsId(utHoldingsId);
            list.add(utHoldings);
            fundHoldingMap.put("id", list);
            Mockito.when(fundListDao.searchTopTenHldgMap(any(FundListRequest.class),anyString())).thenReturn(fundHoldingMap);

            Map<String, List<UTHoldingAlloc>> holdingAllocMap = new HashMap<>();
            List<UTHoldingAlloc> list1 = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassType("STOCK_SEC");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            list1.add(utHoldingAlloc);
            holdingAllocMap.put("id",list1);
            Mockito.when(fundListDao.searchHoldingAllocation(any(FundListRequest.class))).thenReturn(holdingAllocMap);
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }

        @Test
        void test_WhenExecuting8() throws Exception {
            Map<Integer, List<String>> utProdChanl = new HashMap<>();
            List<String> indicate = new ArrayList<>();
            indicate.add("s1");
            indicate.add("s2");
            indicate.add("s3");
            indicate.add("s4");
            utProdChanl.put(2,indicate);
            Mockito.when(fundListDao.searchChanlFund(anyString())).thenReturn(utProdChanl);
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(3);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList2());

            Map<String, List<UtHoldings>> fundHoldingMap = new HashMap<>();
            List<UtHoldings> list = new ArrayList<>();
            UtHoldings utHoldings = new UtHoldings();
            UtHoldingsId utHoldingsId = new UtHoldingsId();
            utHoldingsId.setHoldingId(2);
            utHoldings.setUtHoldingsId(utHoldingsId);
            list.add(utHoldings);
            fundHoldingMap.put("id", list);
            Mockito.when(fundListDao.searchTopTenHldgMap(any(FundListRequest.class),anyString())).thenReturn(fundHoldingMap);

            Map<String, List<UTHoldingAlloc>> holdingAllocMap = new HashMap<>();
            List<UTHoldingAlloc> list1 = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassType("STOCK_GEO");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            list1.add(utHoldingAlloc);
            holdingAllocMap.put("id",list1);
            Mockito.when(fundListDao.searchHoldingAllocation(any(FundListRequest.class))).thenReturn(holdingAllocMap);
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }

        @Test
        void test_WhenExecuting9() throws Exception {
            Map<Integer, List<String>> utProdChanl = new HashMap<>();
            List<String> indicate = new ArrayList<>();
            indicate.add("s1");
            indicate.add("s2");
            indicate.add("s3");
            indicate.add("s4");
            utProdChanl.put(2,indicate);
            Mockito.when(fundListDao.searchChanlFund(anyString())).thenReturn(utProdChanl);
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(3);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList2());

            Map<String, List<UtHoldings>> fundHoldingMap = new HashMap<>();
            List<UtHoldings> list = new ArrayList<>();
            UtHoldings utHoldings = new UtHoldings();
            UtHoldingsId utHoldingsId = new UtHoldingsId();
            utHoldingsId.setHoldingId(2);
            utHoldings.setUtHoldingsId(utHoldingsId);
            list.add(utHoldings);
            fundHoldingMap.put("id", list);
            Mockito.when(fundListDao.searchTopTenHldgMap(any(FundListRequest.class),anyString())).thenReturn(fundHoldingMap);

            Map<String, List<UTHoldingAlloc>> holdingAllocMap = new HashMap<>();
            List<UTHoldingAlloc> list1 = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassType("BOND_SEC");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            list1.add(utHoldingAlloc);
            holdingAllocMap.put("id",list1);
            Mockito.when(fundListDao.searchHoldingAllocation(any(FundListRequest.class))).thenReturn(holdingAllocMap);
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }

        @Test
        void test_WhenExecuting10() throws Exception {
            Map<Integer, List<String>> utProdChanl = new HashMap<>();
            List<String> indicate = new ArrayList<>();
            indicate.add("s1");
            indicate.add("s2");
            indicate.add("s3");
            indicate.add("s4");
            utProdChanl.put(2,indicate);
            Mockito.when(fundListDao.searchChanlFund(anyString())).thenReturn(utProdChanl);
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(3);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList2());

            Map<String, List<UtHoldings>> fundHoldingMap = new HashMap<>();
            List<UtHoldings> list = new ArrayList<>();
            UtHoldings utHoldings = new UtHoldings();
            UtHoldingsId utHoldingsId = new UtHoldingsId();
            utHoldingsId.setHoldingId(2);
            utHoldings.setUtHoldingsId(utHoldingsId);
            list.add(utHoldings);
            fundHoldingMap.put("id", list);
            Mockito.when(fundListDao.searchTopTenHldgMap(any(FundListRequest.class),anyString())).thenReturn(fundHoldingMap);

            Map<String, List<UTHoldingAlloc>> holdingAllocMap = new HashMap<>();
            List<UTHoldingAlloc> list1 = new ArrayList<>();
            UTHoldingAlloc utHoldingAlloc = new UTHoldingAlloc();
            UTHoldingAllocId utHoldingAllocId = new UTHoldingAllocId();
            utHoldingAllocId.setHoldingAllocClassType("BOND_GEO");
            utHoldingAlloc.setUtHoldingAllocId(utHoldingAllocId);
            list1.add(utHoldingAlloc);
            holdingAllocMap.put("id",list1);
            Mockito.when(fundListDao.searchHoldingAllocation(any(FundListRequest.class))).thenReturn(holdingAllocMap);
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }

        @Test
        void test_WhenExecuting6() throws Exception {
            Map<Integer, List<String>> utProdChanl = new HashMap<>();
            List<String> indicate = new ArrayList<>();
            indicate.add("s1");
            indicate.add("s2");
            indicate.add("s3");
            indicate.add("");
            utProdChanl.put(2,indicate);
            Mockito.when(fundListDao.searchChanlFund(anyString())).thenReturn(utProdChanl);
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(3);
            Mockito.when(fundListDao.searchProductList(any(FundListRequest.class))).thenReturn(DataBuilder.buildUtProdInstmList2());
            fundListService.execute(request);
            Assertions.assertEquals("SRBPI",request.getChannelRestrictCode());
        }
    }

    private static class DataBuilder {
        public static List<SearchProduct>  buildSearchProductList(){
            List<SearchProduct>  searchProductList = new ArrayList<>();
            SearchProduct searchProduct = new SearchProduct();
            searchProduct.setExternalKey("0P00007KN5");
            SearchableObject searchObject = new SearchableObject();
            searchObject.setSymbol("540002");
            searchProduct.setSearchObject(searchObject);
            searchProductList.add(searchProduct);
            return searchProductList;
        }

        public static List<FundListProduct>  buildFundListProduct(){
            List<FundListProduct>  fundListProductList = new ArrayList<>();
            FundListProduct fundListProduct = new FundListProduct();
            fundListProduct.setProdAltNumXCode("0P00007KN5");
            fundListProduct.setSymbol("540002");
            FundListProduct.Header header = fundListProduct.new Header();
            fundListProduct.setHeader(header);
            FundListProduct.Summary summary = fundListProduct.new Summary();
            fundListProduct.setSummary(summary);
            FundListProduct.Profile profile = fundListProduct.new Profile();
            fundListProduct.setProfile(profile);
            FundListProduct.Performance performance = fundListProduct.new Performance();
            fundListProduct.setPerformance(performance);
            fundListProduct.setProdAltNumXCode("0P00007KN5");
            FundListProduct.InvestmentStrategy investmentStrategy = fundListProduct.new InvestmentStrategy();
            fundListProduct.setInvestmentStrategy(investmentStrategy);
            FundListProduct.HoldingDetails holdingDetails = fundListProduct.new HoldingDetails();
            fundListProduct.setHoldingDetails(holdingDetails);
            FundListProduct.Performance.CumulativeTotalReturns cumulativeTotalReturns = fundListProduct.getPerformance().new CumulativeTotalReturns();
            List<FundCumulativeReturn> fundCumulativeReturnList = new ArrayList<>();
            FundCumulativeReturn fundCumulativeReturn = new FundCumulativeReturn();
            for (int i = 0; i < 8; i++) {
                fundCumulativeReturnList.add(fundCumulativeReturn);
            }
            cumulativeTotalReturns.setItems(fundCumulativeReturnList);
            FundListProduct.Performance.CalendarYearTotalReturns calendarYearTotalReturns = fundListProduct.getPerformance().new CalendarYearTotalReturns();
            List<FundCalendarYearReturn> fundCalendarYearReturnList = new ArrayList<>();
            FundCalendarYearReturn fundCalendarYearReturn =new FundCalendarYearReturn();
            for (int i = 0; i < 5; i++) {
                fundCalendarYearReturnList.add(fundCalendarYearReturn);
            }
            calendarYearTotalReturns.setItems(fundCalendarYearReturnList);
            fundListProduct.getPerformance().setCumulativeTotalReturns(cumulativeTotalReturns);
            fundListProduct.getPerformance().setCalendarYearTotalReturns(calendarYearTotalReturns);
            FundListProduct.YieldAndCredit yieldAndCredit = fundListProduct.new YieldAndCredit();
            fundListProduct.setYieldAndCredit(yieldAndCredit);

            fundListProductList.add(fundListProduct);
            return fundListProductList;
        }

        public static FundListResponse buildFundListResponse(){
            FundListResponse fundListResponse = new FundListResponse();
            fundListResponse.setSearchProductList(buildSearchProductList());
            fundListResponse.setProducts(buildFundListProduct());
            return fundListResponse;
        }

        public static List<UtProdInstm> buildUtProdInstmList(){
            List<UtProdInstm> utProdInstmList  = new ArrayList<>();
            UtProdInstm utProdInstm =  new UtProdInstm();
            UtProdInstmId utProdInstmId = new UtProdInstmId();
            utProdInstmId.setProductId(2);
            utProdInstmId.setBatchId(4);
            utProdInstm.setAllowReguCntbInd("Y");
            utProdInstm.setUtProdInstmId(utProdInstmId);
            utProdInstm.setPerformanceId("0P00007KN5");
            utProdInstm.setSymbol("540002");
            utProdInstm.setCcyProdTradeCde("HKD");
            utProdInstm.setCcyInvstCde("HKD");
            utProdInstm.setEquityStyle(1);
            utProdInstm.setFixIncomestyle(2);
            utProdInstm.setMonthEndDate(new Date());
            utProdInstmList.add(utProdInstm);
            return utProdInstmList;
        }

        public static List<UtProdInstm> buildUtProdInstmList2(){
            List<UtProdInstm> utProdInstmList  = new ArrayList<>();
            UtProdInstm utProdInstm =  new UtProdInstm();
            UtProdInstmId utProdInstmId = new UtProdInstmId();
            utProdInstmId.setProductId(2);
            utProdInstmId.setBatchId(4);
            utProdInstm.setAllowReguCntbInd("Y");
            utProdInstm.setUtProdInstmId(utProdInstmId);
            utProdInstm.setPerformanceId("id");
            utProdInstm.setSymbol("540002");
            utProdInstm.setCcyProdTradeCde("HKD");
            utProdInstm.setCcyInvstCde("HKD");
            utProdInstm.setEquityStyle(1);
            utProdInstm.setFixIncomestyle(2);
            utProdInstm.setMonthEndDate(new Date());
            utProdInstmList.add(utProdInstm);
            return utProdInstmList;
        }

        public static List<ProductKey> buildProductKeyList(){
            List<ProductKey> productKeyList = new ArrayList<>();
            ProductKey productKey = new ProductKey();
            productKey.setMarket("HK");
            productKey.setProdAltNum("U62509");
            productKey.setProdCdeAltClassCde("M");
            productKey.setProductType("UT");
            productKeyList.add(productKey);
            return productKeyList;
        }

        public static List<Criterias> buildCriteriasList(){
            List<Criterias> criteriasList = new ArrayList<>();
            Criterias criterias = new Criterias();
            criterias.setCriteriaKey("topTenHoldings");
            criterias.setCriteriaValue(true);
            criteriasList.add(criterias);
            return criteriasList;
        }

        public static List<String> buildTradableCurrencyProdUsingMap(){
            List<String> tradableCurrencyProdUsingList = new ArrayList<>();
            tradableCurrencyProdUsingList.add("HK_HBAP_UT");
            return tradableCurrencyProdUsingList;
        }
    }

}