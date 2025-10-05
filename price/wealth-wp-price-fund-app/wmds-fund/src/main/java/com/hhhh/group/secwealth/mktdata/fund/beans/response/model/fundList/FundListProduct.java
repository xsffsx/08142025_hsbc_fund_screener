
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList;

import java.math.BigDecimal;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;




public class FundListProduct {


    private Header header;


    private Summary summary;


    private Profile profile;


    private Performance performance;

    private MorningstarRatings morningstarRatings;


    private MgmtAndContactInfo mgmtAndContactInfo;


    private InvestmentStrategy investmentStrategy;


    private YieldAndCredit yieldAndCredit;

    private HoldingDetails holdingDetails;

    private PurchaseInfo purchaseInfo;


    private Rating rating;


    private List<FundListRisk> risk;


    private TopTenHoldings topTenHoldings;


    private AssetAllocations assetAlloc;


    private GlobalStockSectors stockSectors;


    private RegionalExposures equityRegional;


    private GlobalBondSectors bondSectors;


    private BondRegionalExposures bondRegional;

    private String lastUpdatedDate;

    private String prodAltNumXCode;

    private String symbol;


    private String[] swithableGroup;


    public Header getHeader() {
        return this.header;
    }


    public void setHeader(final Header header) {
        this.header = header;
    }


    public Summary getSummary() {
        return this.summary;
    }


    public void setSummary(final Summary summary) {
        this.summary = summary;
    }


    public Profile getProfile() {
        return this.profile;
    }


    public void setProfile(final Profile profile) {
        this.profile = profile;
    }


    public Performance getPerformance() {
        return this.performance;
    }


    public void setPerformance(final Performance performance) {
        this.performance = performance;
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


    public HoldingDetails getHoldingDetails() {
        return this.holdingDetails;
    }


    public void setHoldingDetails(final HoldingDetails holdingDetails) {
        this.holdingDetails = holdingDetails;
    }


    public PurchaseInfo getPurchaseInfo() {
        return this.purchaseInfo;
    }


    public void setPurchaseInfo(final PurchaseInfo purchaseInfo) {
        this.purchaseInfo = purchaseInfo;
    }


    public Rating getRating() {
        return this.rating;
    }


    public void setRating(final Rating rating) {
        this.rating = rating;
    }


    public List<FundListRisk> getRisk() {
        return this.risk;
    }


    public void setRisk(final List<FundListRisk> risk) {
        this.risk = risk;
    }


    public TopTenHoldings getTopTenHoldings() {
        return this.topTenHoldings;
    }


    public void setTopTenHoldings(final TopTenHoldings topTenHoldings) {
        this.topTenHoldings = topTenHoldings;
    }


    public AssetAllocations getAssetAlloc() {
        return this.assetAlloc;
    }


    public void setAssetAlloc(final AssetAllocations assetAlloc) {
        this.assetAlloc = assetAlloc;
    }


    public GlobalStockSectors getStockSectors() {
        return this.stockSectors;
    }


    public void setStockSectors(final GlobalStockSectors stockSectors) {
        this.stockSectors = stockSectors;
    }


    public RegionalExposures getEquityRegional() {
        return this.equityRegional;
    }


    public void setEquityRegional(final RegionalExposures equityRegional) {
        this.equityRegional = equityRegional;
    }


    public GlobalBondSectors getBondSectors() {
        return this.bondSectors;
    }


    public void setBondSectors(final GlobalBondSectors bondSectors) {
        this.bondSectors = bondSectors;
    }


    public BondRegionalExposures getBondRegional() {
        return this.bondRegional;
    }


    public void setBondRegional(final BondRegionalExposures bondRegional) {
        this.bondRegional = bondRegional;
    }


    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }


    public void setLastUpdatedDate(final String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }


    public String getProdAltNumXCode() {
        return this.prodAltNumXCode;
    }


    public void setProdAltNumXCode(final String prodAltNumXCode) {
        this.prodAltNumXCode = prodAltNumXCode;
    }


