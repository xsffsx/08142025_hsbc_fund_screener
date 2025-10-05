package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstmId;
import com.hhhh.group.secwealth.mktdata.common.service.ServiceExecutor;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareIndexRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundCompareResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareRisk;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.performancereturn.PerformanceReturn;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundCompareDao;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundCompareServiceImplTest {


    private static final String STRING = "STRING";
    @Mock
    private ServiceExecutor fundCompareServiceExecutor;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private FundCompareDao fundCompareDao;
    @InjectMocks
    private FundCompareServiceImpl underTest;
    @Mock
    private List<String> tradableCurrencyProdUsingMap;
    @Nested
    class WhenExecuting {
        @Mock
        private FundCompareRequest request;

        @BeforeEach
        void setup() throws Exception {
            FundCompareResponse fundCompareResponse =new FundCompareResponse ();
            List<SearchProduct> searchProductList = new ArrayList<>();
            SearchProduct searchProduct = new SearchProduct();
            SearchableObject searchableObject = new SearchableObject();
            searchableObject.setSymbol("U50002");
            searchProduct.setExternalKey("U50002");
            searchProduct.setSearchObject(searchableObject);
            searchProductList.add(searchProduct);
            fundCompareResponse.setSearchProductList(searchProductList);

            List<FundCompareProduct> fundCompareProductList = new ArrayList<>();
            FundCompareProduct fundCompareProduct = new FundCompareProduct();
            fundCompareProduct.setProdAltNumXCode("U50002");
            fundCompareProduct.setSymbol("U50002");
            fundCompareProductList.add(fundCompareProduct);
            fundCompareResponse.setProducts(fundCompareProductList);
            FundCompareProduct.Performance performance = fundCompareProduct.new Performance();
            performance.setAnnualizedReturns(null);
            performance.setAnnualizedReturns(null);
            FundCompareProduct.Profile profile =  fundCompareProduct.new Profile();
            profile.setDistributionFrequency("daily");
            FundCompareProduct.Summary summary =  fundCompareProduct.new Summary();
            FundCompareProduct.Header header =  fundCompareProduct.new Header();
            FundCompareProduct.Rating rating = fundCompareProduct.new Rating();

            FundCompareProduct.Performance.AnnualizedReturns annualizedReturns = performance.new AnnualizedReturns();
            FundCompareProduct.Performance.CalendarReturns calendarReturns = performance.new CalendarReturns();

            FundCompareProduct.PurchaseInfo purchaseInfo = fundCompareProduct.new PurchaseInfo();

            summary.setCategoryName("43");
            performance.setAnnualizedReturns(annualizedReturns);
            performance.setCalendarReturns(calendarReturns);
            fundCompareProduct.setPerformance(performance);
            fundCompareProduct.setProfile(profile);
            fundCompareProduct.setSummary(summary);
            fundCompareProduct.setHeader(header);
            fundCompareProduct.setPurchaseInfo(purchaseInfo);
            fundCompareProduct.setRating(rating);
            fundCompareProduct.setProdAltNumXCode("U50002");
            fundCompareProductList.add(fundCompareProduct);
            fundCompareResponse.setProducts(fundCompareProductList);

            List<FundCompareRisk> risk = new ArrayList<>();
            FundCompareRisk fundCompareRisk = new FundCompareRisk();
            FundCompareRisk.YearRisk yearRisk = fundCompareRisk.new YearRisk();
            fundCompareRisk.setYearRisk(yearRisk);

            FundCompareRisk fundCompareRisk1 = new FundCompareRisk();
            FundCompareRisk.YearRisk yearRisk1 = fundCompareRisk1.new YearRisk();
            fundCompareRisk1.setYearRisk(yearRisk1);

            FundCompareRisk fundCompareRisk2 = new FundCompareRisk();
            FundCompareRisk.YearRisk yearRisk2 = fundCompareRisk2.new YearRisk();
            fundCompareRisk2.setYearRisk(yearRisk2);

            FundCompareRisk fundCompareRisk3 = new FundCompareRisk();
            FundCompareRisk.YearRisk yearRisk3 = fundCompareRisk3.new YearRisk();
            fundCompareRisk3.setYearRisk(yearRisk3);

            risk.add(fundCompareRisk);
            risk.add(fundCompareRisk1);
            risk.add(fundCompareRisk2);
            risk.add(fundCompareRisk3);
            fundCompareProduct.setRisk(risk);


            List<UtProdInstm> li = new ArrayList<>();
            UtProdInstm utProdInstm =  new UtProdInstm();
            UtProdInstmId utProdInstmId = new UtProdInstmId();
            utProdInstmId.setProductId(51001);
            utProdInstmId.setBatchId(4);
            utProdInstm.setCcyProdTradeCde("HKD");
            utProdInstm.setSymbol("U50002");
            utProdInstm.setAllowReguCntbInd("Y");
            utProdInstm.setUtProdInstmId(utProdInstmId);
            li.add(utProdInstm);
             List<ProductKey> productKeys = new ArrayList<>();
            ProductKey productKey =  new ProductKey();
            productKey.setProdAltNum("U50002");
            productKey.setProductType("M");
            productKey.setMarket("HK");
            productKey.setProductType("UT");
            productKeys.add(productKey);
            Mockito.when(request.getProductKeys()).thenReturn(productKeys);
            Mockito.when(fundCompareDao.searchForCompare(any(FundCompareRequest.class))).thenReturn(li);
            Mockito.when(fundCompareServiceExecutor.execute(any(FundCompareRequest.class))).thenReturn(fundCompareResponse);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getLocale()).thenReturn("en_US");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
        //    Mockito.when(tradableCurrencyProdUsingMap.contains(any(String.class))).thenReturn(true);



        }
        @Test
         void test_WhenExecuting() throws Exception {
            FundCompareResponse fundCompareResponse =(FundCompareResponse) underTest.execute(request);
            Assert.assertNotNull(fundCompareResponse.getProducts());
        }

    }
}