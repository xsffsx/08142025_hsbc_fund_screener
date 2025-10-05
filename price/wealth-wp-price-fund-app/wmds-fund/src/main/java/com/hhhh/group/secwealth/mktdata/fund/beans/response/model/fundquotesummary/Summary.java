
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary;

import java.math.BigDecimal;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;

public class Summary {

    private BigDecimal bid;
    private BigDecimal weekRangeLow;
    private BigDecimal weekRangeHigh;
    private BigDecimal offer;
    private String weekRangeCurrency;
    private CalendarYearTotalReturns calendarYearTotalReturns;
    private CumulativeTotalReturns cumulativeTotalReturns;
    private Profile Profile;
    private HoldingDetails holdingDetails;
    private ToNewInvestors toNewInvestors;
    private FeesAndExpenses feesAndExpenses;
    private MorningstarRatings morningstarRatings;
    private MgmtAndContactInfo mgmtAndContactInfo;
    private InvestmentStrategy investmentStrategy;
    private YieldAndCredit yieldAndCredit;
    private Rating rating;
    private List<FundSummaryRisk> risk;
    private List<ProdAltNumSeg> prodAltNumSegs;


    public BigDecimal getBid() {
        return this.bid;
    }


    public void setBid(final BigDecimal bid) {
        this.bid = bid;
    }


    public BigDecimal getWeekRangeLow() {
        return this.weekRangeLow;
    }


    public void setWeekRangeLow(final BigDecimal weekRangeLow) {
        this.weekRangeLow = weekRangeLow;
    }


    public BigDecimal getWeekRangeHigh() {
        return this.weekRangeHigh;
    }


    public void setWeekRangeHigh(final BigDecimal weekRangeHigh) {
        this.weekRangeHigh = weekRangeHigh;
    }


    public BigDecimal getOffer() {
        return this.offer;
    }


    public void setOffer(final BigDecimal offer) {
        this.offer = offer;
    }

    public CalendarYearTotalReturns getCalendarYearTotalReturns() {
        return this.calendarYearTotalReturns;
    }

    public void setCalendarYearTotalReturns(final CalendarYearTotalReturns calendarYearTotalReturns) {
        this.calendarYearTotalReturns = calendarYearTotalReturns;
    }

    public CumulativeTotalReturns getCumulativeTotalReturns() {
        return this.cumulativeTotalReturns;
    }

    public void setCumulativeTotalReturns(final CumulativeTotalReturns cumulativeTotalReturns) {
        this.cumulativeTotalReturns = cumulativeTotalReturns;
    }

    public Profile getProfile() {
        return this.Profile;
    }

    public void setProfile(final Profile profile) {
        this.Profile = profile;
    }

    public HoldingDetails getHoldingDetails() {
        return this.holdingDetails;
    }

    public void setHoldingDetails(final HoldingDetails holdingDetails) {
        this.holdingDetails = holdingDetails;
    }

    public ToNewInvestors getToNewInvestors() {
        return this.toNewInvestors;
    }

    public void setToNewInvestors(final ToNewInvestors toNewInvestors) {
        this.toNewInvestors = toNewInvestors;
    }

    public FeesAndExpenses getFeesAndExpenses() {
        return this.feesAndExpenses;
    }

    public void setFeesAndExpenses(final FeesAndExpenses feesAndExpenses) {
        this.feesAndExpenses = feesAndExpenses;
    }

    public MorningstarRatings getMorningstarRatings() {
        return this.morningstarRatings;
    }

    public void setMorningstarRatings(final MorningstarRatings morningstarRatings) {
        this.morningstarRatings = morningstarRatings;
    }

    public MgmtAndContactInfo getMgmtAndContactInfo() {
        return this.mgmtAndContactInfo;
    }

    public void setMgmtAndContactInfo(final MgmtAndContactInfo mgmtAndContactInfo) {
        this.mgmtAndContactInfo = mgmtAndContactInfo;
    }

    public Rating getRating() {
        return this.rating;
    }

    public void setRating(final Rating rating) {
        this.rating = rating;
    }


    public InvestmentStrategy getInvestmentStrategy() {
        return this.investmentStrategy;
    }


    public void setInvestmentStrategy(final InvestmentStrategy investmentStrategy) {
        this.investmentStrategy = investmentStrategy;
    }


    public YieldAndCredit getYieldAndCredit() {
        return this.yieldAndCredit;
    }


    public void setYieldAndCredit(final YieldAndCredit yieldAndCredit) {
        this.yieldAndCredit = yieldAndCredit;
    }

    public List<FundSummaryRisk> getRisk() {
        return this.risk;
    }

    public void setRisk(final List<FundSummaryRisk> risk) {
        this.risk = risk;
    }


    public List<ProdAltNumSeg> getProdAltNumSegs() {
        return this.prodAltNumSegs;
    }


    public void setProdAltNumSegs(final List<ProdAltNumSeg> prodAltNumSegs) {
        this.prodAltNumSegs = prodAltNumSegs;
    }

    public String getWeekRangeCurrency() {
        return this.weekRangeCurrency;
    }

    public void setWeekRangeCurrency(final String weekRangeCurrency) {
        this.weekRangeCurrency = weekRangeCurrency;
    }


    public class CalendarYearTotalReturns {
        List<FundCalendarYearReturn> items;
        private String bestFitIndex;
        private String lastUpdatedDate;
        private String dailyLastUpdatedDate;

        public List<FundCalendarYearReturn> getItems() {
            return this.items;
        }

