package com.hhhh.group.secwealth.mktdata.fund.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hhhh.group.secwealth.mktdata.fund.util.FundSearchResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.service.AbstractService;
import com.hhhh.group.secwealth.mktdata.common.service.ServiceExecutor;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.common.util.JacksonUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundQuoteSummaryRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundQuoteSummaryResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.FundSummaryRisk;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.FundSummaryRisk.YearRisk;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.FeesAndExpenses;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.HoldingDetails;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.InvestmentStrategy;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.Profile;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.Rating;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.ToNewInvestors;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary.Summary.YieldAndCredit;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundQuoteSummaryDao;
import com.hhhh.group.secwealth.mktdata.common.system.constants.PredictiveSearchConstant;

@Service("fundQuoteSummaryService")
public class FundQuoteSummaryServiceImpl extends AbstractService {

    private final String SITE_FEATURE_CURRENCY = ".currency";

    @Autowired
    @Qualifier("quoteSummaryServiceExecutor")
    private ServiceExecutor quoteSummaryServiceExecutor;

    @Resource(name = "tradableCurrencyProdUsingMap")
    private List<String> tradableCurrencyProdUsingMap;

    @Autowired
    @Qualifier("fundQuoteSummaryDao")
    private FundQuoteSummaryDao fundQuoteSummaryDao;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @Autowired
    @Qualifier("siteFeature")
    private SiteFeature siteFeature;

    @Override
    public Object execute(final Object object) throws Exception {

        FundQuoteSummaryRequest request = (FundQuoteSummaryRequest) object;

        FundQuoteSummaryResponse quoteSummaryResponse = (FundQuoteSummaryResponse) this.quoteSummaryServiceExecutor
            .execute(request);
        // get product List from DB
        List<UtProdInstm> utProdInstmList = this.fundQuoteSummaryDao.searchProfile(request);
        
        if (ListUtil.isInvalid(utProdInstmList)) {
            LogUtil.warn(FundQuoteSummaryServiceImpl.class, "the fundQuoteSummary no data in DB =  request: ",
                JacksonUtil.beanToJson(request));
        }

        Map<String, String> headers = request.getHeaders();
        String cmbInd = headers.get(CommonConstants.REQUEST_HEADER_GBGF);
        String channelRestrictCode = FundSearchResultUtil.buildCmbQuoteSummaryChannelRestrictCode(request, cmbInd);

        Map<Integer, List<String>> utProdChanl = this.fundQuoteSummaryDao.searchChanlFund(channelRestrictCode);

        String currencyProdUsingKey = StringUtil.combineWithUnderline(request.getCountryCode(), request.getGroupMember(),
            request.getProductType());

        int index = this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT
            + request.getLocale());

