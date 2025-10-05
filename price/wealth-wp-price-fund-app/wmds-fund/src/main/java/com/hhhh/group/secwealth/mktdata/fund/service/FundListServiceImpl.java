
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.hhhh.group.secwealth.mktdata.fund.util.FundSearchResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtHoldings;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.service.AbstractService;
import com.hhhh.group.secwealth.mktdata.common.service.ServiceExecutor;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundListRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundListResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundCalendarYearReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundCumulativeReturn;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.AssetAllocations;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.BondRegionalExposures;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.GlobalBondSectors;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.GlobalStockSectors;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Header;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.HoldingDetails;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.InvestmentStrategy;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Performance;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Performance.CalendarYearTotalReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Performance.CumulativeTotalReturns;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Profile;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.PurchaseInfo;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Rating;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.RegionalExposures;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.Summary;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.TopTenHoldings;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct.YieldAndCredit;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListRisk;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListRisk.YearRisk;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.HoldingAllocation;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.TopHoldingsSearch;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundListDao;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;

@Service("fundListService")
public class FundListServiceImpl extends AbstractService {

    @Autowired
    @Qualifier("fundListServiceExecutor")
    private ServiceExecutor fundListServiceExecutor;

    @Autowired
    @Qualifier("fundListDao")
    private FundListDao fundListDao;

    @Autowired
    @Qualifier("localeMappingUtil")
    protected LocaleMappingUtil localeMappingUtil;

    @Resource(name = "tradableCurrencyProdUsingMap")
    private List<String> tradableCurrencyProdUsingMap;

    @Override
    public Object execute(final Object object) throws Exception {

        // Create ThreadPool
        // ExecutorService pool =
        // this.getThreadPool(this.fundSearchResultService,
        // this.fundCompareService);
        // AsyncCallable c1 = new AsyncCallable(this.fundSearchResultService,
        // request);
        // AsyncCallable c2 = new AsyncCallable(this.fundSearchResultService,
        // request);
        // Future<Object> f1 = pool.submit(c1);
        // Future<Object> f2 = pool.submit(c2);
        // LogUtil.debugBeanToJson(FundListServiceImpl.class, "f1 is done",
        // f1.get());
        // LogUtil.debugBeanToJson(FundListServiceImpl.class, "f2 is done",
        // f2.get());
        // Close ThreadPool
        // pool.shutdown();

        FundListRequest request = (FundListRequest) object;
        // get data from Mstar API
        FundListResponse fundListResponse = (FundListResponse) this.fundListServiceExecutor.execute(request);

        // get product List from DB
        List<UtProdInstm> utProdInstmList = this.fundListDao.searchProductList(request);
        // get topTenHolding from DB
        Map<String, List<UtHoldings>> fundHoldingMap = this.fundListDao.searchTopTenHldgMap(request, "topTenHoldings");
        // get AssetAllocation from DB
        Map<String, List<UTHoldingAlloc>> holdingAllocMap = this.fundListDao.searchHoldingAllocation(request);

        Map<String, String> headers = request.getHeaders();
        String cmbInd = headers.get(CommonConstants.REQUEST_HEADER_GBGF);
        String channelRestrictCode = FundSearchResultUtil.buildCmbFundListRequestChannelRestrictCode(request, cmbInd);

        Map<Integer, List<String>> utProdChanl = this.fundListDao.searchChanlFund(channelRestrictCode);

        int index =
            this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());

