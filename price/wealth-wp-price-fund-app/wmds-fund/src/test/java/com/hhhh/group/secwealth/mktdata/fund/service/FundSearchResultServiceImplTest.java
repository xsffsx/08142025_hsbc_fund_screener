package com.hhhh.group.secwealth.mktdata.fund.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.convertor.RequestConverter;
import com.hhhh.group.secwealth.mktdata.common.criteria.Holdings;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.*;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist.Data;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchResultRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundSearchResultResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.RedemptionFeesResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchResultProduct;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.HoldingAllocation;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.TopHoldingsSearch;
import com.hhhh.group.secwealth.mktdata.fund.converter.BaseValidateConverter;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundSearchResultDao;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json.ProductInfo;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.service.ProductKeyListWithEligibilityCheckService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.makeAccessible;

@ExtendWith(MockitoExtension.class)
class FundSearchResultServiceImplTest {

    private static final String STRING = "STRING";
    @Mock
    private BaseValidateConverter baseValidateConverter;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private Map<String, String> switchOutCaseMap;
    @Mock
    private Map<String, String> newFundBestSellerMap;
    @Mock
    private Map<String, String> newFundBestSellerCaseMap;
    @Mock
    private Map<String, String> holdingAllocationMap;
    @Mock
    private FundSearchResultDao fundSearchResultDao;
    @Mock
    private RequestConverter requestConverter;
    @Mock
    private SiteFeature siteFeature;
    @Mock
    private List<String> tradableCurrencyProdUsingMap;
    @Mock
    private ProductKeyListWithEligibilityCheckService productKeyListWithEligibilityCheckService;
    @InjectMocks
    private FundSearchResultServiceImpl underTest;

    @Nested
    class WhenExecuting {
        @Mock
        private FundSearchResultRequest request;