        // merge Mstar and DB data
        if(null != quoteSummaryResponse && ListUtil.isValid(utProdInstmList)){
            mergeResponseFromeDB(quoteSummaryResponse, utProdInstmList, utProdChanl, index, currencyProdUsingKey, request.getSiteKey());
        }
        return quoteSummaryResponse;
    }

    private void mergeResponseFromeDB(final FundQuoteSummaryResponse quoteSummaryResponse, final List<UtProdInstm> utProdInstmList,
                                      final Map<Integer, List<String>> utProdChanl, final Integer index, final String currencyProdUsingKey, final String siteKey)
            throws Exception {
        Summary summary = quoteSummaryResponse.getSummary();
        if (null != summary) {
            Profile profile = summary.getProfile();
            HoldingDetails holdingDetails = summary.getHoldingDetails();
            ToNewInvestors toNewInvestors = summary.getToNewInvestors();
            InvestmentStrategy investmentStrategy = summary.getInvestmentStrategy();
            YieldAndCredit yieldAndCredit = summary.getYieldAndCredit();
            Rating rating = summary.new Rating();
            FeesAndExpenses feesAndExpenses = summary.getFeesAndExpenses();
            List<FundSummaryRisk> risk = new ArrayList<FundSummaryRisk>();

            if (utProdInstmList.size() == 1) {
                UtProdInstm utProdInstm = utProdInstmList.get(0);
                if (null != utProdInstm) {
                    summary.setWeekRangeCurrency(utProdInstm.getClosingPrcCcy());

                    buildProfileName(index, profile, utProdInstm);

                    profile.setInceptionDate(DateUtil.getSimpleDateFormat(utProdInstm.getInceptionDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    profile.sethhhhCategoryCode(utProdInstm.getCategoryCode());
                    profile.setCategoryLevel1Code(utProdInstm.getCategoryLevel1Code());
                    profile.setFamilyCode(utProdInstm.getFamilyCode());
                    profile.setDividendYield(utProdInstm.getYield1Yr());
                    profile.setDistributionFrequency(utProdInstm.getDistributionFrequency());
                    profile.setRiskLvlCde(utProdInstm.getRiskLvlCde());
                    profile.setExpenseRatio(utProdInstm.getExpenseRatio());
                    profile.setMarginRatio(utProdInstm.getLoanProdOdMrgnPct());
                    profile.setInitialCharge(utProdInstm.getChrgInitSalesPct());
                    profile.setAnnualManagementFee(utProdInstm.getAnnMgmtFeePct());
                    profile.setAssetsUnderManagement(utProdInstm.getAssetsUnderManagement());
                    profile.setInvestmentRegionCode(utProdInstm.getInvestmentRegionCode());
                    profile.setFundClassCde(utProdInstm.getFundClassCde());
                    profile.setAmcmIndicator(utProdInstm.getAmcmAuthorizeIndicator());
                    profile.setRiskFreeRateName(utProdInstm.getRiskFreeRateName());
                    profile.setRelativeRiskMeasuresIndexName(utProdInstm.getRelativeRiskMeasuresIndexName());
                    profile.setProdStatCde(utProdInstm.getProdStatCde());
                    profile.setNextDealDate(DateUtil.getSimpleDateFormat(utProdInstm.getNextDealDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    profile.setSurveyedFundNetAssetsDate(DateUtil.getSimpleDateFormat(
                            utProdInstm.getSurveyedFundNetAssetsDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    profile.setWeekRangeLowDate(DateUtil.getSimpleDateFormat(utProdInstm.getWeekRangeLowDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    profile.setWeekRangeHighDate(DateUtil.getSimpleDateFormat(utProdInstm.getWeekRangeHighDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    profile.setAnnualReportDate(DateUtil.getSimpleDateFormat(utProdInstm.getAnnualReportDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    profile.setSurveyedFundNetAssetsDate(DateUtil.getSimpleDateFormat(
                            utProdInstm.getSurveyedFundNetAssetsDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    profile.setPiFundInd(utProdInstm.getPiFundInd());
                    profile.setDeAuthFundInd(utProdInstm.getDeAuthFundInd());

                    String currency = this.siteFeature.getStringFeature(siteKey, this.SITE_FEATURE_CURRENCY);

                    setAssetsUnderManagementCurrencyCode(profile, utProdInstm, currency);

                    Character topPerformersInd = utProdInstm.getTopPerformersInd();
                    profile.setTopPerformersIndicator(topPerformersInd == null ? null : topPerformersInd.toString());
                    profile.setTopSellProdIndex(utProdInstm.getTopSellProdIndex());

                    buildAllowBuySellSwInd(utProdChanl, profile, utProdInstm);

                    profile.setAllowSwInProdInd(utProdInstm.getAllowSwInProdInd());
                    profile.setAllowSwOutProdInd(utProdInstm.getAllowSwOutProdInd());
                    profile.setAllowSellMipProdInd(utProdInstm.getAllowSellMipProdInd());

                    holdingDetails.setDividendPerShareCurrency(utProdInstm.getClosingPrcCcy());

                    holdingDetails.setEquityStyle(StringUtil.toStringAndCheckNull(utProdInstm.getEquityStyle()));
                    holdingDetails.setLastUpdatedDate(DateUtil.string2FormatDateString(utProdInstm.getMonthEndDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));

                    toNewInvestors.setMinInitInvst(utProdInstm.getMinInitInvst());
                    // PurchaseCurrencyId - Fund Purchase Details
                    toNewInvestors.setMinInitInvstCurrencyCode(utProdInstm.getPurchaseCcy());
                    toNewInvestors.setMinSubqInvst(utProdInstm.getMinSubqInvst());
                    // PurchaseCurrencyId - Fund Purchase Details
                    toNewInvestors.setMinSubqInvstCurrencyCode(utProdInstm.getPurchaseCcy());

                    toNewInvestors.sethhhhMinInitInvst(utProdInstm.gethhhhMinInitInvst());
                    toNewInvestors.sethhhhMinInitInvstCurrencyCode(this.getTradableCurrencyCode(utProdInstm,
                            currencyProdUsingKey));
                    toNewInvestors.sethhhhMinSubqInvst(utProdInstm.gethhhhMinSubqInvst());
                    toNewInvestors.sethhhhMinSubqInvstCurrencyCode(this.getTradableCurrencyCode(utProdInstm,
                            currencyProdUsingKey));

                    Integer minInitRRSPInvst = 0;
                    Integer minSubqRRSPInvst = 0;

                    buildInvestors(toNewInvestors, profile, minInitRRSPInvst, minSubqRRSPInvst);

                    toNewInvestors.setMinInitRRSPInvst(new BigDecimal(minInitRRSPInvst));
                    toNewInvestors.setMinSubqRRSPInvst(new BigDecimal(minSubqRRSPInvst));

                    investmentStrategy.setInvestmentStyle(StringUtil.toStringAndCheckNull(utProdInstm.getEquityStyle()));
                    investmentStrategy.setInterestRateSensitivity(StringUtil.toStringAndCheckNull(utProdInstm
                            .getFixIncomestyle()));

                    yieldAndCredit.setAverageCurrentYield(utProdInstm.getCurrentYield());
                    yieldAndCredit.setAverageYieldToMaturity(utProdInstm.getYieldToMaturity());
                    yieldAndCredit.setAverageDuration(utProdInstm.getEffectiveDuration());
                    yieldAndCredit.setLastUpdatedDate(DateUtil.string2FormatDateString(
                            utProdInstm.getCreditQualityBreakdownAsOfDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));

                    rating.setAverageCreditQualityName(utProdInstm.getAverageCreditQualityName());
                    rating.setAverageCreditQuality(utProdInstm.getAverageCreditQuality());
                    rating.setAverageCreditQualityDate(DateUtil.getSimpleDateFormat(
                            utProdInstm.getHoldingAllocationPortfolioDate(), DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    rating.setRank1Yr(utProdInstm.getRank1Yr());
                    rating.setRank3Yr(utProdInstm.getRank3Yr());
                    rating.setRank5Yr(utProdInstm.getRank5Yr());
                    rating.setRank10Yr(utProdInstm.getRank10Yr());
                    rating.setRatingDate(DateUtil.string2FormatDateString(utProdInstm.getCreditQualityBreakdownAsOfDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));

                    // Year1 Risk
                    FundSummaryRisk fundSummaryRisk1Year = new FundSummaryRisk();
                    YearRisk year1Risk = fundSummaryRisk1Year.new YearRisk();
                    year1Risk.setYear(MstarConstants.ONE);
                    year1Risk.setBeta(utProdInstm.getBeta1());
                    year1Risk.setStdDev(utProdInstm.getStdDev1());
                    year1Risk.setAlpha(utProdInstm.getAlpha1());
                    year1Risk.setSharpeRatio(utProdInstm.getSharpeRatio1());
                    year1Risk.setrSquared(utProdInstm.getrSquared1());
                    year1Risk.setTotalReturn(utProdInstm.getReturn1yr());
                    year1Risk.setEndDate(DateUtil.string2FormatDateString(utProdInstm.getMonthEndDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    fundSummaryRisk1Year.setYearRisk(year1Risk);
                    risk.add(fundSummaryRisk1Year);

                    // Year3 Risk
                    FundSummaryRisk fundSummaryRisk3Year = new FundSummaryRisk();
                    YearRisk year3Risk = fundSummaryRisk3Year.new YearRisk();
                    year3Risk.setYear(MstarConstants.THREE);
                    year3Risk.setBeta(utProdInstm.getBeta3());
                    year3Risk.setStdDev(utProdInstm.getStdDev3Yr());
                    year3Risk.setAlpha(utProdInstm.getAlpha3());
                    year3Risk.setSharpeRatio(utProdInstm.getSharpeRatio3());
                    year3Risk.setrSquared(utProdInstm.getrSquared3());
                    year3Risk.setTotalReturn(utProdInstm.getReturn3yr());
                    year3Risk.setEndDate(DateUtil.string2FormatDateString(utProdInstm.getMonthEndDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    fundSummaryRisk3Year.setYearRisk(year3Risk);
                    risk.add(fundSummaryRisk3Year);

                    // Year5 Risk
                    FundSummaryRisk fundSummaryRisk5Year = new FundSummaryRisk();
                    YearRisk year5Risk = fundSummaryRisk5Year.new YearRisk();
                    year5Risk.setYear(MstarConstants.FIVE);
                    year5Risk.setBeta(utProdInstm.getBeta5());
                    year5Risk.setStdDev(utProdInstm.getStdDev5());
                    year5Risk.setAlpha(utProdInstm.getAlpha5());
                    year5Risk.setSharpeRatio(utProdInstm.getSharpeRatio5());
                    year5Risk.setrSquared(utProdInstm.getrSquared5());
                    year5Risk.setTotalReturn(utProdInstm.getReturn5yr());
                    year5Risk.setEndDate(DateUtil.string2FormatDateString(utProdInstm.getMonthEndDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    fundSummaryRisk5Year.setYearRisk(year5Risk);
                    risk.add(fundSummaryRisk5Year);

                    // Year10 Risk
                    FundSummaryRisk fundSummaryRisk10Year = new FundSummaryRisk();
                    YearRisk year10Risk = fundSummaryRisk10Year.new YearRisk();
                    year10Risk.setYear(MstarConstants.TEN);
                    year10Risk.setBeta(utProdInstm.getBeta10());
                    year10Risk.setStdDev(utProdInstm.getStdDev10());
                    year10Risk.setAlpha(utProdInstm.getAlpha10());
                    year10Risk.setSharpeRatio(utProdInstm.getSharpeRatio10());
                    year10Risk.setrSquared(utProdInstm.getrSquared10());
                    year10Risk.setTotalReturn(utProdInstm.getReturn10yr());
                    year10Risk.setEndDate(DateUtil.string2FormatDateString(utProdInstm.getMonthEndDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                    fundSummaryRisk10Year.setYearRisk(year10Risk);
                    risk.add(fundSummaryRisk10Year);

                    summary.setRating(rating);
                    summary.setRisk(risk);

                    feesAndExpenses.setOnGoingChargeFigure(utProdInstm.getKiidOngoingCharge());
                    feesAndExpenses.setLastUpdatedDate(DateUtil.string2FormatDateString(utProdInstm.getKiidOngoingChargeDate(),
                            DateConstants.DateFormat_yyyyMMdd_withHyphen));
                }
            } else {
                LogUtil.error(FundQuoteSummaryServiceImpl.class, "the fundQuoteSummaryHelperList is invalid, size =  : "
                        + utProdInstmList.size());
                throw new CommonException(PredictiveSearchConstant.ERRORMSG_NORECORDFOUND);
            }
        }

    }

    private void buildInvestors(ToNewInvestors toNewInvestors, Profile profile, Integer minInitRRSPInvst, Integer minSubqRRSPInvst) {
        try {
            if (!((null == toNewInvestors.getMinSubqRRSPInvst()) || (""
                    .equals(toNewInvestors.getMinSubqRRSPInvst().toString())))) {
                minSubqRRSPInvst = Integer.parseInt(toNewInvestors.getMinSubqRRSPInvst().toString());
            }
            buildSubqRRSPInvst(profile, minSubqRRSPInvst);

            if (!((null == toNewInvestors.getMinInitRRSPInvst()) || (""
                    .equals(toNewInvestors.getMinInitRRSPInvst().toString())))) {
                minInitRRSPInvst = Integer.parseInt(toNewInvestors.getMinInitRRSPInvst().toString());
            }
            buildMinInitRRSPInvst(profile, minInitRRSPInvst);

        } catch (NumberFormatException e) {
            LogUtil.error(FundQuoteSummaryServiceImpl.class, "", e);
        }
    }

    private Integer buildSubqRRSPInvst(Profile profile, Integer minSubqRRSPInvst) {
        if (CommonConstants.hhhh_FUND_FAMILY_CODE.equals(profile.getFamilyCode())) {
            minSubqRRSPInvst = (minSubqRRSPInvst > CommonConstants.hhhh_FUND_ADDITIONAL_RRSP) ? minSubqRRSPInvst
                    : CommonConstants.hhhh_FUND_ADDITIONAL_RRSP;
        } else {
            minSubqRRSPInvst = (minSubqRRSPInvst > CommonConstants.NON_hhhh_FUND_ADDITIONAL_RRSP)
                    ? minSubqRRSPInvst : CommonConstants.NON_hhhh_FUND_ADDITIONAL_RRSP;
        }
        return minSubqRRSPInvst;
    }

    private Integer buildMinInitRRSPInvst(Profile profile, Integer minInitRRSPInvst) {
        if (CommonConstants.hhhh_FUND_FAMILY_CODE.equals(profile.getFamilyCode())) {
            minInitRRSPInvst = (minInitRRSPInvst > CommonConstants.hhhh_FUND_INITIAL_RRSP) ? minInitRRSPInvst
                    : CommonConstants.hhhh_FUND_INITIAL_RRSP;
        } else {
            minInitRRSPInvst = (minInitRRSPInvst > CommonConstants.NON_hhhh_FUND_INITIAL_RRSP) ? minInitRRSPInvst
                    : CommonConstants.NON_hhhh_FUND_INITIAL_RRSP;
        }
        return minInitRRSPInvst;
    }

    private void setAssetsUnderManagementCurrencyCode(Profile profile, UtProdInstm utProdInstm, String currency) {
        if (StringUtil.isValid(currency)) {
            profile.setAssetsUnderManagementCurrencyCode(currency);
        } else {
            profile.setAssetsUnderManagementCurrencyCode(utProdInstm.getCcyAsofRep());
        }
    }

    private void buildProfileName(Integer index, Profile profile, UtProdInstm utProdInstm) {
        if (1 == index) {
            profile.setName(utProdInstm.getProdPllName());
            profile.sethhhhCategoryName(utProdInstm.getCategoryName2());
            profile.setFamilyName(utProdInstm.getFamilyName2());
            profile.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name2());
            profile.setInvestmentRegionName(utProdInstm.getInvestmentRegionName2());
        } else if (2 == index) {
            profile.setName(utProdInstm.getProdSllName());
            profile.sethhhhCategoryName(utProdInstm.getCategoryName3());
            profile.setFamilyName(utProdInstm.getFamilyName3());
            profile.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name3());
            profile.setInvestmentRegionName(utProdInstm.getInvestmentRegionName3());
        } else {
            profile.setName(utProdInstm.getProdName());
            profile.sethhhhCategoryName(utProdInstm.getCategoryName1());
            profile.setFamilyName(utProdInstm.getFamilyName1());
            profile.setCategoryLevel1Name(utProdInstm.getCategoryLevel1Name1());
            profile.setInvestmentRegionName(utProdInstm.getInvestmentRegionName1());
        }
    }

    private void buildAllowBuySellSwInd(Map<Integer, List<String>> utProdChanl, Profile profile, UtProdInstm utProdInstm) {
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
    }

    private String getTradableCurrencyCode(final UtProdInstm fund, final String usingProductKey) {
        if (this.tradableCurrencyProdUsingMap.contains(usingProductKey)) {
            return fund.getCcyProdTradeCde();
        }
        return fund.getCcyInvstCde();
    }

}