        List<ProductKey> ProductKeys = request.getProductKeys();
        Map<String, String> currencyProdUsingKeyMap = new HashMap<String, String>();
        for (ProductKey productKey : ProductKeys) {
            String currencyProdUsingKey =
                StringUtil.combineWithUnderline(request.getCountryCode(), request.getGroupMember(), productKey.getProductType());
            currencyProdUsingKeyMap.put(productKey.getProdAltNum(), currencyProdUsingKey);
        }
        // merge Mstar and DB data
        mergeResponseFromeDB(fundListResponse, utProdInstmList, utProdChanl, holdingAllocMap, fundHoldingMap, index,
            currencyProdUsingKeyMap, ProductKeys, request.getSiteKey());
        return fundListResponse;
    }

    private void mergeResponseFromeDB(final FundListResponse fundListResponse, final List<UtProdInstm> utProdInstmList,
        final Map<Integer, List<String>> utProdChanl, final Map<String, List<UTHoldingAlloc>> holdingAllocMap,
        final Map<String, List<UtHoldings>> fundHoldingMap, final Integer index, final Map<String, String> currencyProdUsingKeyMap,
        final List<ProductKey> ProductKeyList, final String site) throws Exception {

        if (null != fundListResponse && ListUtil.isValid(utProdInstmList)) {
            fundListResponse.setTotalNumberOfRecords(utProdInstmList.size());
            List<FundListProduct> products = fundListResponse.getProducts();
            List<SearchProduct> list = (List<SearchProduct>) fundListResponse.getSearchProductList();
            Set<String> mstarPerformanceId = new HashSet<String>();
            Set<String> dbPerformanceId = new HashSet<String>();
            List<FundListProduct> notReturnProducts = new ArrayList<FundListProduct>();
            if (ListUtil.isValid(products)) {
                for (FundListProduct fundListProduct : products) {
                    mstarPerformanceId.add(fundListProduct.getProdAltNumXCode());
                    Header header = fundListProduct.getHeader();
                    Summary summary = fundListProduct.getSummary();
                    Profile profile = fundListProduct.getProfile();
                    Performance performance = fundListProduct.getPerformance();
                    InvestmentStrategy investmentStrategy = fundListProduct.getInvestmentStrategy();
                    YieldAndCredit yieldAndCredit = fundListProduct.getYieldAndCredit();
                    HoldingDetails holdingDetails = fundListProduct.getHoldingDetails();
                    PurchaseInfo purchaseInfo = fundListProduct.new PurchaseInfo();
                    Rating rating = fundListProduct.new Rating();
                    List<FundListRisk> risk = new ArrayList<FundListRisk>();
                    List<TopHoldingsSearch> topHoldingsSearch = new ArrayList<TopHoldingsSearch>();
                    AssetAllocations assetAllocations = fundListProduct.new AssetAllocations();
                    GlobalStockSectors globalStockSectors = fundListProduct.new GlobalStockSectors();
                    RegionalExposures regionalExposures = fundListProduct.new RegionalExposures();
                    GlobalBondSectors globalBondSectors = fundListProduct.new GlobalBondSectors();
                    BondRegionalExposures bondRegionalExposures = fundListProduct.new BondRegionalExposures();
                    TopTenHoldings topTenHoldings = fundListProduct.new TopTenHoldings();

                    for (SearchProduct searchProduct : list) {
                        // For SearchProduct -> O:externalKey; For SearchObject
                        // -> M:symbol, T:key
                        SearchableObject searchObject = searchProduct.getSearchObject();
                        if (null != searchObject) {
                            String prodAltNumMCode = searchObject.getSymbol();
                            if (fundListProduct.getProdAltNumXCode().equals(searchProduct.getExternalKey())
                                && fundListProduct.getSymbol().equals(prodAltNumMCode)) {
                                for (UtProdInstm utProdInstm : utProdInstmList) {
                                    if (null != utProdInstm) {
                                        dbPerformanceId.add(utProdInstm.getPerformanceId());
                                        if (prodAltNumMCode.equals(utProdInstm.getSymbol())) {
                                            header.setProdAltNumSeg(searchObject.getProdAltNumSeg());
                                            header.setCurrency(searchObject.getProductCcy());
                                            summary.setWeekRangeCurrency(utProdInstm.getClosingPrcCcy());
                                            String currencyProdUsingKey = currencyProdUsingKeyMap.get(prodAltNumMCode);
                                            if (null != profile) {
                                                setProfileData(utProdInstm, utProdChanl, profile, searchObject, index, site);
                                            }
                                            if (null != performance) {
                                                setPerformance(utProdInstm, performance, site);
                                            }
                                            if (null != investmentStrategy) {
                                                investmentStrategy.setInvestmentStyle(
                                                    StringUtil.toStringAndCheckNull(utProdInstm.getEquityStyle()));
                                                investmentStrategy.setInterestRateSensitivity(
                                                    StringUtil.toStringAndCheckNull(utProdInstm.getFixIncomestyle()));
                                            }
                                            if (null != yieldAndCredit) {
                                                setYieldAndCredit(utProdInstm, yieldAndCredit);
                                            }
                                            if (null != holdingDetails) {
                                                holdingDetails.setDividendPerShareCurrency(utProdInstm.getClosingPrcCcy());
                                                holdingDetails.setLastUpdatedDate(DateUtil.string2FormatDateString(
                                                    utProdInstm.getMonthEndDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                                            }

                                            fundListProduct.setPurchaseInfo(
                                                setPurchaseInfoData(utProdInstm, purchaseInfo, currencyProdUsingKey));
                                            fundListProduct.setRating(setRating(utProdInstm, rating));
                                            fundListProduct.setRisk(setRiskList(utProdInstm, risk));
                                            if (null != fundHoldingMap) {
                                                topTenHoldings
                                                    .setItems((setTopHoldingsList(utProdInstm, fundHoldingMap, topHoldingsSearch)));
                                                topTenHoldings.setLastUpdatedDate(DateUtil.string2FormatDateString(
                                                    utProdInstm.getHoldingAllocationPortfolioDate(),
                                                    DateConstants.DateFormat_yyyyMMdd_withHyphen));
                                                fundListProduct.setTopTenHoldings(topTenHoldings);
                                            }
                                            if (null != holdingAllocMap) {
                                                fundListProduct.setAssetAlloc(setAssetAllocation(utProdInstm, holdingAllocMap,
                                                    assetAllocations, "ASET_ALLOC"));
                                                fundListProduct.setStockSectors(setGlobalStockSectors(utProdInstm, holdingAllocMap,
                                                    globalStockSectors, "STOCK_SEC"));
                                                fundListProduct.setEquityRegional(setRegionalExposures(utProdInstm, holdingAllocMap,
                                                    regionalExposures, "STOCK_GEO"));
                                                fundListProduct.setBondSectors(setGlobalBondSectors(utProdInstm, holdingAllocMap,
                                                    globalBondSectors, "BOND_SEC"));
                                                fundListProduct.setBondRegional(setBondRegionalExposures(utProdInstm,
                                                    holdingAllocMap, bondRegionalExposures, "BOND_GEO"));
                                            }
                                            fundListProduct.setLastUpdatedDate(
                                                DateUtil.string2FormatDateString(utProdInstm.getHoldingAllocationPortfolioDate(),
                                                    DateConstants.DateFormat_yyyyMMdd_withHyphen));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // if DB data not match with Mstar, return product list that
                // exists in Mstar and DB
                for (String mstarPerfId : mstarPerformanceId) {
                    if (!dbPerformanceId.contains(mstarPerfId)) {
                        for (FundListProduct fundListProduct : products) {
                            if (fundListProduct.getProdAltNumXCode().equals(mstarPerfId)) {
                                notReturnProducts.add(fundListProduct);
                            }
                        }
                    }
                }
            }
            products.removeAll(notReturnProducts);
        }
    }

    private void setProfileData(final UtProdInstm utProdInstm, final Map<Integer, List<String>> utProdChanl, final Profile profile,
        final SearchableObject searchObject, final Integer index, final String site) {
        if (0 == index) {
            profile.setName(utProdInstm.getProdName());
            profile.setCategoryName(utProdInstm.getCategoryName1());
            profile.setFamilyName(utProdInstm.getFamilyName1());
            profile.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name1());
            profile.setInvestmentRegionName(utProdInstm.getInvestmentRegionName1());
        } else if (1 == index) {
            profile.setName(utProdInstm.getProdPllName());
            profile.setCategoryName(utProdInstm.getCategoryName2());
            profile.setFamilyName(utProdInstm.getFamilyName2());
            profile.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name2());
            profile.setInvestmentRegionName(utProdInstm.getInvestmentRegionName2());
        } else if (2 == index) {
            profile.setName(utProdInstm.getProdSllName());
            profile.setCategoryName(utProdInstm.getCategoryName3());
            profile.setFamilyName(utProdInstm.getFamilyName3());
            profile.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name3());
            profile.setInvestmentRegionName(utProdInstm.getInvestmentRegionName3());
        } else {
            profile.setName(utProdInstm.getProdName());
            profile.setCategoryName(utProdInstm.getCategoryName1());
            profile.setFamilyName(utProdInstm.getFamilyName1());
            profile.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name1());
            profile.setInvestmentRegionName(utProdInstm.getInvestmentRegionName1());
        }
        profile.setCategoryCode(utProdInstm.getCategoryCode());
        profile.setFamilyCode(utProdInstm.getFamilyCode());
        profile.setCategoryLevel1Code(utProdInstm.getCategoryLevel1Code());
        profile.setCompanyName(searchObject.getProductName());
        profile.setInvestmentRegionCode(utProdInstm.getInvestmentRegionCode());
        profile.setPriceQuote(utProdInstm.getDayEndNAV());
        profile.setPriceQuoteCurrency(utProdInstm.getClosingPrcCcy());
        profile.setChangeAmount(utProdInstm.getChangeAmountNAV());
        profile.setChangePercent(utProdInstm.getMarketPrice());
        profile.setTopSellProdIndex(utProdInstm.getTopSellProdIndex());
        Character topPerformersInd = utProdInstm.getTopPerformersInd();
        profile.setTopPerformersIndicator(topPerformersInd == null ? null : topPerformersInd.toString());
        profile.setAssetsUnderManagement(utProdInstm.getAssetsUnderManagement());
        profile.setAssetsUnderManagementCurrencyCode("USD");
        profile.setRiskLvlCde(utProdInstm.getRiskLvlCde());
        profile.setDistributionFrequency(utProdInstm.getDistributionFrequency());
        profile.setDividendYield(utProdInstm.getYield1Yr());
        profile.setRiskFreeRateName(utProdInstm.getRiskFreeRateName());
        profile.setProdStatCde(utProdInstm.getProdStatCde());
        profile.setRelativeRiskMeasuresIndexName(utProdInstm.getRelativeRiskMeasuresIndexName());
        profile.setAmcmIndicator(utProdInstm.getAmcmAuthorizeIndicator());
        profile.setNextDealDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getNextDealDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        profile.setInceptionDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getInceptionDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        profile.setProductCurrency(searchObject.getProductCcy());
        profile.setExchangeUpdatedTime(
            DateUtil.getSimpleDateFormat(utProdInstm.getAsOfDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        profile.setEndDateYearRisk(
            DateUtil.getSimpleDateFormat(utProdInstm.getEndDateYearRisk(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        profile.setEndDateRiskLvlCde(
            DateUtil.getSimpleDateFormat(utProdInstm.getEndDateRiskLvlCde(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        profile.setWeekRangeLowDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getWeekRangeLowDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        profile.setWeekRangeHighDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getWeekRangeHighDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        profile.setSurveyedFundNetAssetsDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getSurveyedFundNetAssetsDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        if (!utProdChanl.isEmpty() && utProdChanl.containsKey(utProdInstm.getUtProdInstmId().getProductId())) {
            List<String> indicate = utProdChanl.get(utProdInstm.getUtProdInstmId().getProductId());
            if (StringUtil.isValid(indicate.get(0))
                    && StringUtil.isValid(indicate.get(1))
                    && StringUtil.isValid(indicate.get(2))
                    && StringUtil.isValid(indicate.get(3))) {
                profile.setAllowBuy(indicate.get(0));
                profile.setAllowSell(indicate.get(1));
                profile.setAllowSwOutProdInd(indicate.get(2));
                profile.setAllowSwInProdInd(indicate.get(3));
            } else {
                profile.setAllowBuy(utProdInstm.getAllowBuyProdInd());
                profile.setAllowSell(utProdInstm.getAllowSellProdInd());
                profile.setAllowSwInProdInd(utProdInstm.getAllowSwInProdInd());
                profile.setAllowSwOutProdInd(utProdInstm.getAllowSwOutProdInd());
            }
        } else {
            profile.setAllowBuy(utProdInstm.getAllowBuyProdInd());
            profile.setAllowSell(utProdInstm.getAllowSellProdInd());
            profile.setAllowSwInProdInd(utProdInstm.getAllowSwInProdInd());
            profile.setAllowSwOutProdInd(utProdInstm.getAllowSwOutProdInd());
        }

        profile.setAllowSellMipProdInd(utProdInstm.getAllowSellMipProdInd());
        profile.setPiFundInd(utProdInstm.getPiFundInd());
        profile.setDeAuthFundInd(utProdInstm.getDeAuthFundInd());
    }

    private void setPerformance(final UtProdInstm utProdInstm, final Performance performance, final String site)
        throws ParseException {
        // performance update Annualised returns
        CumulativeTotalReturns cumulativeTotalReturns = performance.getCumulativeTotalReturns();
        List<FundCumulativeReturn> fundCumulativeReturn = cumulativeTotalReturns.getItems();
        // set annualized returns (daily)
        fundCumulativeReturn.get(0).setTotalDailyReturn(utProdInstm.getReturnytdDaily());
        fundCumulativeReturn.get(1).setTotalDailyReturn(utProdInstm.getReturn1mthDaily());
        fundCumulativeReturn.get(2).setTotalDailyReturn(utProdInstm.getReturn3mthDaily());
        fundCumulativeReturn.get(3).setTotalDailyReturn(utProdInstm.getReturn6mthDaily());
        fundCumulativeReturn.get(4).setTotalDailyReturn(utProdInstm.getReturn1yrDaily());
        fundCumulativeReturn.get(5).setTotalDailyReturn(utProdInstm.getReturn3yrDaily());
        fundCumulativeReturn.get(6).setTotalDailyReturn(utProdInstm.getReturn5yrDaily());
        fundCumulativeReturn.get(7).setTotalDailyReturn(utProdInstm.getReturn10yrDaily());
        // set annualized returns (monthly)
        fundCumulativeReturn.get(0).setTotalReturn(utProdInstm.getReturnYTD());
        fundCumulativeReturn.get(1).setTotalReturn(utProdInstm.getReturn1mth());
        fundCumulativeReturn.get(2).setTotalReturn(utProdInstm.getReturn3mth());
        fundCumulativeReturn.get(3).setTotalReturn(utProdInstm.getReturn6mth());
        fundCumulativeReturn.get(4).setTotalReturn(utProdInstm.getReturn1yr());
        fundCumulativeReturn.get(5).setTotalReturn(utProdInstm.getReturn3yr());
        fundCumulativeReturn.get(6).setTotalReturn(utProdInstm.getReturn5yr());
        fundCumulativeReturn.get(7).setTotalReturn(utProdInstm.getReturn10yr());

        // performance update Calendar returns
        CalendarYearTotalReturns calendarYearTotalReturns = performance.getCalendarYearTotalReturns();
        List<FundCalendarYearReturn> fundCalendarYearReturn = calendarYearTotalReturns.getItems();
        // set calendar returns (monthly)
        fundCalendarYearReturn.get(0).setFundCalendarYearReturn(utProdInstm.getYear1());
        fundCalendarYearReturn.get(1).setFundCalendarYearReturn(utProdInstm.getYear2());
        fundCalendarYearReturn.get(2).setFundCalendarYearReturn(utProdInstm.getYear3());
        fundCalendarYearReturn.get(3).setFundCalendarYearReturn(utProdInstm.getYear4());
        fundCalendarYearReturn.get(4).setFundCalendarYearReturn(utProdInstm.getYear5());
    }

    private YieldAndCredit setYieldAndCredit(final UtProdInstm utProdInstm, final YieldAndCredit yieldAndCredit)
        throws ParseException {
        yieldAndCredit.setAverageCurrentYield(utProdInstm.getCurrentYield());
        yieldAndCredit.setAverageYieldToMaturity(utProdInstm.getYieldToMaturity());
        yieldAndCredit.setAverageDuration(utProdInstm.getEffectiveDuration());
        yieldAndCredit.setAverageCreditQualityName(utProdInstm.getAverageCreditQualityName());
        yieldAndCredit.setLastUpdatedDate(DateUtil.string2FormatDateString(utProdInstm.getCreditQualityBreakdownAsOfDate(),
            DateConstants.DateFormat_yyyyMMdd_withHyphen));
        return yieldAndCredit;
    }

    private List<FundListRisk> setRiskList(final UtProdInstm utProdInstm, final List<FundListRisk> risk) throws ParseException {
        // Year1 Risk
        FundListRisk fundListRisk1Year = new FundListRisk();
        YearRisk year1Risk = fundListRisk1Year.new YearRisk();
        year1Risk.setYear(MstarConstants.ONE);
        year1Risk.setBeta(utProdInstm.getBeta1());
        year1Risk.setStdDev(utProdInstm.getStdDev1());
        year1Risk.setAlpha(utProdInstm.getAlpha1());
        year1Risk.setSharpeRatio(utProdInstm.getSharpeRatio1());
        year1Risk.setrSquared(utProdInstm.getrSquared1());
        year1Risk.setTotalReturn(utProdInstm.getReturn1yr());
        year1Risk.setEndDate(
            DateUtil.string2FormatDateString(utProdInstm.getMonthEndDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        fundListRisk1Year.setYearRisk(year1Risk);
        risk.add(fundListRisk1Year);

        // Year3 Risk
        FundListRisk fundListRisk3Year = new FundListRisk();
        YearRisk year3Risk = fundListRisk3Year.new YearRisk();
        year3Risk.setYear(MstarConstants.THREE);
        year3Risk.setBeta(utProdInstm.getBeta3());
        year3Risk.setStdDev(utProdInstm.getStdDev3Yr());
        year3Risk.setAlpha(utProdInstm.getAlpha3());
        year3Risk.setSharpeRatio(utProdInstm.getSharpeRatio3());
        year3Risk.setrSquared(utProdInstm.getrSquared3());
        year3Risk.setTotalReturn(utProdInstm.getReturn3yr());
        year3Risk.setEndDate(
            DateUtil.string2FormatDateString(utProdInstm.getMonthEndDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        fundListRisk3Year.setYearRisk(year3Risk);
        risk.add(fundListRisk3Year);

        // Year5 Risk
        FundListRisk fundListRisk5Year = new FundListRisk();
        YearRisk year5Risk = fundListRisk5Year.new YearRisk();
        year5Risk.setYear(MstarConstants.FIVE);
        year5Risk.setBeta(utProdInstm.getBeta5());
        year5Risk.setStdDev(utProdInstm.getStdDev5());
        year5Risk.setAlpha(utProdInstm.getAlpha5());
        year5Risk.setSharpeRatio(utProdInstm.getSharpeRatio5());
        year5Risk.setrSquared(utProdInstm.getrSquared5());
        year5Risk.setTotalReturn(utProdInstm.getReturn5yr());
        year5Risk.setEndDate(
            DateUtil.string2FormatDateString(utProdInstm.getMonthEndDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        fundListRisk5Year.setYearRisk(year5Risk);
        risk.add(fundListRisk5Year);

        // Year10 Risk
        FundListRisk fundListRisk10Year = new FundListRisk();
        YearRisk year10Risk = fundListRisk10Year.new YearRisk();
        year10Risk.setYear(MstarConstants.TEN);
        year10Risk.setBeta(utProdInstm.getBeta10());
        year10Risk.setStdDev(utProdInstm.getStdDev10());
        year10Risk.setAlpha(utProdInstm.getAlpha10());
        year10Risk.setSharpeRatio(utProdInstm.getSharpeRatio10());
        year10Risk.setrSquared(utProdInstm.getrSquared10());
        year10Risk.setTotalReturn(utProdInstm.getReturn10yr());
        year10Risk.setEndDate(
            DateUtil.string2FormatDateString(utProdInstm.getMonthEndDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        fundListRisk10Year.setYearRisk(year10Risk);
        risk.add(fundListRisk10Year);
        return risk;
    }

    private Rating setRating(final UtProdInstm utProdInstm, final Rating rating) throws ParseException {
        rating.setAverageCreditQuality(utProdInstm.getAverageCreditQuality());
        rating.setAverageCreditQualityName(utProdInstm.getAverageCreditQualityName());
        rating.setAverageCreditQualityDate(DateUtil.getSimpleDateFormat(utProdInstm.getHoldingAllocationPortfolioDate(),
            DateConstants.DateFormat_yyyyMMdd_withHyphen));
        rating.setRank1Yr(utProdInstm.getRank1Yr());
        rating.setRank3Yr(utProdInstm.getRank3Yr());
        rating.setRank5Yr(utProdInstm.getRank5Yr());
        rating.setRank10Yr(utProdInstm.getRank10Yr());
        rating.setRatingDate(DateUtil.string2FormatDateString(utProdInstm.getCreditQualityBreakdownAsOfDate(),
            DateConstants.DateFormat_yyyyMMdd_withHyphen));
        return rating;
    }

    private List<TopHoldingsSearch> setTopHoldingsList(final UtProdInstm utProdInstm,
        final Map<String, List<UtHoldings>> fundHoldingMap, final List<TopHoldingsSearch> topHoldings) {
        List<UtHoldings> fundHoldingList = fundHoldingMap.get(utProdInstm.getPerformanceId());
        if (ListUtil.isValid(fundHoldingList)) {
            for (UtHoldings utHoldings : fundHoldingList) {
                TopHoldingsSearch topHoldingsItem = new TopHoldingsSearch();
                topHoldingsItem.setHoldingName(utHoldings.getUtHoldingsId().getHoldingId().toString());
                topHoldingsItem.setHoldingCompany(utHoldings.getUtHoldingsId().getHolderName());
                topHoldingsItem.setHoldingPercent(utHoldings.getWeight());
                topHoldings.add(topHoldingsItem);
            }
            sortHolding_top10Hldg(topHoldings);
        }
        return topHoldings;
    }

    private void sortHolding_top10Hldg(final List<TopHoldingsSearch> topHoldings) {
        Collections.sort(topHoldings, new Comparator<TopHoldingsSearch>() {
            @Override
            public int compare(final TopHoldingsSearch holding1, final TopHoldingsSearch holding2) {

                if (holding1 == null || holding1.getHoldingPercent() == null) {
                    return 1;
                } else if (holding2 == null || holding2.getHoldingPercent() == null) {
                    return -1;
                } else {
                    return holding2.getHoldingPercent().compareTo(holding1.getHoldingPercent());
                }
            }
        });
    }

    private AssetAllocations setAssetAllocation(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> utAssetAllocMap, final AssetAllocations assetAllocations, final String classType)
        throws ParseException {
        if (null != utAssetAllocMap) {
            List<UTHoldingAlloc> assetAllocList = utAssetAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> assetAllocationsList = new ArrayList<HoldingAllocation>();
            if (ListUtil.isValid(assetAllocList)) {
                for (UTHoldingAlloc assetAlloc : assetAllocList) {
                    if (assetAlloc.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(assetAlloc.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(assetAlloc.getHoldingAllocWeightNet());
                        assetAllocationsList.add(holdingAllocation);
                        assetAllocations.setAssetAllocations(assetAllocationsList);
                        assetAllocations.setPortfolioDate(DateUtil.getSimpleDateFormat(assetAlloc.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }
        }
        return assetAllocations;
    }

    private GlobalStockSectors setGlobalStockSectors(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> utAssetAllocMap, final GlobalStockSectors globalStockSectors,
        final String classType) {
        if (null != utAssetAllocMap) {
            List<UTHoldingAlloc> stockSectorsList = utAssetAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> globalStockSectorsList = new ArrayList<HoldingAllocation>();
            if (ListUtil.isValid(stockSectorsList)) {
                for (UTHoldingAlloc stockSectors : stockSectorsList) {
                    if (stockSectors.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(stockSectors.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(stockSectors.getHoldingAllocWeightNet());
                        globalStockSectorsList.add(holdingAllocation);
                        globalStockSectors.setGlobalStockSectors(globalStockSectorsList);
                        globalStockSectors.setPortfolioDate(DateUtil.getSimpleDateFormat(stockSectors.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }
        }
        return globalStockSectors;
    }


    private RegionalExposures setRegionalExposures(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> utAssetAllocMap, final RegionalExposures equityRegional, final String classType) {
        if (null != utAssetAllocMap) {
            List<UTHoldingAlloc> equityRegionalList = utAssetAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> RegionalExposuresList = new ArrayList<HoldingAllocation>();
            if (ListUtil.isValid(equityRegionalList)) {
                for (UTHoldingAlloc equityRegiona : equityRegionalList) {
                    if (equityRegiona.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(equityRegiona.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(equityRegiona.getHoldingAllocWeightNet());
                        RegionalExposuresList.add(holdingAllocation);
                        equityRegional.setRegionalExposures(RegionalExposuresList);
                        equityRegional.setPortfolioDate(DateUtil.getSimpleDateFormat(equityRegiona.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }
        }
        return equityRegional;
    }

    private GlobalBondSectors setGlobalBondSectors(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> utAssetAllocMap, final GlobalBondSectors globalBondSectors,
        final String classType) {
        if (null != utAssetAllocMap) {
            List<UTHoldingAlloc> bondSectorsList = utAssetAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> globalBondSectorsList = new ArrayList<HoldingAllocation>();
            if (ListUtil.isValid(bondSectorsList)) {
                for (UTHoldingAlloc bondSectors : bondSectorsList) {
                    if (bondSectors.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(bondSectors.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(bondSectors.getHoldingAllocWeightNet());
                        globalBondSectorsList.add(holdingAllocation);
                        globalBondSectors.setGlobalBondSectors(globalBondSectorsList);
                        globalBondSectors.setPortfolioDate(DateUtil.getSimpleDateFormat(bondSectors.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }
        }
        return globalBondSectors;
    }

    private BondRegionalExposures setBondRegionalExposures(final UtProdInstm utProdInstm,
        final Map<String, List<UTHoldingAlloc>> utAssetAllocMap, final BondRegionalExposures bondRegionalExposures,
        final String classType) {
        if (null != utAssetAllocMap) {
            List<UTHoldingAlloc> bondRegionalList = utAssetAllocMap.get(utProdInstm.getPerformanceId());
            List<HoldingAllocation> bondRegionalExposuresList = new ArrayList<HoldingAllocation>();
            if (ListUtil.isValid(bondRegionalList)) {
                for (UTHoldingAlloc bondRegional : bondRegionalList) {
                    if (bondRegional.getUtHoldingAllocId().getHoldingAllocClassType().equals(classType)) {
                        HoldingAllocation holdingAllocation = new HoldingAllocation();
                        holdingAllocation.setName(bondRegional.getUtHoldingAllocId().getHoldingAllocClassName());
                        holdingAllocation.setWeighting(bondRegional.getHoldingAllocWeightNet());
                        bondRegionalExposuresList.add(holdingAllocation);
                        bondRegionalExposures.setBondRegionalExposures(bondRegionalExposuresList);
                        bondRegionalExposures.setPortfolioDate(DateUtil.getSimpleDateFormat(bondRegional.getPortfolioDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    }
                }
            }
        }
        return bondRegionalExposures;
    }


    private PurchaseInfo setPurchaseInfoData(final UtProdInstm utProdInstm, final PurchaseInfo purchaseInfo,
        final String currencyProdUsingKey) {
        purchaseInfo.setInitialCharge(utProdInstm.getChrgInitSalesPct());
        purchaseInfo.setAnnualManagementFee(utProdInstm.getAnnMgmtFeePct());
        purchaseInfo.setExpenseRatio(utProdInstm.getExpenseRatio());
        purchaseInfo.setMinimumInitial(utProdInstm.getMinInitInvst());
        // PurchaseCurrencyId - Fund Purchase Details;
        purchaseInfo.setMinimumInitialCurrencyCode(utProdInstm.getPurchaseCcy());
        // PurchaseCurrencyId - Fund Purchase Details;
        purchaseInfo.setMinimumSubsequent(utProdInstm.getMinSubqInvst());
        purchaseInfo.setMinimumSubsequentCurrencyCode(utProdInstm.getPurchaseCcy());

        purchaseInfo.setMinInitInvst(utProdInstm.getMinInitInvst());
        // PurchaseCurrencyId - Fund Purchase Details;
        purchaseInfo.setMinInitInvstCurrencyCode(utProdInstm.getPurchaseCcy());
        purchaseInfo.setMinSubqInvst(utProdInstm.getMinSubqInvst());
        // PurchaseCurrencyId - Fund Purchase Details;
        purchaseInfo.setMinSubqInvstCurrencyCode(utProdInstm.getPurchaseCcy());

        purchaseInfo.sethhhhMinInitInvst(utProdInstm.gethhhhMinInitInvst());
        purchaseInfo.sethhhhMinInitInvstCurrencyCode(getTradableCurrencyCode(utProdInstm, currencyProdUsingKey));
        purchaseInfo.sethhhhMinSubqInvst(utProdInstm.gethhhhMinSubqInvst());
        purchaseInfo.sethhhhMinSubqInvstCurrencyCode(getTradableCurrencyCode(utProdInstm, currencyProdUsingKey));
        purchaseInfo.setAnnualReportDate(
            DateUtil.getSimpleDateFormat(utProdInstm.getAnnualReportDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
        return purchaseInfo;
    }

    private String getTradableCurrencyCode(final UtProdInstm fund, final String usingProductKey) {
        if (this.tradableCurrencyProdUsingMap.contains(usingProductKey)) {
            return fund.getCcyProdTradeCde();
        }
        return fund.getCcyInvstCde();
    }
}