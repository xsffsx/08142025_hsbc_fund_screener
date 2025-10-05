
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult;

import java.math.BigDecimal;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;




public class FundSearchResultProduct {

    
    private Header header;

    
    private Summary summary;

    
    private Profile profile;

    
    private Rating rating;

    
    private Performance performance;

    
    private List<FundSearchRisk> risk;

    
    private Holdings holdings;

    
    private Purchase purchase;

    
    private String[] swithableGroup;

    
    public Header getHeader() {
        return this.header;
    }

    
    public String[] getSwithableGroup() {
        return this.swithableGroup;
    }

    
    public void setSwithableGroup(final String[] swithableGroup) {
        this.swithableGroup = swithableGroup;
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

    
    public Rating getRating() {
        return this.rating;
    }

    
    public void setRating(final Rating rating) {
        this.rating = rating;
    }

    
    public Performance getPerformance() {
        return this.performance;
    }

    
    public void setPerformance(final Performance performance) {
        this.performance = performance;
    }

    
    public List<FundSearchRisk> getRisk() {
        return this.risk;
    }

    
    public void setRisk(final List<FundSearchRisk> risk) {
        this.risk = risk;
    }

    
    public Holdings getHoldings() {
        return this.holdings;
    }

    
    public void setHoldings(final Holdings holdings) {
        this.holdings = holdings;
    }

    
    public Purchase getPurchase() {
        return this.purchase;
    }

    
    public void setPurchase(final Purchase purchase) {
        this.purchase = purchase;
    }

    

    public class Header {

        
        private String name;

        
        private String market;

        
        private String productType;

        
        private String currency;

        
        private String categoryCode;

        
        private String categoryName;

        private String familyCode;

        private String familyName;

        
        private String categoryLevel0Code;

        
        private String categoryLevel0Name;

        
        private String categoryLevel1Code;

        
        private String categoryLevel1Name;

        private String investmentRegionCode;

        private String investmentRegionName;

        private String siFundCatCde;

        
        private List<ProdAltNumSeg> prodAltNumSeg;

        public List<ProdAltNumSeg> getProdAltNumSeg() {
            return this.prodAltNumSeg;
        }

        public void setProdAltNumSeg(final List<ProdAltNumSeg> prodAltNumSeg) {
            this.prodAltNumSeg = prodAltNumSeg;
        }

        
        public String getName() {
            return this.name;
        }

        
        public void setName(final String name) {
            this.name = name;
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

        
        public String getCategoryLevel0Code() {
            return this.categoryLevel0Code;
        }

        
        public void setCategoryLevel0Code(final String categoryLevel0Code) {
            this.categoryLevel0Code = categoryLevel0Code;
        }

        
        public String getCategoryLevel1Code() {
            return this.categoryLevel1Code;
        }

        
        public void setCategoryLevel1Code(final String categoryLevel1Code) {
            this.categoryLevel1Code = categoryLevel1Code;
        }

        
        public String getCategoryLevel0Name() {
            return this.categoryLevel0Name;
        }

        
        public void setCategoryLevel0Name(final String categoryLevel0Name) {
            this.categoryLevel0Name = categoryLevel0Name;
        }

        
        public String getCategoryLevel1Name() {
            return this.categoryLevel1Name;
        }

        
        public void setCategoryLevel1Name(final String categoryLevel1Name) {
            this.categoryLevel1Name = categoryLevel1Name;
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

        
        public String getSiFundCatCde() {
            return siFundCatCde;
        }

        
        public void setSiFundCatCde(String siFundCatCde) {
            this.siFundCatCde = siFundCatCde;
        }

    }

    
    public class Pagination {

        
        private String totalNumberOfRecords;

        
        private String numberOfRecords;

        
        private String startDetail;

        
        private String endDetail;

        
        public String getTotalNumberOfRecords() {
            return this.totalNumberOfRecords;
        }

        
        public void setTotalNumberOfRecords(final String totalNumberOfRecords) {
            this.totalNumberOfRecords = totalNumberOfRecords;
        }

        
        public String getNumberOfRecords() {
            return this.numberOfRecords;
        }

        
        public void setNumberOfRecords(final String numberOfRecords) {
            this.numberOfRecords = numberOfRecords;
        }

        
        public String getStartDetail() {
            return this.startDetail;
        }

        
        public void setStartDetail(final String startDetail) {
            this.startDetail = startDetail;
        }

        
        public String getEndDetail() {
            return this.endDetail;
        }

        
        public void setEndDetail(final String endDetail) {
            this.endDetail = endDetail;
        }
    }

    

    public class Summary {

        
        private String riskLvlCde;

        
        private BigDecimal lastPrice;

        
        private BigDecimal dayEndNAV;

        
        private String dayEndNAVCurrencyCode;

        
        private BigDecimal changeAmountNAV;

        
        private BigDecimal changePercentageNAV;

        
        private String asOfDate;

        
        private BigDecimal averageDailyVolume;

        
        private BigDecimal assetsUnderManagement;

        
        private String assetsUnderManagementCurrencyCode;

        
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

        public String getAsOfDate() {
            return this.asOfDate;
        }

        public void setAsOfDate(final String asOfDate) {
            this.asOfDate = asOfDate;
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

        
        public String getAssetsUnderManagementCurrencyCode() {
            return this.assetsUnderManagementCurrencyCode;
        }

        
        public void setAssetsUnderManagementCurrencyCode(final String assetsUnderManagementCurrencyCode) {
            this.assetsUnderManagementCurrencyCode = assetsUnderManagementCurrencyCode;
        }

        
        public String getTotalNetAssetCurrencyCode() {
            return this.totalNetAssetCurrencyCode;
        }

        
        public void setTotalNetAssetCurrencyCode(final String totalNetAssetCurrencyCode) {
            this.totalNetAssetCurrencyCode = totalNetAssetCurrencyCode;
        }

        
        public String getRiskLvlCde() {
            return this.riskLvlCde;
        }

        
        public void setRiskLvlCde(final String riskLvlCde) {
            this.riskLvlCde = riskLvlCde;
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

        
        public BigDecimal getAverageDailyVolume() {
            return this.averageDailyVolume;
        }

        
        public void setAverageDailyVolume(final BigDecimal averageDailyVolume) {
            this.averageDailyVolume = averageDailyVolume;
        }

        
        public BigDecimal getAssetsUnderManagement() {
            return this.assetsUnderManagement;
        }

        
        public void setAssetsUnderManagement(final BigDecimal assetsUnderManagement) {
            this.assetsUnderManagement = assetsUnderManagement;
        }

        
        public Long getTotalNetAsset() {
            return this.totalNetAsset;
        }

        
        public void setTotalNetAsset(final Long totalNetAsset) {
            this.totalNetAsset = totalNetAsset;
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

        
        public String getEndDate() {
            return this.endDate;
        }

        
        public void setEndDate(final String endDate) {
            this.endDate = endDate;
        }
    }

    

    public class Profile {

        
        private String inceptionDate;

        
        private BigDecimal turnoverRatio;

        
        private BigDecimal stdDev3Yr;

        
        private Integer equityStylebox;

        
        private BigDecimal expenseRatio;

        
        private BigDecimal marginRatio;

        
        private BigDecimal initialCharge;

        
        private BigDecimal annualManagementFee;

        
        private BigDecimal distributionYield;

        
        private String distributionFrequency;

        
        private String topPerformersIndicator;

        
        private BigDecimal prodTopSellRankNum;

        
        private String topSellProdIndex;

        
        private String prodLaunchDate;

        private String fundClassCde;
        private String amcmIndicator;
        private String nextDealDate;
        private String prodStatCde;
        private String allowBuy;
        private String allowSell;
        private String allowSwInProdInd;
        private String allowSwOutProdInd;
        private String allowSellMipProdInd;
        private String annualReportDate;
        private String piFundInd;
        private String deAuthFundInd;
        private String payCashDivInd;
        private String undlIndexCde;
        private Integer popularRankNum;
        private String esgInd;

        private String inDealDate;
        private String inScribStlDate;
        private String inRedempStlDate;
        
        private String gbaAcctTrdb;
        private String gnrAcctTrdb;
        
         private Integer shortListRPQLvlNum;

        
        public String getUndlIndexCde() {
            return undlIndexCde;
        }

        
        public void setUndlIndexCde(String undlIndexCde) {
            this.undlIndexCde = undlIndexCde;
        }

        
        public Integer getPopularRankNum() {
            return popularRankNum;
        }

        
        public void setPopularRankNum(Integer popularRankNum) {
            this.popularRankNum = popularRankNum;
        }

         
         public String getEsgInd() {
         return esgInd;
         }

         
         public void setEsgInd(String esgInd) {
         this.esgInd = esgInd;
         }

        
        public String getInDealDate() {
            return inDealDate;
        }

        
        public void setInDealDate(String inDealDate) {
            this.inDealDate = inDealDate;
        }

        
        public String getInScribStlDate() {
            return inScribStlDate;
        }

        
        public void setInScribStlDate(String InScribStlDate) {
            this.inScribStlDate = InScribStlDate;
        }

        
        public String getInRedempStlDate() {
            return inRedempStlDate;
        }

        
        public void setInRedempStlDate(String inRedempStlDate) {
            this.inRedempStlDate = inRedempStlDate;
        }

        
        public String getPayCashDivInd() {
            return payCashDivInd;
        }


        
        public void setPayCashDivInd(String payCashDivInd) {
            this.payCashDivInd = payCashDivInd;
        }

        
        public String getInceptionDate() {
            return this.inceptionDate;
        }

        
        public void setInceptionDate(final String inceptionDate) {
            this.inceptionDate = inceptionDate;
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

        
        public String getTopPerformersIndicator() {
            return this.topPerformersIndicator;
        }

        
        public void setTopPerformersIndicator(final String topPerformersIndicator) {
            this.topPerformersIndicator = topPerformersIndicator;
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

        
        public String getProdLaunchDate() {
            return this.prodLaunchDate;
        }

        
        public void setProdLaunchDate(final String prodLaunchDate) {
            this.prodLaunchDate = prodLaunchDate;
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

        
        public String getGbaAcctTrdb() {
            return gbaAcctTrdb;
        }

        
        public void setGbaAcctTrdb(String gbaAcctTrdb) {
            this.gbaAcctTrdb = gbaAcctTrdb;
        }

        
        public String getGnrAcctTrdb() {
            return gnrAcctTrdb;
        }

        
        public void setGnrAcctTrdb(String gnrAcctTrdb) {
            this.gnrAcctTrdb = gnrAcctTrdb;
        }
        
        
        public Integer getShortListRPQLvlNum() {
            return shortListRPQLvlNum;
        }

        
        public void setShortListRPQLvlNum(Integer shortListRPQLvlNum) {
            this.shortListRPQLvlNum = shortListRPQLvlNum;
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

        
        private AnnualizedReturns annualizedReturns;

        
        private CalendarReturns calendarReturns;

        
        public AnnualizedReturns getAnnualizedReturns() {
            return this.annualizedReturns;
        }

        
        public void setAnnualizedReturns(final AnnualizedReturns annualizedReturns) {
            this.annualizedReturns = annualizedReturns;
        }

        
        public CalendarReturns getCalendarReturns() {
            return this.calendarReturns;
        }

        
        public void setCalendarReturns(final CalendarReturns calendarReturns) {
            this.calendarReturns = calendarReturns;
        }

        

        public class AnnualizedReturns {

            private BigDecimal return1day;

            
            private BigDecimal return1Mth;

            
            private BigDecimal return3Mth;

            
            private BigDecimal return6Mth;

            
            private BigDecimal return1Yr;

            
            private BigDecimal return3Yr;

            
            private BigDecimal return5Yr;

            
            private BigDecimal return10Yr;

            
            private BigDecimal returnSinceInception;

            
            private String inceptionDate;

            private String asOfDate;

            
            public BigDecimal getReturn1day() {
                return this.return1day;
            }

            
            public void setReturn1day(final BigDecimal return1day) {
                this.return1day = return1day;
            }

            
            public BigDecimal getReturn1Mth() {
                return this.return1Mth;
            }

            
            public void setReturn1Mth(final BigDecimal return1Mth) {
                this.return1Mth = return1Mth;
            }

            
            public BigDecimal getReturn3Mth() {
                return this.return3Mth;
            }

            
            public void setReturn3Mth(final BigDecimal return3Mth) {
                this.return3Mth = return3Mth;
            }

            
            public BigDecimal getReturn6Mth() {
                return this.return6Mth;
            }

            
            public void setReturn6Mth(final BigDecimal return6Mth) {
                this.return6Mth = return6Mth;
            }

            
            public BigDecimal getReturn1Yr() {
                return this.return1Yr;
            }

            
            public void setReturn1Yr(final BigDecimal return1Yr) {
                this.return1Yr = return1Yr;
            }

            
            public BigDecimal getReturn3Yr() {
                return this.return3Yr;
            }

            
            public void setReturn3Yr(final BigDecimal return3Yr) {
                this.return3Yr = return3Yr;
            }

            
            public BigDecimal getReturn5Yr() {
                return this.return5Yr;
            }

            
            public void setReturn5Yr(final BigDecimal return5Yr) {
                this.return5Yr = return5Yr;
            }

            
            public BigDecimal getReturn10Yr() {
                return this.return10Yr;
            }

            
            public void setReturn10Yr(final BigDecimal return10Yr) {
                this.return10Yr = return10Yr;
            }

            
            public BigDecimal getReturnSinceInception() {
                return this.returnSinceInception;
            }

            
            public void setReturnSinceInception(final BigDecimal returnSinceInception) {
                this.returnSinceInception = returnSinceInception;
            }

            
            public String getInceptionDate() {
                return this.inceptionDate;
            }

            
            public void setInceptionDate(final String inceptionDate) {
                this.inceptionDate = inceptionDate;
            }

            
            public String getAsOfDate() {
                return this.asOfDate;
            }

            
            public void setAsOfDate(final String asOfDate) {
                this.asOfDate = asOfDate;
            }

        }

        

        public class CalendarReturns {

            
            private BigDecimal returnYTD;

            
            private List<FundSearchResultYear> year;

            
            public BigDecimal getReturnYTD() {
                return this.returnYTD;
            }

            
            public void setReturnYTD(final BigDecimal returnYTD) {
                this.returnYTD = returnYTD;
            }

            
            public List<FundSearchResultYear> getYear() {
                return this.year;
            }

            
            public void setYear(final List<FundSearchResultYear> year) {
                this.year = year;
            }

        }
    }

    

    public class Holdings {

        
        private FundSearchSector sector;

        
        private FundSearchGeographicRegion geographicRegion;

        
        private List<TopHoldingsSearch> topHoldingsList;

        
        private AssetAllocations assetAlloc;

        
        private GlobalStockSectors stockSectors;

        
        private RegionalExposures equityRegional;

        
        private GlobalBondSectors bondSectors;

        
        private BondRegionalExposures bondRegional;

        
        public FundSearchSector getSector() {
            return this.sector;
        }

        
        public void setSector(final FundSearchSector sector) {
            this.sector = sector;
        }

        
        public FundSearchGeographicRegion getGeographicRegion() {
            return this.geographicRegion;
        }

        
        public void setGeographicRegion(final FundSearchGeographicRegion geographicRegion) {
            this.geographicRegion = geographicRegion;
        }

        
        public List<TopHoldingsSearch> getTopHoldingsList() {
            return this.topHoldingsList;
        }

        
        public void setTopHoldingsList(final List<TopHoldingsSearch> topHoldingsList) {
            this.topHoldingsList = topHoldingsList;
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


    
    public class Purchase {

        
        private BigDecimal minInitInvst;

        
        private String minInitInvstCurrencyCode;

        
        private BigDecimal minSubqInvst;

        
        private String minSubqInvstCurrencyCode;

        
        private Long minInitRRSPInvst;

        
        private String minInitRRSPInvstCurrencyCode;

        
        private BigDecimal hhhhMinInitInvst;

        
        private String hhhhMinInitInvstCurrencyCode;

        
        private BigDecimal hhhhMinSubqInvst;

        
        private String hhhhMinSubqInvstCurrencyCode;

        
        private String loadType;

        
        private String RRSPEligibility;

        
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

        
        public Long getMinInitRRSPInvst() {
            return this.minInitRRSPInvst;
        }

        
        public void setMinInitRRSPInvst(final Long minInitRRSPInvst) {
            this.minInitRRSPInvst = minInitRRSPInvst;
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


        
        public String getMinInitRRSPInvstCurrencyCode() {
            return this.minInitRRSPInvstCurrencyCode;
        }

        
        public void setMinInitRRSPInvstCurrencyCode(final String minInitRRSPInvstCurrencyCode) {
            this.minInitRRSPInvstCurrencyCode = minInitRRSPInvstCurrencyCode;
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

        
        public String getLoadType() {
            return this.loadType;
        }

        
        public void setLoadType(final String loadType) {
            this.loadType = loadType;
        }

        
        public String getRRSPEligibility() {
            return this.RRSPEligibility;
        }

        
        public void setRRSPEligibility(final String rRSPEligibility) {
            this.RRSPEligibility = rRSPEligibility;
        }



    }


}