        @BeforeEach
        void setup() throws Exception {

            Field navUrl = underTest.getClass().getDeclaredField("limitRecord");
            navUrl.setAccessible(true);
            navUrl.set(underTest, "100");
            Field holdinglist = underTest.getClass().getDeclaredField("holdingsReturnList");
            holdinglist.setAccessible(true);
            holdinglist.set(underTest, 5);

            Mockito.when(request.getStartDetail()).thenReturn(10);
            when(request.getSiteKey()).thenReturn("hk_hbap");
            when(newFundBestSellerMap.get(any())).thenReturn("DB");
            Mockito.when(request.getEntityTimezone()).thenReturn("GMT");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getNumberOfRecords()).thenReturn(10);
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getLocale()).thenReturn("en");
            SearchProduct searchProduct = new SearchProduct();
            searchProduct.setSearchObject(new SearchableObject());
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(any(), any(), any(), any(), any(), any(), any())).thenReturn(searchProduct);
            Mockito.when(holdingAllocationMap.get(anyString())).thenReturn("ASSETALLOC");

            Mockito.when(request.getProductType()).thenReturn("UT");
            Mockito.when(request.getHoldings()).thenReturn(DataBuilder.buildHoldingList());
            when(switchOutCaseMap.get(any())).thenReturn("DEFAULT_DB");
            String utprod = "{\"utProdInstmId\":{\"productId\":1000047826,\"batchId\":162033},\"market\":\"HK\",\"mStarID\":\"F0GBR061M5\",\"productType\":\"UT\",\"productSubTypeCode\":\"GB\",\"symbol\":\"U61749\",\"performanceId\":\"0P00001CCH\",\"fundserviceId\":null,\"prodName\":\"Schroder ISF Global Equity Yield (Class A-MDIST Cash)\",\"prodPllName\":\"施羅德環球基金系列 - 環球收益股票 (A類-每月派息現金)\",\"prodSllName\":\"施罗德环球基金系列 - 环球收益股票 (A类-每月派息现金)\",\"familyCode\":\"SCHRO\",\"familyName1\":\"Schroder Investment Management (HK) Ltd\",\"familyName2\":\"施羅德投資管理(香港)有限公司\",\"familyName3\":\"施罗德投资管理（香港）有限公司\",\"categoryCode\":\"GB\",\"categoryName1\":\"Global Equity - Broad\",\"categoryName2\":\"環球股票\",\"categoryName3\":\"环球股票\",\"categoryLevel0Code\":\"EQUT\",\"categoryLevel0SequencenNum\":10,\"categoryLevel0Name1\":\"Equity funds\",\"categoryLevel0Name2\":\"股票基金\",\"categoryLevel0Name3\":\"股票基金\",\"categoryLevel1Code\":\"EDDM\",\"categoryLevel1SequencenNum\":10,\"categoryLevel1Name1\":\"Equity Developed Market\",\"categoryLevel1Name2\":\"已發展市場股票\",\"categoryLevel1Name3\":\"已发展市场股票\",\"ccyProdTradeCde\":\"USD\",\"fundRtainMinAmt\":null,\"fundSwInMinAmt\":1000,\"allowSellMipProdInd\":\"Y\",\"currency\":\"USD\",\"riskLvlCde\":\"4\",\"invstIncrmMinAmt\":null,\"classType\":null,\"dayEndNAV\":121.2539,\"changeAmountNAV\":1.0427,\"assetsUnderManagement\":268639386,\"totalNetAsset\":30946422,\"ratingOverall\":3,\"mer\":null,\"yield1Yr\":3.07958,\"inceptionDate\":1122566400000,\"turnoverRatio\":null,\"stdDev3Yr\":19.96,\"equityStyle\":1,\"fixIncomestyle\":null,\"taxAdjustedRating\":null,\"averageCreditQuality\":null,\"rank1Yr\":1,\"returnSinceInception\":4.49643,\"returnYTD\":8.44193,\"year1\":-7.16712,\"year2\":18.52558,\"year3\":-6.88932,\"year4\":15.56548,\"year5\":-11.72995,\"beta1\":0.99,\"stdDev1\":16.257,\"alpha1\":10.06,\"sharpeRatio1\":1.314,\"rSquared1\":79.47,\"beta3\":1.17,\"alpha3\":5.21,\"sharpeRatio3\":0.65,\"rSquared3\":76.59,\"beta5\":1.18,\"stdDev5\":20.56,\"alpha5\":-1.99,\"sharpeRatio5\":0.137,\"rSquared5\":79.96,\"beta10\":1.11,\"stdDev10\":16.167,\"alpha10\":-0.68,\"sharpeRatio10\":0.312,\"rSquared10\":76.27,\"minInitInvst\":1000,\"minSubqInvst\":1000,\"hhhhMinInitInvst\":1000,\"hhhhMinSubqInvst\":1000,\"purchaseCcy\":\"USD\",\"rrspEligibility\":null,\"updatedBy\":\"system\",\"updatedOn\":1697044209145,\"asOfDate\":1696953600000,\"mean5Yr\":0.388,\"trackingError1Yr\":null,\"risk3Yr\":\"5\",\"marketPriceChange\":null,\"distributionFrequency\":\"Monthly\",\"rank3Yr\":1,\"rank5Yr\":4,\"rank10Yr\":2,\"minimumIRA\":null,\"return1day\":0.86739,\"return1mth\":-2.50962,\"return3mth\":-2.45148,\"return6mth\":-0.84083,\"return1yr\":28.43776,\"return3yr\":14.02069,\"return5yr\":2.60254,\"return10yr\":5.06902,\"productShortPrimaryLanguageName\":null,\"payCashDivInd\":\"Y\",\"productShortSecondLanguageName\":null,\"productShortName\":null,\"ccyAsofRep\":\"USD\",\"annualReportOngoingCharge\":null,\"actualManagementFee\":1.5,\"allowSwOutProdInd\":\"Y\",\"allowSwInProdInd\":\"N\",\"allowTradeProdInd\":\"Y\",\"prodTaxFreeWrapActStaCde\":null,\"ratingDate\":1696003200000,\"monthEndDate\":1696003200000,\"endDateYearRisk\":1696003200000,\"endDateRiskLvlCde\":1696003200000,\"familySequencenNum\":191,\"categorySequencenNum\":5,\"averageCreditQualityNum\":null,\"distributionFrequencyNum\":96,\"expenseRatio\":1.84,\"loanProdOdMrgnPct\":null,\"chrgInitSalesPct\":3,\"annMgmtFeePct\":0,\"priShareClassInd\":null,\"topPerformersInd\":null,\"fundId\":\"FSGBR06GIY\",\"averageCreditQualityName\":null,\"closingPrcCcy\":\"USD\",\"mstarCcyCde\":\"USD\",\"creditQualityBreakdownAsOfDate\":null,\"ccyInvstCde\":\"USD\",\"yieldToMaturity\":null,\"effectiveDuration\":null,\"currentYield\":null,\"return1mthDaily\":-1.38141,\"return3mthDaily\":-1.50082,\"return6mthDaily\":-2.09875,\"returnytdDaily\":8.43728,\"return1yrDaily\":28.05796,\"return3yrDaily\":12.16369,\"return5yrDaily\":3.41823,\"return10yrDaily\":4.92331,\"returnSinceInceptionDaily\":4.48858,\"prodTopSellRankNum\":null,\"topSellProdIndex\":\"N\",\"prodLaunchDate\":1151251200000,\"investmentRegionCode\":\"GL\",\"investmentRegionName1\":\"Global\",\"investmentRegionName2\":\"環球\",\"investmentRegionName3\":\"环球\",\"investmentRegionSequencenNum\":1,\"listProdCode\":null,\"listProdType\":null,\"numberOfBondHoldings\":0,\"numberOfStockHoldings\":49,\"holdingAllocationPortfolioDate\":1696003200000,\"kiidOngoingCharge\":1.84,\"kiidOngoingChargeDate\":1675872000000,\"fundShreClsName\":\"CLASS A-MDIST CASH\",\"fundShreClsNamePriLang\":\"A類-每月派息現金\",\"fundShreClsNameSecLang\":\"A类-每月派息现金\",\"fundClassCde\":\"WIC\",\"amcmAuthorizeIndicator\":\"Y\",\"nextDealDate\":1694707200000,\"indexId\":\"FOUSA06OZ9\",\"indexName\":\"MSCI World High Dividend Yield NR USD\",\"surveyedFundNetAssetsDate\":1696953600000,\"weekRangeLowDate\":1665590400000,\"weekRangeHighDate\":1690387200000,\"riskFreeRateName\":\"USTREAS T-Bill Auction Ave 3 Mon\",\"relativeRiskMeasuresIndexName\":\"MSCI World High Dividend Yield NR USD\",\"prodStatCde\":\"A\",\"allowBuyProdInd\":\"N\",\"allowSellProdInd\":\"Y\",\"annualReportDate\":1672416000000,\"restrOnlScribInd\":null,\"marketPrice\":0.86739,\"piFundInd\":null,\"deAuthFundInd\":null,\"bidclosingPrc\":null,\"bidPriceDate\":1696953600000,\"askclosingPrc\":null,\"undlIndexCde\":null,\"undlIndexName\":null,\"undlIndexPllName\":null,\"undlIndexSllName\":null,\"popularRankNum\":null,\"esgInd\":null,\"cutOffTime\":4102360200000,\"inDealBefDate\":1694707200000,\"inScribStlBefDate\":1694966400000,\"inRedempStlBefDate\":1695139200000,\"inDealAftDate\":1694966400000,\"inScribStlAftDate\":1695052800000,\"inRedempStlAftDate\":1695225600000,\"interfaceDate\":1694707200000,\"siFundCategoryCode\":null,\"gbaAcctTrdb\":\"N\",\"gnrAcctTrdb\":\"Y\",\"shortLstRPQLvlNum\":null,\"fundName\":null,\"currencyId\":null,\"allowReguCntbInd\":null}";
            UtProdInstm ut = new ObjectMapper().readValue(utprod, UtProdInstm.class);
            List<UtProdInstm> list = new ArrayList<>();
            list.add(ut);

            Mockito.when(fundSearchResultDao.searchFund(any(), any(), any(), any(), anyString(), any(), any(), anyBoolean(), anyInt()))
                    .thenReturn(list);

        }
        @Test
         void test_WhenExecuting() throws Exception {
            Mockito.when(request.getDetailedCriterias()).thenReturn(DataBuilder.buildSearchCriteria());
            FundSearchResultResponse response = null;
           // when(fundSearchResultDao.appendSqlForCoreSwitchOut(any(),any(),any())).thenReturn(new ArrayList<Integer>(){{add(3);}});
            when(switchOutCaseMap.get(any())).thenReturn("PREDSRCH");
            response = (FundSearchResultResponse) underTest.execute(request);
            Assert.assertNotNull(response);

        }

        @Test
         void test_WhenExecuting1() throws Exception {
            Mockito.when(request.getDetailedCriterias()).thenReturn(DataBuilder.buildSearchCriteria2());
            FundSearchResultResponse response = (FundSearchResultResponse) underTest.execute(request);
            Assert.assertNotNull(response);

        }

        @Test
         void test_WhenExecuting2() throws Exception {
            Mockito.when(request.getDetailedCriterias()).thenReturn(DataBuilder.buildSearchCriteria3());
            FundSearchResultResponse response = (FundSearchResultResponse) underTest.execute(request);
            Assert.assertNotNull(response);

        }



    }


    @Nested
    class WhenExecuting1 {
        @Mock
        private FundSearchResultRequest request;
        @Test
         void checkhhhhRiskLevelRatingCriterionAvailable() throws Exception {
            Mockito.when(request.getDetailedCriterias()).thenReturn(DataBuilder.buildSearchCriteria());
            Method method = findMethodWithAccess(underTest, "checkhhhhRiskLevelRatingCriterionAvailable", FundSearchResultRequest.class);
            String.valueOf(method.invoke(underTest, request));
            Assert.assertNotNull(method);
        }

        @Test
         void removeValIsNullAndDistinct() throws Exception {
            Method method = findMethodWithAccess(underTest, "removeValIsNullAndDistinct", List.class);
            String.valueOf(method.invoke(underTest, new ArrayList<String>(){{add("1");}}));
            Assert.assertNotNull(method);
        }
        @Test
         void sortHolding_sicHldgs() throws Exception {
            Method method = findMethodWithAccess(underTest, "sortHolding_sicHldgs", List.class);
            UtCatAsetAlloc utCatAsetAlloc = new UtCatAsetAlloc();
            utCatAsetAlloc.setFundAllocation(new BigDecimal(1));
            utCatAsetAlloc.setCategoryAllocation(new BigDecimal(1));
            UtCatAsetAlloc utCatAsetAllocTwo = new UtCatAsetAlloc();
            utCatAsetAllocTwo.setFundAllocation(new BigDecimal(2));
            utCatAsetAllocTwo.setCategoryAllocation(new BigDecimal(2));
            String.valueOf(method.invoke(underTest, new ArrayList<UtCatAsetAlloc>(){{add(utCatAsetAlloc);
            add(utCatAsetAllocTwo);}}));
            Method methodtwo = findMethodWithAccess(underTest, "sortHolding_geoHldgs", List.class);
            assertNotNull(methodtwo);
            String.valueOf(methodtwo.invoke(underTest, new ArrayList<UtCatAsetAlloc>(){{add(utCatAsetAlloc);
                add(utCatAsetAllocTwo);}}));
            Assert.assertNotNull(method);
        }
        @Test
         void setTopHoldingsList() throws Exception {
            Field holdinglist = underTest.getClass().getDeclaredField("holdingsReturnList");
            holdinglist.setAccessible(true);
            holdinglist.set(underTest, 5);
            Method method = findMethodWithAccess(underTest, "setTopHoldingsList", UtProdInstm.class,Map.class,List.class,Map.class,Map.class,Map.class,String.class);
            UtProdInstm utProdInstm = new UtProdInstm();
            utProdInstm.setPerformanceId("00001");
            Map<String,List<UtHoldings>>fundHoldingMap = new HashMap<>();
            UtHoldings utHoldings = new UtHoldings();
            UtHoldingsId utHoldingsId = new UtHoldingsId();
            utHoldingsId.setHoldingId(12);
            utHoldingsId.setHolderName("ew");
            utHoldings.setUtHoldingsId(utHoldingsId);
            fundHoldingMap.put("00001",new ArrayList<UtHoldings>(){{add(utHoldings);}});
            String.valueOf(method.invoke(underTest,utProdInstm,fundHoldingMap,new ArrayList<TopHoldingsSearch>(){{add(new TopHoldingsSearch());}},new HashMap<String,Boolean>(), new HashMap<String, Integer>(),new HashMap<String,Boolean>(),"UT"));
            Assert.assertNotNull(method);
        }

        @Test
         void setAssetAllocation() throws Exception {
            Method method = findMethodWithAccess(underTest, "setAssetAllocation", UtProdInstm.class,Map.class, FundSearchResultProduct.Holdings.AssetAllocations.class,String.class);
            UtProdInstm utProdInstm = new UtProdInstm();
            utProdInstm.setPerformanceId("00001");
            Map<String,List<UTHoldingAlloc>>fundHoldingMap = new HashMap<>();
            UTHoldingAlloc utHoldings = new UTHoldingAlloc();
            UTHoldingAllocId uTHoldingAllocId = new UTHoldingAllocId();
            uTHoldingAllocId.setHoldingAllocClassType("UT");
            utHoldings.setUtHoldingAllocId(uTHoldingAllocId);
            fundHoldingMap.put("00001",new ArrayList<UTHoldingAlloc>(){{add(utHoldings);}});
            String.valueOf(method.invoke(underTest, utProdInstm, fundHoldingMap,new FundSearchResultProduct().new Holdings().new AssetAllocations(),"UT"));
            Assert.assertNotNull(method);
        }
        @Test
         void getProductFromPredSearch() throws Exception {
            Method method = findMethodWithAccess(underTest, "getProductFromPredSearch", String.class,String.class,String[].class,Map.class);
            String.valueOf(method.invoke(underTest, "ut", "hk",null,new HashMap<>()));
            Assert.assertNotNull(method);
        }

        @Test
         void getProdAltNumFromWebservicce() throws Exception {
            Method method = findMethodWithAccess(underTest, "getProdAltNumFromWebservicce", List.class);
            String.valueOf(method.invoke(underTest,new ArrayList<ProductInfo>(){{add(new ProductInfo());}}));
            Assert.assertNotNull(method);
        }

        @Test
        void setAnnualizedReturns() throws Exception {
            Method method = findMethodWithAccess(underTest, "setAnnualizedReturns", UtProdInstm.class, FundSearchResultProduct.Performance.AnnualizedReturns.class,String.class);
            String.valueOf(method.invoke(underTest,new UtProdInstm(),new FundSearchResultProduct().new Performance().new AnnualizedReturns(),"daily"));
            Assert.assertNotNull(method);
        }

        @Test
         void setHeader() throws Exception {
            Method method = findMethodWithAccess(underTest, "setHeader",int.class, UtProdInstm.class, FundSearchResultProduct.Header.class);
            String.valueOf(method.invoke(underTest,1,new UtProdInstm(),new FundSearchResultProduct().new Header()));
            String.valueOf(method.invoke(underTest,2,new UtProdInstm(),new FundSearchResultProduct().new Header()));
            String.valueOf(method.invoke(underTest,3,new UtProdInstm(),new FundSearchResultProduct().new Header()));
            Assert.assertNotNull(method);
        }
        @Test
         void setOthers() throws Exception {
            Method method = findMethodWithAccess(underTest, "setOthers",List.class,int.class,BigDecimal.class,BigDecimal.class);
            String.valueOf(method.invoke(underTest,new ArrayList<HoldingAllocation>(){{add(new HoldingAllocation());}},1,new BigDecimal(20),new BigDecimal(20)));
            Assert.assertNotNull(method);
        }


    }


    private static class DataBuilder {
        public static List<UtProdInstm> buildUtProdInstmList(){
            List<UtProdInstm> utProdInstmList  = new ArrayList<>();
            UtProdInstm utProdInstm =  new UtProdInstm();
            UtProdInstmId utProdInstmId = new UtProdInstmId();
            utProdInstmId.setProductId(2);
            utProdInstmId.setBatchId(4);
            utProdInstm.setMarket("HK");
            utProdInstm.setProductType("UT");
            utProdInstm.setAllowReguCntbInd("Y");
            utProdInstm.setUtProdInstmId(utProdInstmId);
            utProdInstm.setPerformanceId("0P00007KN5");
            utProdInstm.setSymbol("540002");
            utProdInstm.setCcyProdTradeCde("HKD");
            utProdInstm.setCcyInvstCde("HKD");
            utProdInstmList.add(utProdInstm);
            return utProdInstmList;
        }

        public static SearchProduct buildSearchProduct(){
            SearchProduct searchProduct = new SearchProduct();
            searchProduct.setExternalKey("0P0001BFG7");
            SearchableObject searchObject = new SearchableObject();
            searchObject.setSymbol("540002");
            searchProduct.setSearchObject(searchObject);
            return searchProduct;
        }

        public static List<SearchCriteria> buildSearchCriteria(){
            List<SearchCriteria> searchCriteriaList = new ArrayList<>();
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("switchOutFund");
            searchCriteria.setCriteriaValue("ABCD:A:C:D");
            searchCriteriaList.add(searchCriteria);
            SearchCriteria searchCriteria1 = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("hhhh_BEST_SELLER");
            searchCriteria1.setCriteriaValue("Y");
            searchCriteriaList.add(searchCriteria1);
            SearchCriteria searchCriteria2 = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("HRR");
            searchCriteria2.setCriteriaValue("ABCD");
            searchCriteriaList.add(searchCriteria2);
            return searchCriteriaList;
        }

        public static List<SearchCriteria> buildSearchCriteria2(){
            List<SearchCriteria> searchCriteriaList = new ArrayList<>();
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("switchOutFund");
            searchCriteria.setCriteriaValue("ABCD:A:C:D");
            searchCriteriaList.add(searchCriteria);
            SearchCriteria searchCriteria1 = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("hhhh_NEW_FUND");
            searchCriteria1.setCriteriaValue("Y");
            searchCriteriaList.add(searchCriteria1);
            SearchCriteria searchCriteria2 = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("HRR");
            searchCriteria2.setCriteriaValue("ABCD");
            searchCriteriaList.add(searchCriteria2);
            SearchCriteria searchCriteria3 = new SearchCriteria();
            searchCriteria3.setOperator("eq");
            searchCriteria3.setCriteriaKey("hhhh_RETIREMENT_FUND");
            searchCriteria3.setCriteriaValue("Y");
            searchCriteriaList.add(searchCriteria3);
            return searchCriteriaList;
        }

        public static List<SearchCriteria> buildSearchCriteria3(){
            List<SearchCriteria> searchCriteriaList = new ArrayList<>();
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("switchOutFund");
            searchCriteria.setCriteriaValue("ABCD:A:C:D");
            searchCriteriaList.add(searchCriteria);
            SearchCriteria searchCriteria1 = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("hhhh_TOP5_PERFORMERS");
            searchCriteria1.setCriteriaValue("Y");
            searchCriteriaList.add(searchCriteria1);
            SearchCriteria searchCriteria2 = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("HRR");
            searchCriteria2.setCriteriaValue("ABCD");
            searchCriteriaList.add(searchCriteria2);
            return searchCriteriaList;
        }



        public static List<Holdings> buildHoldingList(){
            List<Holdings> holdings = new ArrayList<>();
            Holdings holding = new Holdings();
            holding.setCriteriaKey("PCDI");
            holding.setCriteriaValue(true);
            holding.setOthers(true);
            holding.setTop(2);
            holdings.add(holding);
            return holdings;
        }
    }

    public static Method findMethodWithAccess(Object target, String name, Class<?>... paramTypes) {
        Method method = findMethod(target.getClass(), name, paramTypes);
        makeAccessible(Objects.requireNonNull(method));
        return method;
    }




}