        public void setItems(final List<FundCalendarYearReturn> items) {
            this.items = items;
        }

        public String getBestFitIndex() {
            return this.bestFitIndex;
        }

        public void setBestFitIndex(final String bestFitIndex) {
            this.bestFitIndex = bestFitIndex;
        }

        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }

        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

        public String getDailyLastUpdatedDate() {
            return this.dailyLastUpdatedDate;
        }

        public void setDailyLastUpdatedDate(final String dailyLastUpdatedDate) {
            this.dailyLastUpdatedDate = dailyLastUpdatedDate;
        }
    }

    public class CumulativeTotalReturns {
        List<FundCumulativeReturn> items;
        private String bestFitIndex;
        private String lastUpdatedDate;
        private String dailyLastUpdatedDate;

        public List<FundCumulativeReturn> getItems() {
            return this.items;
        }

        public void setItems(final List<FundCumulativeReturn> items) {
            this.items = items;
        }

        public String getBestFitIndex() {
            return this.bestFitIndex;
        }

        public void setBestFitIndex(final String bestFitIndex) {
            this.bestFitIndex = bestFitIndex;
        }

        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }

        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

        public String getDailyLastUpdatedDate() {
            return this.dailyLastUpdatedDate;
        }

        public void setDailyLastUpdatedDate(final String dailyLastUpdatedDate) {
            this.dailyLastUpdatedDate = dailyLastUpdatedDate;
        }
    }

    public class Profile {
        private String name;
        private String bestFitIndex;
        private String bestFitIndexCode;
        private String categoryCode;
        private String categoryName;

        private String categoryLevel1Code;

        private String categoryLevel1Name;
        private String familyName;
        private String familyCode;
        private String advisor;
        private String subAdvisor;
        private String inceptionDate;
        private String investmentObjectiveAndStrategy;
        private String hhhhCategoryCode;
        private String hhhhCategoryName;
        private BigDecimal dividendYield;
        private String distributionFrequency;
        private String riskLvlCde;
        private String currency;
        private BigDecimal expenseRatio;
        private BigDecimal marginRatio; // The Margin Ratio
        private BigDecimal initialCharge;// Initial Charge Upon Subscription
        private BigDecimal annualManagementFee;// Annual Management Fee
        private BigDecimal assetsUnderManagement;
        private String assetsUnderManagementCurrencyCode;
        private String topPerformersIndicator;// The Top Performers Indicator
        private String topSellProdIndex;// hhhh_BEST_SELLER Filtering Indicator
        private String investmentRegionCode;
        private String investmentRegionName;
        private String[] channelRestrictList;
        private String fundClassCde;
        private String amcmIndicator;
        private String nextDealDate;
        private String surveyedFundNetAssetsDate;
        private String WeekRangeLowDate;
        private String WeekRangeHighDate;
        private String riskFreeRateName;
        private String relativeRiskMeasuresIndexName;
        private String dayEndBidOfferPricesDate;
        private String dayEndNAVDate;
        private String prodStatCde;
        private String allowBuy;
        private String allowSell;
        private String allowSwInProdInd;
        private String allowSwOutProdInd;
        private String allowSellMipProdInd;
        private String annualReportDate;
        private String piFundInd;
        private String deAuthFundInd;


        public String getName() {
            return this.name;
        }


        public void setName(final String name) {
            this.name = name;
        }

        public String gethhhhCategoryCode() {
            return this.hhhhCategoryCode;
        }

        public void sethhhhCategoryCode(final String hhhhCategoryCode) {
            this.hhhhCategoryCode = hhhhCategoryCode;
        }

        public String gethhhhCategoryName() {
            return this.hhhhCategoryName;
        }

        public void sethhhhCategoryName(final String hhhhCategoryName) {
            this.hhhhCategoryName = hhhhCategoryName;
        }

        public String getBestFitIndex() {
            return this.bestFitIndex;
        }

        public void setBestFitIndex(final String bestFitIndex) {
            this.bestFitIndex = bestFitIndex;
        }

        public String getBestFitIndexCode() {
            return this.bestFitIndexCode;
        }

        public void setBestFitIndexCode(final String bestFitIndexCode) {
            this.bestFitIndexCode = bestFitIndexCode;
        }

        public String getCategoryCode() {
            return this.categoryCode;
        }

        public void setCategoryCode(final String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public String getCategoryName() {
            return this.categoryName;
        }

        public void setCategoryName(final String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryLevel1Code() {
            return this.categoryLevel1Code;
        }

        public void setCategoryLevel1Code(final String categoryLevel1Code) {
            this.categoryLevel1Code = categoryLevel1Code;
        }

        public String getCategoryLevel1Name() {
            return this.categoryLevel1Name;
        }

        public void setCategoryLevel1Name(final String categoryLevel1Name) {
            this.categoryLevel1Name = categoryLevel1Name;
        }

        public String getFamilyName() {
            return this.familyName;
        }

        public void setFamilyName(final String familyName) {
            this.familyName = familyName;
        }

        public String getFamilyCode() {
            return this.familyCode;
        }

        public void setFamilyCode(final String familyCode) {
            this.familyCode = familyCode;
        }

        public String getAdvisor() {
            return this.advisor;
        }

        public void setAdvisor(final String advisor) {
            this.advisor = advisor;
        }

        public String getSubAdvisor() {
            return this.subAdvisor;
        }

        public void setSubAdvisor(final String subAdvisor) {
            this.subAdvisor = subAdvisor;
        }

        public String getInceptionDate() {
            return this.inceptionDate;
        }

        public void setInceptionDate(final String inceptionDate) {
            this.inceptionDate = inceptionDate;
        }

        public String getInvestmentObjectiveAndStrategy() {
            return this.investmentObjectiveAndStrategy;
        }

        public void setInvestmentObjectiveAndStrategy(final String investmentObjectiveAndStrategy) {
            this.investmentObjectiveAndStrategy = investmentObjectiveAndStrategy;
        }


        public BigDecimal getDividendYield() {
            return this.dividendYield;
        }


        public void setDividendYield(final BigDecimal dividendYield) {
            this.dividendYield = dividendYield;
        }


        public String getDistributionFrequency() {
            return this.distributionFrequency;
        }


        public void setDistributionFrequency(final String distributionFrequency) {
            this.distributionFrequency = distributionFrequency;
        }


        public String getRiskLvlCde() {
            return this.riskLvlCde;
        }


        public void setRiskLvlCde(final String riskLvlCde) {
            this.riskLvlCde = riskLvlCde;
        }


        public String getCurrency() {
            return this.currency;
        }


        public void setCurrency(final String currency) {
            this.currency = currency;
        }


        public BigDecimal getExpenseRatio() {
            return this.expenseRatio;
        }


        public void setExpenseRatio(final BigDecimal expenseRatio) {
            this.expenseRatio = expenseRatio;
        }


        public BigDecimal getMarginRatio() {
            return this.marginRatio;
        }


        public void setMarginRatio(final BigDecimal marginRatio) {
            this.marginRatio = marginRatio;
        }


        public BigDecimal getInitialCharge() {
            return this.initialCharge;
        }


        public void setInitialCharge(final BigDecimal initialCharge) {
            this.initialCharge = initialCharge;
        }


        public BigDecimal getAnnualManagementFee() {
            return this.annualManagementFee;
        }


        public void setAnnualManagementFee(final BigDecimal annualManagementFee) {
            this.annualManagementFee = annualManagementFee;
        }


        public BigDecimal getAssetsUnderManagement() {
            return this.assetsUnderManagement;
        }


        public void setAssetsUnderManagement(final BigDecimal assetsUnderManagement) {
            this.assetsUnderManagement = assetsUnderManagement;
        }


        public String getAssetsUnderManagementCurrencyCode() {
            return this.assetsUnderManagementCurrencyCode;
        }


        public void setAssetsUnderManagementCurrencyCode(final String assetsUnderManagementCurrencyCode) {
            this.assetsUnderManagementCurrencyCode = assetsUnderManagementCurrencyCode;
        }


        public String getTopPerformersIndicator() {
            return this.topPerformersIndicator;
        }


        public void setTopPerformersIndicator(final String topPerformersIndicator) {
            this.topPerformersIndicator = topPerformersIndicator;
        }


        public String getTopSellProdIndex() {
            return this.topSellProdIndex;
        }


        public void setTopSellProdIndex(final String topSellProdIndex) {
            this.topSellProdIndex = topSellProdIndex;
        }


        public String getInvestmentRegionCode() {
            return this.investmentRegionCode;
        }


        public void setInvestmentRegionCode(final String investmentRegionCode) {
            this.investmentRegionCode = investmentRegionCode;
        }


        public String getInvestmentRegionName() {
            return this.investmentRegionName;
        }


        public void setInvestmentRegionName(final String investmentRegionName) {
            this.investmentRegionName = investmentRegionName;
        }


        public String[] getChannelRestrictList() {
            return this.channelRestrictList;
        }


        public void setChannelRestrictList(final String[] channelRestrictList) {
            this.channelRestrictList = channelRestrictList;
        }


        public String getFundClassCde() {
            return this.fundClassCde;
        }


        public void setFundClassCde(final String fundClassCde) {
            this.fundClassCde = fundClassCde;
        }


        public String getAmcmIndicator() {
            return this.amcmIndicator;
        }


        public void setAmcmIndicator(final String amcmIndicator) {
            this.amcmIndicator = amcmIndicator;
        }


        public String getNextDealDate() {
            return this.nextDealDate;
        }


        public void setNextDealDate(final String nextDealDate) {
            this.nextDealDate = nextDealDate;
        }


        public String getSurveyedFundNetAssetsDate() {
            return this.surveyedFundNetAssetsDate;
        }


        public void setSurveyedFundNetAssetsDate(final String surveyedFundNetAssetsDate) {
            this.surveyedFundNetAssetsDate = surveyedFundNetAssetsDate;
        }


        public String getWeekRangeLowDate() {
            return this.WeekRangeLowDate;
        }


        public void setWeekRangeLowDate(final String weekRangeLowDate) {
            this.WeekRangeLowDate = weekRangeLowDate;
        }


        public String getWeekRangeHighDate() {
            return this.WeekRangeHighDate;
        }


        public void setWeekRangeHighDate(final String weekRangeHighDate) {
            this.WeekRangeHighDate = weekRangeHighDate;
        }


        public String getRiskFreeRateName() {
            return this.riskFreeRateName;
        }


        public void setRiskFreeRateName(final String riskFreeRateName) {
            this.riskFreeRateName = riskFreeRateName;
        }


        public String getRelativeRiskMeasuresIndexName() {
            return this.relativeRiskMeasuresIndexName;
        }


        public void setRelativeRiskMeasuresIndexName(final String relativeRiskMeasuresIndexName) {
            this.relativeRiskMeasuresIndexName = relativeRiskMeasuresIndexName;
        }


        public String getDayEndBidOfferPricesDate() {
            return this.dayEndBidOfferPricesDate;
        }


        public void setDayEndBidOfferPricesDate(final String dayEndBidOfferPricesDate) {
            this.dayEndBidOfferPricesDate = dayEndBidOfferPricesDate;
        }


        public String getDayEndNAVDate() {
            return this.dayEndNAVDate;
        }


        public void setDayEndNAVDate(final String dayEndNAVDate) {
            this.dayEndNAVDate = dayEndNAVDate;
        }


        public String getProdStatCde() {
            return this.prodStatCde;
        }


        public void setProdStatCde(final String prodStatCde) {
            this.prodStatCde = prodStatCde;
        }


        public String getAllowBuy() {
            return this.allowBuy;
        }


        public void setAllowBuy(final String allowBuy) {
            this.allowBuy = allowBuy;
        }


        public String getAllowSell() {
            return this.allowSell;
        }


        public void setAllowSell(final String allowSell) {
            this.allowSell = allowSell;
        }


        public String getAllowSwInProdInd() {
            return this.allowSwInProdInd;
        }


        public void setAllowSwInProdInd(final String allowSwInProdInd) {
            this.allowSwInProdInd = allowSwInProdInd;
        }


        public String getAllowSwOutProdInd() {
            return this.allowSwOutProdInd;
        }


        public void setAllowSwOutProdInd(final String allowSwOutProdInd) {
            this.allowSwOutProdInd = allowSwOutProdInd;
        }


        public String getAllowSellMipProdInd() {
            return this.allowSellMipProdInd;
        }


        public void setAllowSellMipProdInd(final String allowSellMipProdInd) {
            this.allowSellMipProdInd = allowSellMipProdInd;
        }


        public String getAnnualReportDate() {
            return this.annualReportDate;
        }


        public void setAnnualReportDate(final String annualReportDate) {
            this.annualReportDate = annualReportDate;
        }


        public String getPiFundInd() {
            return this.piFundInd;
        }


        public void setPiFundInd(final String piFundInd) {
            this.piFundInd = piFundInd;
        }


        public String getDeAuthFundInd() {
            return this.deAuthFundInd;
        }


        public void setDeAuthFundInd(final String deAuthFundInd) {
            this.deAuthFundInd = deAuthFundInd;
        }

    }


    public class HoldingDetails {
        private String totalNetAssets;
        private String totalNetAssetsCurrencyCode;
        private BigDecimal annualPortfolioTurnover;
        private String equityStyle;
        private String fixedIncomeStyle;
        private BigDecimal sharesOutstanding;
        private BigDecimal netAssetValue;
        private String netAssetValueCurrencyCode;
        private String premiumDiscountToNAV;
        private BigDecimal priceEarningsRatio;
        private BigDecimal dividendYield;
        private String dividendPerShare;
        private String dividendPerShareCurrency;
        private String dividendPerShareCurrencyCode;
        private String exDividendDate;
        private String dividendPaymentDate;
        private BigDecimal beta;
        private String taxAdjustedRating;
        private String lastUpdatedDate;


        public String getTotalNetAssets() {
            return this.totalNetAssets;
        }

        public void setTotalNetAssets(final String totalNetAssets) {
            this.totalNetAssets = totalNetAssets;
        }

        public String getTotalNetAssetsCurrencyCode() {
            return this.totalNetAssetsCurrencyCode;
        }

        public void setTotalNetAssetsCurrencyCode(final String totalNetAssetsCurrencyCode) {
            this.totalNetAssetsCurrencyCode = totalNetAssetsCurrencyCode;
        }


        public BigDecimal getAnnualPortfolioTurnover() {
            return this.annualPortfolioTurnover;
        }


        public void setAnnualPortfolioTurnover(final BigDecimal annualPortfolioTurnover) {
            this.annualPortfolioTurnover = annualPortfolioTurnover;
        }

        public String getEquityStyle() {
            return this.equityStyle;
        }

        public void setEquityStyle(final String equityStyle) {
            this.equityStyle = equityStyle;
        }

        public String getFixedIncomeStyle() {
            return this.fixedIncomeStyle;
        }

        public void setFixedIncomeStyle(final String fixedIncomeStyle) {
            this.fixedIncomeStyle = fixedIncomeStyle;
        }


        public BigDecimal getSharesOutstanding() {
            return this.sharesOutstanding;
        }


        public void setSharesOutstanding(final BigDecimal sharesOutstanding) {
            this.sharesOutstanding = sharesOutstanding;
        }


        public BigDecimal getNetAssetValue() {
            return this.netAssetValue;
        }


        public void setNetAssetValue(final BigDecimal netAssetValue) {
            this.netAssetValue = netAssetValue;
        }

        public String getNetAssetValueCurrencyCode() {
            return this.netAssetValueCurrencyCode;
        }

        public void setNetAssetValueCurrencyCode(final String netAssetValueCurrencyCode) {
            this.netAssetValueCurrencyCode = netAssetValueCurrencyCode;
        }

        public String getPremiumDiscountToNAV() {
            return this.premiumDiscountToNAV;
        }

        public void setPremiumDiscountToNAV(final String premiumDiscountToNAV) {
            this.premiumDiscountToNAV = premiumDiscountToNAV;
        }


        public BigDecimal getPriceEarningsRatio() {
            return this.priceEarningsRatio;
        }


        public void setPriceEarningsRatio(final BigDecimal priceEarningsRatio) {
            this.priceEarningsRatio = priceEarningsRatio;
        }


        public BigDecimal getDividendYield() {
            return this.dividendYield;
        }


        public void setDividendYield(final BigDecimal dividendYield) {
            this.dividendYield = dividendYield;
        }

        public String getDividendPerShare() {
            return this.dividendPerShare;
        }

        public void setDividendPerShare(final String dividendPerShare) {
            this.dividendPerShare = dividendPerShare;
        }

        public String getDividendPerShareCurrency() {
            return this.dividendPerShareCurrency;
        }

        public void setDividendPerShareCurrency(final String dividendPerShareCurrency) {
            this.dividendPerShareCurrency = dividendPerShareCurrency;
        }

        public String getDividendPerShareCurrencyCode() {
            return this.dividendPerShareCurrencyCode;
        }

        public void setDividendPerShareCurrencyCode(final String dividendPerShareCurrencyCode) {
            this.dividendPerShareCurrencyCode = dividendPerShareCurrencyCode;
        }

        public String getExDividendDate() {
            return this.exDividendDate;
        }

        public void setExDividendDate(final String exDividendDate) {
            this.exDividendDate = exDividendDate;
        }

        public String getDividendPaymentDate() {
            return this.dividendPaymentDate;
        }

        public void setDividendPaymentDate(final String dividendPaymentDate) {
            this.dividendPaymentDate = dividendPaymentDate;
        }


        public BigDecimal getBeta() {
            return this.beta;
        }


        public void setBeta(final BigDecimal beta) {
            this.beta = beta;
        }

        public String getTaxAdjustedRating() {
            return this.taxAdjustedRating;
        }

        public void setTaxAdjustedRating(final String taxAdjustedRating) {
            this.taxAdjustedRating = taxAdjustedRating;
        }


        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }


        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

    }


    public class ToNewInvestors {

        private BigDecimal minInitInvst;
        private String minInitInvstCurrencyCode;
        private BigDecimal minSubqInvst;
        private String minSubqInvstCurrencyCode;
        private BigDecimal minInitRRSPInvst;
        private String minInitRRSPInvstCurrencyCode;
        private BigDecimal minSubqRRSPInvst;
        private String minSubqRRSPInvstCurrencyCode;
        private BigDecimal hhhhMinInitInvst;
        private String hhhhMinInitInvstCurrencyCode;
        private BigDecimal hhhhMinSubqInvst;
        private String hhhhMinSubqInvstCurrencyCode;
        private String purchaseCurId;
        private String minInitUnit;
        private String minSubqUnit;
        private Boolean indicator;


        public BigDecimal getMinInitInvst() {
            return this.minInitInvst;
        }


        public void setMinInitInvst(final BigDecimal minInitInvst) {
            this.minInitInvst = minInitInvst;
        }


        public BigDecimal getMinSubqInvst() {
            return this.minSubqInvst;
        }


        public void setMinSubqInvst(final BigDecimal minSubqInvst) {
            this.minSubqInvst = minSubqInvst;
        }


        public BigDecimal getMinInitRRSPInvst() {
            return this.minInitRRSPInvst;
        }


        public void setMinInitRRSPInvst(final BigDecimal minInitRRSPInvst) {
            this.minInitRRSPInvst = minInitRRSPInvst;
        }


        public BigDecimal getMinSubqRRSPInvst() {
            return this.minSubqRRSPInvst;
        }


        public void setMinSubqRRSPInvst(final BigDecimal minSubqRRSPInvst) {
            this.minSubqRRSPInvst = minSubqRRSPInvst;
        }


        public BigDecimal gethhhhMinInitInvst() {
            return this.hhhhMinInitInvst;
        }


        public void sethhhhMinInitInvst(final BigDecimal hhhhMinInitInvst) {
            this.hhhhMinInitInvst = hhhhMinInitInvst;
        }


        public BigDecimal gethhhhMinSubqInvst() {
            return this.hhhhMinSubqInvst;
        }


        public void sethhhhMinSubqInvst(final BigDecimal hhhhMinSubqInvst) {
            this.hhhhMinSubqInvst = hhhhMinSubqInvst;
        }

        public String getMinInitRRSPInvstCurrencyCode() {
            return this.minInitRRSPInvstCurrencyCode;
        }

        public void setMinInitRRSPInvstCurrencyCode(final String minInitRRSPInvstCurrencyCode) {
            this.minInitRRSPInvstCurrencyCode = minInitRRSPInvstCurrencyCode;
        }

        public String getMinSubqRRSPInvstCurrencyCode() {
            return this.minSubqRRSPInvstCurrencyCode;
        }

        public void setMinSubqRRSPInvstCurrencyCode(final String minSubqRRSPInvstCurrencyCode) {
            this.minSubqRRSPInvstCurrencyCode = minSubqRRSPInvstCurrencyCode;
        }


        public String getMinInitInvstCurrencyCode() {
            return this.minInitInvstCurrencyCode;
        }

        public void setMinInitInvstCurrencyCode(final String minInitInvstCurrencyCode) {
            this.minInitInvstCurrencyCode = minInitInvstCurrencyCode;
        }


        public String getMinSubqInvstCurrencyCode() {
            return this.minSubqInvstCurrencyCode;
        }

        public void setMinSubqInvstCurrencyCode(final String minSubqInvstCurrencyCode) {
            this.minSubqInvstCurrencyCode = minSubqInvstCurrencyCode;
        }



        public String gethhhhMinInitInvstCurrencyCode() {
            return this.hhhhMinInitInvstCurrencyCode;
        }


        public void sethhhhMinInitInvstCurrencyCode(final String hhhhMinInitInvstCurrencyCode) {
            this.hhhhMinInitInvstCurrencyCode = hhhhMinInitInvstCurrencyCode;
        }


        public String gethhhhMinSubqInvstCurrencyCode() {
            return this.hhhhMinSubqInvstCurrencyCode;
        }


        public void sethhhhMinSubqInvstCurrencyCode(final String hhhhMinSubqInvstCurrencyCode) {
            this.hhhhMinSubqInvstCurrencyCode = hhhhMinSubqInvstCurrencyCode;
        }

        public String getPurchaseCurId() {
            return this.purchaseCurId;
        }

        public void setPurchaseCurId(final String purchaseCurId) {
            this.purchaseCurId = purchaseCurId;
        }

        public String getMinInitUnit() {
            return this.minInitUnit;
        }

        public void setMinInitUnit(final String minInitUnit) {
            this.minInitUnit = minInitUnit;
        }

        public String getMinSubqUnit() {
            return this.minSubqUnit;
        }

        public void setMinSubqUnit(final String minSubqUnit) {
            this.minSubqUnit = minSubqUnit;
        }


        public Boolean getIndicator() {
            return this.indicator;
        }


        public void setIndicator(final Boolean indicator) {
            this.indicator = indicator;
        }

    }


    public class FeesAndExpenses {

        private BigDecimal maximumInitialSalesFees;
        private BigDecimal maximumDeferredSalesFees;
        private BigDecimal actualManagementFee;
        private String prospectusCustodianUnit;
        private BigDecimal prospectusCustodianFee;
        private BigDecimal actualMER;
        private String loadType;
        private String lastUpdatedDate;
        private BigDecimal onGoingChargeFigure;
        private BigDecimal actualFrontLoad;


        public BigDecimal getMaximumInitialSalesFees() {
            return this.maximumInitialSalesFees;
        }


        public void setMaximumInitialSalesFees(final BigDecimal maximumInitialSalesFees) {
            this.maximumInitialSalesFees = maximumInitialSalesFees;
        }


        public BigDecimal getMaximumDeferredSalesFees() {
            return this.maximumDeferredSalesFees;
        }


        public void setMaximumDeferredSalesFees(final BigDecimal maximumDeferredSalesFees) {
            this.maximumDeferredSalesFees = maximumDeferredSalesFees;
        }


        public BigDecimal getActualManagementFee() {
            return this.actualManagementFee;
        }


        public void setActualManagementFee(final BigDecimal actualManagementFee) {
            this.actualManagementFee = actualManagementFee;
        }


        public String getProspectusCustodianUnit() {
            return this.prospectusCustodianUnit;
        }


        public void setProspectusCustodianUnit(final String prospectusCustodianUnit) {
            this.prospectusCustodianUnit = prospectusCustodianUnit;
        }


        public BigDecimal getProspectusCustodianFee() {
            return this.prospectusCustodianFee;
        }


        public void setProspectusCustodianFee(final BigDecimal prospectusCustodianFee) {
            this.prospectusCustodianFee = prospectusCustodianFee;
        }


        public BigDecimal getActualMER() {
            return this.actualMER;
        }


        public void setActualMER(final BigDecimal actualMER) {
            this.actualMER = actualMER;
        }

        public String getLoadType() {
            return this.loadType;
        }

        public void setLoadType(final String loadType) {
            this.loadType = loadType;
        }

        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }

        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }


        public BigDecimal getOnGoingChargeFigure() {
            return this.onGoingChargeFigure;
        }


        public void setOnGoingChargeFigure(final BigDecimal onGoingChargeFigure) {
            this.onGoingChargeFigure = onGoingChargeFigure;
        }


        public BigDecimal getActualFrontLoad() {
            return this.actualFrontLoad;
        }


        public void setActualFrontLoad(final BigDecimal actualFrontLoad) {
            this.actualFrontLoad = actualFrontLoad;
        }

    }


    public class MorningstarRatings {
        private String morningstarRatingOverall;
        private BigDecimal morningstarRating3Yr;
        private BigDecimal morningstarRating5Yr;
        private BigDecimal morningstarRating10Yr;
        private String morningstarTaxAdjustedRatingOverall;
        private BigDecimal morningstarTaxAdjustedRating3Yr;
        private BigDecimal morningstarTaxAdjustedRating5Yr;
        private BigDecimal morningstarTaxAdjustedRating10Yr;
        private String lastUpdatedDate;

        public String getMorningstarRatingOverall() {
            return this.morningstarRatingOverall;
        }

        public void setMorningstarRatingOverall(final String morningstarRatingOverall) {
            this.morningstarRatingOverall = morningstarRatingOverall;
        }


        public BigDecimal getMorningstarRating3Yr() {
            return this.morningstarRating3Yr;
        }


        public void setMorningstarRating3Yr(final BigDecimal morningstarRating3Yr) {
            this.morningstarRating3Yr = morningstarRating3Yr;
        }


        public BigDecimal getMorningstarRating5Yr() {
            return this.morningstarRating5Yr;
        }


        public void setMorningstarRating5Yr(final BigDecimal morningstarRating5Yr) {
            this.morningstarRating5Yr = morningstarRating5Yr;
        }


        public BigDecimal getMorningstarRating10Yr() {
            return this.morningstarRating10Yr;
        }


        public void setMorningstarRating10Yr(final BigDecimal morningstarRating10Yr) {
            this.morningstarRating10Yr = morningstarRating10Yr;
        }

        public String getMorningstarTaxAdjustedRatingOverall() {
            return this.morningstarTaxAdjustedRatingOverall;
        }

        public void setMorningstarTaxAdjustedRatingOverall(final String morningstarTaxAdjustedRatingOverall) {
            this.morningstarTaxAdjustedRatingOverall = morningstarTaxAdjustedRatingOverall;
        }


        public BigDecimal getMorningstarTaxAdjustedRating3Yr() {
            return this.morningstarTaxAdjustedRating3Yr;
        }


        public void setMorningstarTaxAdjustedRating3Yr(final BigDecimal morningstarTaxAdjustedRating3Yr) {
            this.morningstarTaxAdjustedRating3Yr = morningstarTaxAdjustedRating3Yr;
        }


        public BigDecimal getMorningstarTaxAdjustedRating5Yr() {
            return this.morningstarTaxAdjustedRating5Yr;
        }


        public void setMorningstarTaxAdjustedRating5Yr(final BigDecimal morningstarTaxAdjustedRating5Yr) {
            this.morningstarTaxAdjustedRating5Yr = morningstarTaxAdjustedRating5Yr;
        }


        public BigDecimal getMorningstarTaxAdjustedRating10Yr() {
            return this.morningstarTaxAdjustedRating10Yr;
        }


        public void setMorningstarTaxAdjustedRating10Yr(final BigDecimal morningstarTaxAdjustedRating10Yr) {
            this.morningstarTaxAdjustedRating10Yr = morningstarTaxAdjustedRating10Yr;
        }

        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }

        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }
    }


    public class MgmtAndContactInfo {
        private String companyName;
        private String address;
        private String city;
        private String province;
        private String country;
        private String postalCode;
        private String telephoneNo;
        private String faxNo;
        private String website;
        List<ManagemenInfo> mgmtInfos;

        public String getCompanyName() {
            return this.companyName;
        }

        public void setCompanyName(final String companyName) {
            this.companyName = companyName;
        }

        public String getAddress() {
            return this.address;
        }

        public void setAddress(final String address) {
            this.address = address;
        }

        public String getCity() {
            return this.city;
        }

        public void setCity(final String city) {
            this.city = city;
        }

        public String getProvince() {
            return this.province;
        }

        public void setProvince(final String province) {
            this.province = province;
        }

        public String getTelephoneNo() {
            return this.telephoneNo;
        }

        public void setTelephoneNo(final String telephoneNo) {
            this.telephoneNo = telephoneNo;
        }

        public String getFaxNo() {
            return this.faxNo;
        }

        public void setFaxNo(final String faxNo) {
            this.faxNo = faxNo;
        }

        public String getWebsite() {
            return this.website;
        }

        public void setWebsite(final String website) {
            this.website = website;
        }


        public String getCountry() {
            return this.country;
        }


        public void setCountry(final String country) {
            this.country = country;
        }

        public String getPostalCode() {
            return this.postalCode;
        }

        public void setPostalCode(final String postalCode) {
            this.postalCode = postalCode;
        }


        public List<ManagemenInfo> getMgmtInfos() {
            return this.mgmtInfos;
        }


        public void setMgmtInfos(final List<ManagemenInfo> mgmtInfos) {
            this.mgmtInfos = mgmtInfos;
        }
    }


    public class Rating {


        // private String morningstarRating;


        // private Integer taxAdjustedRating;

        private String averageCreditQualityName;


        private Integer averageCreditQuality;

        private String averageCreditQualityDate;


        private BigDecimal rank1Yr;


        private BigDecimal rank3Yr;


        private BigDecimal rank5Yr;


        private BigDecimal rank10Yr;

        private String ratingDate;

        //
        // public String getMorningstarRating() {
        // return this.morningstarRating;
        // }
        //
        //
        // public void setMorningstarRating(final String morningstarRating) {
        // this.morningstarRating = morningstarRating;
        // }
        //
        //
        // public Integer getTaxAdjustedRating() {
        // return this.taxAdjustedRating;
        // }
        //
        //
        // public void setTaxAdjustedRating(final Integer taxAdjustedRating) {
        // this.taxAdjustedRating = taxAdjustedRating;
        // }



        public Integer getAverageCreditQuality() {
            return this.averageCreditQuality;
        }


        public String getAverageCreditQualityName() {
            return this.averageCreditQualityName;
        }


        public void setAverageCreditQualityName(final String averageCreditQualityName) {
            this.averageCreditQualityName = averageCreditQualityName;
        }


        public void setAverageCreditQuality(final Integer averageCreditQuality) {
            this.averageCreditQuality = averageCreditQuality;
        }


        public String getAverageCreditQualityDate() {
            return this.averageCreditQualityDate;
        }


        public void setAverageCreditQualityDate(final String averageCreditQualityDate) {
            this.averageCreditQualityDate = averageCreditQualityDate;
        }


        public BigDecimal getRank1Yr() {
            return this.rank1Yr;
        }


        public void setRank1Yr(final BigDecimal rank1Yr) {
            this.rank1Yr = rank1Yr;
        }


        public BigDecimal getRank3Yr() {
            return this.rank3Yr;
        }


        public void setRank3Yr(final BigDecimal rank3Yr) {
            this.rank3Yr = rank3Yr;
        }


        public BigDecimal getRank5Yr() {
            return this.rank5Yr;
        }


        public void setRank5Yr(final BigDecimal rank5Yr) {
            this.rank5Yr = rank5Yr;
        }


        public BigDecimal getRank10Yr() {
            return this.rank10Yr;
        }


        public void setRank10Yr(final BigDecimal rank10Yr) {
            this.rank10Yr = rank10Yr;
        }


        public String getRatingDate() {
            return this.ratingDate;
        }


        public void setRatingDate(final String ratingDate) {
            this.ratingDate = ratingDate;
        }
    }

    public class InvestmentStrategy {
        private String investmentStyle;
        private String interestRateSensitivity;
        private BigDecimal assetAllocBondNet;
        private BigDecimal preferredStockNet;
        private BigDecimal assetAllocCashNet;
        private BigDecimal otherNet;
        private BigDecimal assetAllocEquityNet;


        public String getInvestmentStyle() {
            return this.investmentStyle;
        }


        public void setInvestmentStyle(final String investmentStyle) {
            this.investmentStyle = investmentStyle;
        }


        public String getInterestRateSensitivity() {
            return this.interestRateSensitivity;
        }


        public void setInterestRateSensitivity(final String interestRateSensitivity) {
            this.interestRateSensitivity = interestRateSensitivity;
        }


        public BigDecimal getAssetAllocBondNet() {
            return this.assetAllocBondNet;
        }


        public void setAssetAllocBondNet(final BigDecimal assetAllocBondNet) {
            this.assetAllocBondNet = assetAllocBondNet;
        }


        public BigDecimal getPreferredStockNet() {
            return this.preferredStockNet;
        }


        public void setPreferredStockNet(final BigDecimal preferredStockNet) {
            this.preferredStockNet = preferredStockNet;
        }


        public BigDecimal getAssetAllocCashNet() {
            return this.assetAllocCashNet;
        }


        public void setAssetAllocCashNet(final BigDecimal assetAllocCashNet) {
            this.assetAllocCashNet = assetAllocCashNet;
        }


        public BigDecimal getOtherNet() {
            return this.otherNet;
        }


        public void setOtherNet(final BigDecimal otherNet) {
            this.otherNet = otherNet;
        }


        public BigDecimal getAssetAllocEquityNet() {
            return this.assetAllocEquityNet;
        }


        public void setAssetAllocEquityNet(final BigDecimal assetAllocEquityNet) {
            this.assetAllocEquityNet = assetAllocEquityNet;
        }

    }

    public class YieldAndCredit {
        private BigDecimal averageCurrentYield;
        private BigDecimal averageYieldToMaturity;
        private BigDecimal averageDuration;
        private String lastUpdatedDate;
        private BigDecimal creditQualA;
        private BigDecimal creditQualAA;
        private BigDecimal creditQualAAA;
        private BigDecimal creditQualB;
        private BigDecimal creditQualBB;
        private BigDecimal creditQualBBB;
        private BigDecimal creditQualBelowB;
        private BigDecimal creditQualNotRated;
        private String creditQualDate;


        public BigDecimal getAverageCurrentYield() {
            return this.averageCurrentYield;
        }


        public void setAverageCurrentYield(final BigDecimal averageCurrentYield) {
            this.averageCurrentYield = averageCurrentYield;
        }


        public BigDecimal getAverageYieldToMaturity() {
            return this.averageYieldToMaturity;
        }


        public void setAverageYieldToMaturity(final BigDecimal averageYieldToMaturity) {
            this.averageYieldToMaturity = averageYieldToMaturity;
        }


        public BigDecimal getAverageDuration() {
            return this.averageDuration;
        }


        public void setAverageDuration(final BigDecimal averageDuration) {
            this.averageDuration = averageDuration;
        }


        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }


        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }


        public BigDecimal getCreditQualA() {
            return this.creditQualA;
        }


        public void setCreditQualA(final BigDecimal creditQualA) {
            this.creditQualA = creditQualA;
        }


        public BigDecimal getCreditQualAA() {
            return this.creditQualAA;
        }


        public void setCreditQualAA(final BigDecimal creditQualAA) {
            this.creditQualAA = creditQualAA;
        }


        public BigDecimal getCreditQualAAA() {
            return this.creditQualAAA;
        }


        public void setCreditQualAAA(final BigDecimal creditQualAAA) {
            this.creditQualAAA = creditQualAAA;
        }


        public BigDecimal getCreditQualB() {
            return this.creditQualB;
        }


        public void setCreditQualB(final BigDecimal creditQualB) {
            this.creditQualB = creditQualB;
        }


        public BigDecimal getCreditQualBB() {
            return this.creditQualBB;
        }


        public void setCreditQualBB(final BigDecimal creditQualBB) {
            this.creditQualBB = creditQualBB;
        }


        public BigDecimal getCreditQualBBB() {
            return this.creditQualBBB;
        }


        public void setCreditQualBBB(final BigDecimal creditQualBBB) {
            this.creditQualBBB = creditQualBBB;
        }


        public BigDecimal getCreditQualBelowB() {
            return this.creditQualBelowB;
        }


        public void setCreditQualBelowB(final BigDecimal creditQualBelowB) {
            this.creditQualBelowB = creditQualBelowB;
        }


        public BigDecimal getCreditQualNotRated() {
            return this.creditQualNotRated;
        }


        public void setCreditQualNotRated(final BigDecimal creditQualNotRated) {
            this.creditQualNotRated = creditQualNotRated;
        }


        public String getCreditQualDate() {
            return this.creditQualDate;
        }


        public void setCreditQualDate(final String creditQualDate) {
            this.creditQualDate = creditQualDate;
        }

    }
}