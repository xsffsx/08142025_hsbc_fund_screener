package com.hhhh.group.secwealth.mktdata.fund.service;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstmId;
import com.hhhh.group.secwealth.mktdata.common.service.ServiceExecutor;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundQuoteSummaryRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundQuoteSummaryResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundQuoteSummaryDao;
import org.junit.Assert;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundQuoteSummaryServiceImplTest {


    private static final String S_I_T_E__F_E_A_T_U_R_E__C_U_R_R_E_N_C_Y = "S_I_T_E__F_E_A_T_U_R_E__C_U_R_R_E_N_C_Y";
    private static final String STRING = "STRING";
    @Mock
    private ServiceExecutor quoteSummaryServiceExecutor;
    @Mock
    private FundQuoteSummaryDao fundQuoteSummaryDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private SiteFeature siteFeature;
    @Mock
    private List<String> tradableCurrencyProdUsingMap;
    @InjectMocks
    private FundQuoteSummaryServiceImpl underTest;

    @Nested
    class WhenExecuting {
        @Mock
        private FundQuoteSummaryRequest request;

        @BeforeEach
        void setup() throws Exception {

            Map<Integer, List<String>> utProdChanl = new HashMap<>();
            List<String> chanl = new ArrayList<>();
            chanl.add("SI_I");
            utProdChanl.put(1,chanl);
            List<UtProdInstm> utProdInstmList = new ArrayList<>();
            UtProdInstm utProdInstm =  new UtProdInstm();
            UtProdInstmId utProdInstmId = new UtProdInstmId();
            utProdInstmId.setProductId(2024);
            utProdInstm.setUtProdInstmId(utProdInstmId);
            utProdInstm.setClosingPrcCcy("hkd");
            utProdInstm.setMinSubqInvst(new BigDecimal(2));
            utProdInstm.setMinInitInvst(new BigDecimal(2));

            utProdInstmList.add(utProdInstm);
            FundQuoteSummaryResponse response  = new FundQuoteSummaryResponse();
            Summary summary = new Summary();
            summary.setBid(new BigDecimal(12));
            summary.setOffer(new BigDecimal(14));
            summary.setWeekRangeHigh(new BigDecimal(22));
            summary.setWeekRangeLow(new BigDecimal(81));
            List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
            ProdAltNumSeg prodAltNumSeg =  new ProdAltNumSeg();
            prodAltNumSeg.setProdAltNum("U50002");
            prodAltNumSeg.setProdCdeAltClassCde("M");
            prodAltNumSegs.add(prodAltNumSeg);
            summary.setProdAltNumSegs(prodAltNumSegs);
            Summary.CalendarYearTotalReturns calendarYearTotalReturns = summary.new CalendarYearTotalReturns();
            Summary.CumulativeTotalReturns cumulativeTotalReturns = summary.new CumulativeTotalReturns();
            Summary.Profile profile = summary.new Profile();

            Summary.HoldingDetails holdingDetails = summary.new HoldingDetails();
            Summary.ToNewInvestors toNewInvestors = summary.new ToNewInvestors();
            Summary.InvestmentStrategy investmentStrategy = summary.new InvestmentStrategy();
            Summary.YieldAndCredit yieldAndCredit = summary.new YieldAndCredit();
            Summary.Rating rating = summary.new Rating();
            Summary.FeesAndExpenses feesAndExpenses = summary.new FeesAndExpenses();
            summary.setHoldingDetails(holdingDetails);
            summary.setToNewInvestors(toNewInvestors);
            summary.setInvestmentStrategy(investmentStrategy);
            summary.setYieldAndCredit(yieldAndCredit);
            summary.setRating(rating);
            summary.setFeesAndExpenses(feesAndExpenses);


            summary.setProfile(profile);
            summary.setCalendarYearTotalReturns(calendarYearTotalReturns);
            summary.setCumulativeTotalReturns(cumulativeTotalReturns);

            response.setSummary(summary);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getLocale()).thenReturn("en_US");

            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("UT");


            Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            Mockito.when(quoteSummaryServiceExecutor.execute(any(FundQuoteSummaryRequest.class))).thenReturn(response);
            Mockito.when(fundQuoteSummaryDao.searchProfile(any(FundQuoteSummaryRequest.class))).thenReturn(utProdInstmList);

            Mockito.when(tradableCurrencyProdUsingMap.contains(any(String.class))).thenReturn(true);


        }
        @Test
         void test_WhenExecuting() throws Exception {
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(1);
            FundQuoteSummaryResponse quoteSummaryResponse =(FundQuoteSummaryResponse) underTest.execute(request);
            Assert.assertNotNull(quoteSummaryResponse.getSummary());
        }
        @Test
         void test_WhenExecuting2() throws Exception {
            Mockito.when(localeMappingUtil.getNameByIndex(anyString())).thenReturn(2);
            FundQuoteSummaryResponse quoteSummaryResponse =(FundQuoteSummaryResponse) underTest.execute(request);
            Assert.assertNotNull(quoteSummaryResponse.getSummary());
        }

        @Test
         void test_WhenExecuting3() throws Exception {
            FundQuoteSummaryResponse quoteSummaryResponse =(FundQuoteSummaryResponse) underTest.execute(request);
            Assert.assertNotNull(quoteSummaryResponse.getSummary());
        }

        @Test
        void test_WhenExecuting4() throws Exception {
            List<String> list = new ArrayList<>();
            list.add("s1");
            list.add("s2");
            list.add("s3");
            list.add("s4");
            Map<Integer, List<String>> map = new HashMap<>();
            map.put(2024,list);
            Mockito.when(fundQuoteSummaryDao.searchChanlFund(anyString())).thenReturn(map);
            FundQuoteSummaryResponse quoteSummaryResponse =(FundQuoteSummaryResponse) underTest.execute(request);
            Assert.assertNotNull(quoteSummaryResponse.getSummary());
        }

        @Test
        void test_WhenExecuting5() throws Exception {
            List<String> list = new ArrayList<>();
            list.add("s1");
            list.add("s2");
            list.add("s3");
            list.add("");
            Map<Integer, List<String>> map = new HashMap<>();
            map.put(2024,list);
            Mockito.when(fundQuoteSummaryDao.searchChanlFund(anyString())).thenReturn(map);
            FundQuoteSummaryResponse quoteSummaryResponse =(FundQuoteSummaryResponse) underTest.execute(request);
            Assert.assertNotNull(quoteSummaryResponse.getSummary());
        }

    }
}