    public String getSymbol() {
        return this.symbol;
    }


    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }


    public String[] getSwithableGroup() {
        return this.swithableGroup;
    }


    public void setSwithableGroup(final String[] swithableGroup) {
        this.swithableGroup = swithableGroup;
    }



    public class Header {


        private String market;


        private String productType;


        private String currency;


        private List<ProdAltNumSeg> prodAltNumSeg;

        public List<ProdAltNumSeg> getProdAltNumSeg() {
            return this.prodAltNumSeg;
        }

        public void setProdAltNumSeg(final List<ProdAltNumSeg> prodAltNumSeg) {
            this.prodAltNumSeg = prodAltNumSeg;
        }


        public String getMarket() {
            return this.market;
        }


        public void setMarket(final String market) {
            this.market = market;
        }


        public String getProductType() {
            return this.productType;
        }


        public void setProductType(final String productType) {
            this.productType = productType;
        }


        public String getCurrency() {
            return this.currency;
        }


        public void setCurrency(final String currency) {
            this.currency = currency;
        }

    }



    public class Summary {

        private BigDecimal bid;
        private BigDecimal offer;
        private BigDecimal weekRangeLow;
        private BigDecimal weekRangeHigh;
        private String weekRangeCurrency;


        private BigDecimal lastPrice;


        private BigDecimal dayEndNAV;


        private String dayEndNAVCurrencyCode;


        private BigDecimal changeAmountNAV;


        private BigDecimal changePercentageNAV;


        private String asOfDate;


        private BigDecimal averageDailyVolume;


        private Long totalNetAsset;


        private String totalNetAssetCurrencyCode;


        private Integer ratingOverall;


        private BigDecimal mer;


        private BigDecimal yield1Yr;


        private BigDecimal switchInMinAmount;


        private String switchInMinAmountCurrencyCode;

        private BigDecimal annualReportOngoingCharge;

        private BigDecimal actualManagementFee;

        private String endDate;


        public BigDecimal getBid() {
            return this.bid;
        }


        public void setBid(final BigDecimal bid) {
            this.bid = bid;
        }


        public BigDecimal getOffer() {
            return this.offer;
        }


        public void setOffer(final BigDecimal offer) {
            this.offer = offer;
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


        public String getWeekRangeCurrency() {
            return this.weekRangeCurrency;
        }


        public void setWeekRangeCurrency(final String weekRangeCurrency) {
            this.weekRangeCurrency = weekRangeCurrency;
        }


        public BigDecimal getLastPrice() {
            return this.lastPrice;
        }


        public void setLastPrice(final BigDecimal lastPrice) {
            this.lastPrice = lastPrice;
        }


        public BigDecimal getDayEndNAV() {
            return this.dayEndNAV;
        }


        public void setDayEndNAV(final BigDecimal dayEndNAV) {
            this.dayEndNAV = dayEndNAV;
        }


        public String getDayEndNAVCurrencyCode() {
            return this.dayEndNAVCurrencyCode;
        }


        public void setDayEndNAVCurrencyCode(final String dayEndNAVCurrencyCode) {
            this.dayEndNAVCurrencyCode = dayEndNAVCurrencyCode;
        }


        public BigDecimal getChangeAmountNAV() {
            return this.changeAmountNAV;
        }


        public void setChangeAmountNAV(final BigDecimal changeAmountNAV) {
            this.changeAmountNAV = changeAmountNAV;
        }


        public BigDecimal getChangePercentageNAV() {
            return this.changePercentageNAV;
        }


        public void setChangePercentageNAV(final BigDecimal changePercentageNAV) {
            this.changePercentageNAV = changePercentageNAV;
        }


        public String getAsOfDate() {
            return this.asOfDate;
        }


        public void setAsOfDate(final String asOfDate) {
            this.asOfDate = asOfDate;
        }


        public BigDecimal getAverageDailyVolume() {
            return this.averageDailyVolume;
        }


        public void setAverageDailyVolume(final BigDecimal averageDailyVolume) {
            this.averageDailyVolume = averageDailyVolume;
        }


        public Long getTotalNetAsset() {
            return this.totalNetAsset;
        }


        public void setTotalNetAsset(final Long totalNetAsset) {
            this.totalNetAsset = totalNetAsset;
        }


        public String getTotalNetAssetCurrencyCode() {
            return this.totalNetAssetCurrencyCode;
        }


        public void setTotalNetAssetCurrencyCode(final String totalNetAssetCurrencyCode) {
            this.totalNetAssetCurrencyCode = totalNetAssetCurrencyCode;
        }


        public Integer getRatingOverall() {
            return this.ratingOverall;
        }


        public void setRatingOverall(final Integer ratingOverall) {
            this.ratingOverall = ratingOverall;
        }


        public BigDecimal getMer() {
            return this.mer;
        }


        public void setMer(final BigDecimal mer) {
            this.mer = mer;
        }


        public BigDecimal getYield1Yr() {
            return this.yield1Yr;
        }


        public void setYield1Yr(final BigDecimal yield1Yr) {
            this.yield1Yr = yield1Yr;
        }


        public BigDecimal getSwitchInMinAmount() {
            return this.switchInMinAmount;
        }


        public void setSwitchInMinAmount(final BigDecimal switchInMinAmount) {
            this.switchInMinAmount = switchInMinAmount;
        }


        public String getSwitchInMinAmountCurrencyCode() {
            return this.switchInMinAmountCurrencyCode;
        }


        public void setSwitchInMinAmountCurrencyCode(final String switchInMinAmountCurrencyCode) {
            this.switchInMinAmountCurrencyCode = switchInMinAmountCurrencyCode;
        }


        public BigDecimal getAnnualReportOngoingCharge() {
            return this.annualReportOngoingCharge;
        }


        public void setAnnualReportOngoingCharge(final BigDecimal annualReportOngoingCharge) {
            this.annualReportOngoingCharge = annualReportOngoingCharge;
        }


        public BigDecimal getActualManagementFee() {
            return this.actualManagementFee;
        }


        public void setActualManagementFee(final BigDecimal actualManagementFee) {
            this.actualManagementFee = actualManagementFee;
        }


        public String getEndDate() {
            return this.endDate;
        }


        public void setEndDate(final String endDate) {
            this.endDate = endDate;
        }

    }



    public class Profile {

        private String name;

        private String categoryCode;

        private String categoryName;

        private String familyCode;

        private String familyName;


        private String categoryLevel1Code;


        private String categoryLevel1Name;


        private String companyName;

        private String investmentRegionCode;

        private String investmentRegionName;


        private BigDecimal priceQuote;


        private String priceQuoteCurrency;


        private BigDecimal changeAmount;


        private BigDecimal changePercent;

        private BigDecimal assetsUnderManagement;

        private String assetsUnderManagementCurrencyCode;

        private String riskLvlCde;


        private String inceptionDate;

        private String productCurrency;


        private BigDecimal turnoverRatio;


        private BigDecimal stdDev3Yr;


        private Integer equityStylebox;


        private BigDecimal expenseRatio;


        private BigDecimal marginRatio;


        private BigDecimal initialCharge;


        private BigDecimal annualManagementFee;


        private BigDecimal distributionYield;


        private String distributionFrequency;

        private BigDecimal dividendYield;


        private BigDecimal prodTopSellRankNum;


        private String topSellProdIndex;


        private String topPerformersIndicator;


        private String prodLaunchDate;

        private String investmentObjectiveAndStrategy;


        private String exchangeUpdatedTime;

        private String endDateYearRisk;

        private String endDateRiskLvlCde;

        private String WeekRangeLowDate;

        private String WeekRangeHighDate;

        private String riskFreeRateName;

        private String relativeRiskMeasuresIndexName;

        private String dayEndBidOfferPricesDate;

        private String dayEndNAVDate;

        private String prodStatCde;

        private String amcmIndicator;

        private String nextDealDate;

        private String allowBuy;

        private String allowSell;

        private String allowSwInProdInd;

        private String allowSwOutProdInd;

        private String allowSellMipProdInd;

        private String piFundInd;

        private String deAuthFundInd;

        private String surveyedFundNetAssetsDate;


        public String getName() {
            return this.name;
        }


        public void setName(final String name) {
            this.name = name;
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


        public String getFamilyCode() {
            return this.familyCode;
        }


        public void setFamilyCode(final String familyCode) {
            this.familyCode = familyCode;
        }


        public String getFamilyName() {
            return this.familyName;
        }


        public void setFamilyName(final String familyName) {
            this.familyName = familyName;
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


        public String getCompanyName() {
            return this.companyName;
        }


        public void setCompanyName(final String companyName) {
            this.companyName = companyName;
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


        public BigDecimal getPriceQuote() {
            return this.priceQuote;
        }


        public void setPriceQuote(final BigDecimal priceQuote) {
            this.priceQuote = priceQuote;
        }


        public String getPriceQuoteCurrency() {
            return this.priceQuoteCurrency;
        }


        public void setPriceQuoteCurrency(final String priceQuoteCurrency) {
            this.priceQuoteCurrency = priceQuoteCurrency;
        }


        public BigDecimal getChangeAmount() {
            return this.changeAmount;
        }


        public void setChangeAmount(final BigDecimal changeAmount) {
            this.changeAmount = changeAmount;
        }


        public BigDecimal getChangePercent() {
            return this.changePercent;
        }


        public void setChangePercent(final BigDecimal changePercent) {
            this.changePercent = changePercent;
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


        public String getRiskLvlCde() {
            return this.riskLvlCde;
        }


        public void setRiskLvlCde(final String riskLvlCde) {
            this.riskLvlCde = riskLvlCde;
        }


        public String getInceptionDate() {
            return this.inceptionDate;
        }


        public void setInceptionDate(final String inceptionDate) {
            this.inceptionDate = inceptionDate;
        }


        public String getProductCurrency() {
            return this.productCurrency;
        }


        public void setProductCurrency(final String productCurrency) {
            this.productCurrency = productCurrency;
        }


        public BigDecimal getTurnoverRatio() {
            return this.turnoverRatio;
        }


        public void setTurnoverRatio(final BigDecimal turnoverRatio) {
            this.turnoverRatio = turnoverRatio;
        }


        public BigDecimal getStdDev3Yr() {
            return this.stdDev3Yr;
        }


        public void setStdDev3Yr(final BigDecimal stdDev3Yr) {
            this.stdDev3Yr = stdDev3Yr;
        }


        public Integer getEquityStylebox() {
            return this.equityStylebox;
        }


        public void setEquityStylebox(final Integer equityStylebox) {
            this.equityStylebox = equityStylebox;
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


        public BigDecimal getDistributionYield() {
            return this.distributionYield;
        }


        public void setDistributionYield(final BigDecimal distributionYield) {
            this.distributionYield = distributionYield;
        }


        public String getDistributionFrequency() {
            return this.distributionFrequency;
        }


        public void setDistributionFrequency(final String distributionFrequency) {
            this.distributionFrequency = distributionFrequency;
        }


        public BigDecimal getDividendYield() {
            return this.dividendYield;
        }


        public void setDividendYield(final BigDecimal dividendYield) {
            this.dividendYield = dividendYield;
        }


        public BigDecimal getProdTopSellRankNum() {
            return this.prodTopSellRankNum;
        }


        public void setProdTopSellRankNum(final BigDecimal prodTopSellRankNum) {
            this.prodTopSellRankNum = prodTopSellRankNum;
        }


        public String getTopSellProdIndex() {
            return this.topSellProdIndex;
        }


        public void setTopSellProdIndex(final String topSellProdIndex) {
            this.topSellProdIndex = topSellProdIndex;
        }


        public String getTopPerformersIndicator() {
            return this.topPerformersIndicator;
        }


        public void setTopPerformersIndicator(final String topPerformersIndicator) {
            this.topPerformersIndicator = topPerformersIndicator;
        }


        public String getProdLaunchDate() {
            return this.prodLaunchDate;
        }


        public void setProdLaunchDate(final String prodLaunchDate) {
            this.prodLaunchDate = prodLaunchDate;
        }


        public String getInvestmentObjectiveAndStrategy() {
            return this.investmentObjectiveAndStrategy;
        }


        public void setInvestmentObjectiveAndStrategy(final String investmentObjectiveAndStrategy) {
            this.investmentObjectiveAndStrategy = investmentObjectiveAndStrategy;
        }


        public String getExchangeUpdatedTime() {
            return this.exchangeUpdatedTime;
        }


        public void setExchangeUpdatedTime(final String exchangeUpdatedTime) {
            this.exchangeUpdatedTime = exchangeUpdatedTime;
        }


        public String getEndDateYearRisk() {
            return this.endDateYearRisk;
        }


        public void setEndDateYearRisk(final String endDateYearRisk) {
            this.endDateYearRisk = endDateYearRisk;
        }


        public String getEndDateRiskLvlCde() {
            return this.endDateRiskLvlCde;
        }


        public void setEndDateRiskLvlCde(final String endDateRiskLvlCde) {
            this.endDateRiskLvlCde = endDateRiskLvlCde;
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


        public String getSurveyedFundNetAssetsDate() {
            return this.surveyedFundNetAssetsDate;
        }


        public void setSurveyedFundNetAssetsDate(final String surveyedFundNetAssetsDate) {
            this.surveyedFundNetAssetsDate = surveyedFundNetAssetsDate;
        }

    }



    public class Rating {


        private String morningstarRating;


        private Integer taxAdjustedRating;

        private String averageCreditQualityName;


        private Integer averageCreditQuality;

        private String averageCreditQualityDate;


        private BigDecimal rank1Yr;


        private BigDecimal rank3Yr;


        private BigDecimal rank5Yr;


        private BigDecimal rank10Yr;

        private String ratingDate;


        public String getMorningstarRating() {
            return this.morningstarRating;
        }


        public void setMorningstarRating(final String morningstarRating) {
            this.morningstarRating = morningstarRating;
        }


        public Integer getTaxAdjustedRating() {
            return this.taxAdjustedRating;
        }


        public void setTaxAdjustedRating(final Integer taxAdjustedRating) {
            this.taxAdjustedRating = taxAdjustedRating;
        }


        public String getAverageCreditQualityName() {
            return this.averageCreditQualityName;
        }


        public void setAverageCreditQualityName(final String averageCreditQualityName) {
            this.averageCreditQualityName = averageCreditQualityName;
        }


        public Integer getAverageCreditQuality() {
            return this.averageCreditQuality;
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



    public class Performance {


        private CalendarYearTotalReturns calendarYearTotalReturns;


        private CumulativeTotalReturns cumulativeTotalReturns;


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

        public class CalendarYearTotalReturns {
            List<FundCalendarYearReturn> items;
            private String mptpiIndexId;
            private String mptpiIndexName;
            private String lastUpdatedDate;

            public List<FundCalendarYearReturn> getItems() {
                return this.items;
            }

            public void setItems(final List<FundCalendarYearReturn> items) {
                this.items = items;
            }


            public String getMptpiIndexId() {
                return this.mptpiIndexId;
            }


            public void setMptpiIndexId(final String mptpiIndexId) {
                this.mptpiIndexId = mptpiIndexId;
            }


            public String getMptpiIndexName() {
                return this.mptpiIndexName;
            }


            public void setMptpiIndexName(final String mptpiIndexName) {
                this.mptpiIndexName = mptpiIndexName;
            }


            public String getLastUpdatedDate() {
                return this.lastUpdatedDate;
            }


            public void setLastUpdatedDate(final String lastUpdatedDate) {
                this.lastUpdatedDate = lastUpdatedDate;
            }
        }

        public class CumulativeTotalReturns {
            List<FundCumulativeReturn> items;
            private String mptpiIndexId;
            private String mptpiIndexName;
            private String lastUpdatedDate;
            private String dailyLastUpdatedDate;

            public List<FundCumulativeReturn> getItems() {
                return this.items;
            }

            public void setItems(final List<FundCumulativeReturn> items) {
                this.items = items;
            }


            public String getMptpiIndexId() {
                return this.mptpiIndexId;
            }


            public void setMptpiIndexId(final String mptpiIndexId) {
                this.mptpiIndexId = mptpiIndexId;
            }


            public String getMptpiIndexName() {
                return this.mptpiIndexName;
            }


            public void setMptpiIndexName(final String mptpiIndexName) {
                this.mptpiIndexName = mptpiIndexName;
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
    }

    public class InvestmentStrategy {
        private String investmentStyle;
        private String interestRateSensitivity;
        private BigDecimal assetAllocBondNet;
        private BigDecimal assetAllocEquityNet;
        private BigDecimal preferredStockNet;
        private BigDecimal assetAllocCashNet;
        private BigDecimal otherNet;


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


        public BigDecimal getAssetAllocEquityNet() {
            return this.assetAllocEquityNet;
        }


        public void setAssetAllocEquityNet(final BigDecimal assetAllocEquityNet) {
            this.assetAllocEquityNet = assetAllocEquityNet;
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

    public class YieldAndCredit {
        private BigDecimal averageCurrentYield;
        private BigDecimal averageYieldToMaturity;
        private BigDecimal averageDuration;
        private String averageCreditQualityName;
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


        public String getAverageCreditQualityName() {
            return this.averageCreditQualityName;
        }


        public void setAverageCreditQualityName(final String averageCreditQualityName) {
            this.averageCreditQualityName = averageCreditQualityName;
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

    public class PurchaseInfo {

        private Boolean rrsp;
        private String loadType;
        private BigDecimal expenseRatio;
        private BigDecimal initialCharge;// Initial Charge Upon Subscription
        private BigDecimal annualManagementFee;// Annual Management Fee
        private String annualReportDate;

        private BigDecimal minimumInitial;
        private String minimumInitialCurrencyCode;
        private BigDecimal minimumSubsequent;
        private String minimumSubsequentCurrencyCode;
        private BigDecimal minimumIRA;
        private String minimumIRACurrencyCode;


        private BigDecimal minInitInvst;
        private String minInitInvstCurrencyCode;
        private BigDecimal minSubqInvst;
        private String minSubqInvstCurrencyCode;

        private BigDecimal hhhhMinInitInvst;
        private String hhhhMinInitInvstCurrencyCode;
        private BigDecimal hhhhMinSubqInvst;
        private String hhhhMinSubqInvstCurrencyCode;


        public Boolean getRrsp() {
            return this.rrsp;
        }


        public void setRrsp(final Boolean rrsp) {
            this.rrsp = rrsp;
        }


        public String getLoadType() {
            return this.loadType;
        }


        public void setLoadType(final String loadType) {
            this.loadType = loadType;
        }


        public BigDecimal getExpenseRatio() {
            return this.expenseRatio;
        }


        public void setExpenseRatio(final BigDecimal expenseRatio) {
            this.expenseRatio = expenseRatio;
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


        public String getAnnualReportDate() {
            return this.annualReportDate;
        }


        public void setAnnualReportDate(final String annualReportDate) {
            this.annualReportDate = annualReportDate;
        }


        public BigDecimal getMinimumInitial() {
            return this.minimumInitial;
        }


        public void setMinimumInitial(final BigDecimal minimumInitial) {
            this.minimumInitial = minimumInitial;
        }


        public String getMinimumInitialCurrencyCode() {
            return this.minimumInitialCurrencyCode;
        }


        public void setMinimumInitialCurrencyCode(final String minimumInitialCurrencyCode) {
            this.minimumInitialCurrencyCode = minimumInitialCurrencyCode;
        }


        public BigDecimal getMinimumSubsequent() {
            return this.minimumSubsequent;
        }


        public void setMinimumSubsequent(final BigDecimal minimumSubsequent) {
            this.minimumSubsequent = minimumSubsequent;
        }


        public String getMinimumSubsequentCurrencyCode() {
            return this.minimumSubsequentCurrencyCode;
        }


        public void setMinimumSubsequentCurrencyCode(final String minimumSubsequentCurrencyCode) {
            this.minimumSubsequentCurrencyCode = minimumSubsequentCurrencyCode;
        }


        public BigDecimal getMinimumIRA() {
            return this.minimumIRA;
        }


        public void setMinimumIRA(final BigDecimal minimumIRA) {
            this.minimumIRA = minimumIRA;
        }


        public String getMinimumIRACurrencyCode() {
            return this.minimumIRACurrencyCode;
        }


        public void setMinimumIRACurrencyCode(final String minimumIRACurrencyCode) {
            this.minimumIRACurrencyCode = minimumIRACurrencyCode;
        }


        public BigDecimal getMinInitInvst() {
            return this.minInitInvst;
        }


        public void setMinInitInvst(final BigDecimal minInitInvst) {
            this.minInitInvst = minInitInvst;
        }


        public String getMinInitInvstCurrencyCode() {
            return this.minInitInvstCurrencyCode;
        }


        public void setMinInitInvstCurrencyCode(final String minInitInvstCurrencyCode) {
            this.minInitInvstCurrencyCode = minInitInvstCurrencyCode;
        }


        public BigDecimal getMinSubqInvst() {
            return this.minSubqInvst;
        }


        public void setMinSubqInvst(final BigDecimal minSubqInvst) {
            this.minSubqInvst = minSubqInvst;
        }


        public String getMinSubqInvstCurrencyCode() {
            return this.minSubqInvstCurrencyCode;
        }


        public void setMinSubqInvstCurrencyCode(final String minSubqInvstCurrencyCode) {
            this.minSubqInvstCurrencyCode = minSubqInvstCurrencyCode;
        }


        public BigDecimal gethhhhMinInitInvst() {
            return this.hhhhMinInitInvst;
        }


        public void sethhhhMinInitInvst(final BigDecimal hhhhMinInitInvst) {
            this.hhhhMinInitInvst = hhhhMinInitInvst;
        }


        public String gethhhhMinInitInvstCurrencyCode() {
            return this.hhhhMinInitInvstCurrencyCode;
        }


        public void sethhhhMinInitInvstCurrencyCode(final String hhhhMinInitInvstCurrencyCode) {
            this.hhhhMinInitInvstCurrencyCode = hhhhMinInitInvstCurrencyCode;
        }


        public BigDecimal gethhhhMinSubqInvst() {
            return this.hhhhMinSubqInvst;
        }


        public void sethhhhMinSubqInvst(final BigDecimal hhhhMinSubqInvst) {
            this.hhhhMinSubqInvst = hhhhMinSubqInvst;
        }


        public String gethhhhMinSubqInvstCurrencyCode() {
            return this.hhhhMinSubqInvstCurrencyCode;
        }


        public void sethhhhMinSubqInvstCurrencyCode(final String hhhhMinSubqInvstCurrencyCode) {
            this.hhhhMinSubqInvstCurrencyCode = hhhhMinSubqInvstCurrencyCode;
        }

    }

    public class TopTenHoldings {


        List<TopHoldingsSearch> items;


        private String lastUpdatedDate;


        public List<TopHoldingsSearch> getItems() {
            return this.items;
        }


        public void setItems(final List<TopHoldingsSearch> items) {
            this.items = items;
        }


        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }


        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }
    }

    public class AssetAllocations {


        private List<HoldingAllocation> assetAllocations;
        private String portfolioDate;


        public List<HoldingAllocation> getAssetAllocations() {
            return this.assetAllocations;
        }


        public void setAssetAllocations(final List<HoldingAllocation> assetAllocations) {
            this.assetAllocations = assetAllocations;
        }


        public String getPortfolioDate() {
            return this.portfolioDate;
        }


        public void setPortfolioDate(final String portfolioDate) {
            this.portfolioDate = portfolioDate;
        }

    }

    public class GlobalStockSectors {


        private List<HoldingAllocation> globalStockSectors;
        private String portfolioDate;


        public List<HoldingAllocation> getGlobalStockSectors() {
            return this.globalStockSectors;
        }


        public void setGlobalStockSectors(final List<HoldingAllocation> globalStockSectors) {
            this.globalStockSectors = globalStockSectors;
        }


        public String getPortfolioDate() {
            return this.portfolioDate;
        }


        public void setPortfolioDate(final String portfolioDate) {
            this.portfolioDate = portfolioDate;
        }

    }

    public class RegionalExposures {


        private List<HoldingAllocation> regionalExposures;
        private String portfolioDate;


        public List<HoldingAllocation> getRegionalExposures() {
            return this.regionalExposures;
        }


        public void setRegionalExposures(final List<HoldingAllocation> regionalExposures) {
            this.regionalExposures = regionalExposures;
        }


        public String getPortfolioDate() {
            return this.portfolioDate;
        }


        public void setPortfolioDate(final String portfolioDate) {
            this.portfolioDate = portfolioDate;
        }

    }

    public class GlobalBondSectors {


        private List<HoldingAllocation> globalBondSectors;
        private String portfolioDate;


        public List<HoldingAllocation> getGlobalBondSectors() {
            return this.globalBondSectors;
        }


        public void setGlobalBondSectors(final List<HoldingAllocation> globalBondSectors) {
            this.globalBondSectors = globalBondSectors;
        }


        public String getPortfolioDate() {
            return this.portfolioDate;
        }


        public void setPortfolioDate(final String portfolioDate) {
            this.portfolioDate = portfolioDate;
        }


    }

    public class BondRegionalExposures {


        private List<HoldingAllocation> bondRegionalExposures;
        private String portfolioDate;


        public List<HoldingAllocation> getBondRegionalExposures() {
            return this.bondRegionalExposures;
        }


        public void setBondRegionalExposures(final List<HoldingAllocation> bondRegionalExposures) {
            this.bondRegionalExposures = bondRegionalExposures;
        }


        public String getPortfolioDate() {
            return this.portfolioDate;
        }


        public void setPortfolioDate(final String portfolioDate) {
            this.portfolioDate = portfolioDate;
        }


    }

}