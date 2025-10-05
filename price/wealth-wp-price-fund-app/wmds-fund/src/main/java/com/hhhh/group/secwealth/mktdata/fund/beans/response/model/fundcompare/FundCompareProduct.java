
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare;

import java.math.BigDecimal;
import java.util.List;

public class FundCompareProduct {

    private Header header;

    private Summary summary;

    private Profile profile;

    private Rating rating;

    private Performance performance;

    private List<FundCompareRisk> risk;

    private PurchaseInfo purchaseInfo;

    private String prodAltNumXCode;

    private String Symbol;

    public String getProdAltNumXCode() {
        return this.prodAltNumXCode;
    }

    public void setProdAltNumXCode(final String prodAltNumXCode) {
        this.prodAltNumXCode = prodAltNumXCode;
    }


    public String getSymbol() {
        return this.Symbol;
    }


    public void setSymbol(final String symbol) {
        this.Symbol = symbol;
    }

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

    public List<FundCompareRisk> getRisk() {
        return this.risk;
    }

    public void setRisk(final List<FundCompareRisk> risk) {
        this.risk = risk;
    }


    public PurchaseInfo getPurchaseInfo() {
        return this.purchaseInfo;
    }


    public void setPurchaseInfo(final PurchaseInfo purchaseInfo) {
        this.purchaseInfo = purchaseInfo;
    }

    public class Header {

        private String name;

        private String prodAltNum;

        private String currency;

        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getProdAltNum() {
            return this.prodAltNum;
        }

        public void setProdAltNum(final String prodAltNum) {
            this.prodAltNum = prodAltNum;
        }

        public String getCurrency() {
            return this.currency;
        }

        public void setCurrency(final String currency) {
            this.currency = currency;
        }

    }

    public class Summary {

        private String categoryName;

        private BigDecimal dayEndNAV;

        private String exchangeUpdatedTime;

        private String dayEndNAVCurrencyCode;

        private BigDecimal changeAmountNAV;

        private BigDecimal changePercentageNAV;

        private BigDecimal assetsUnderManagement;

        private String assetsUnderManagementCurrencyCode;

        private BigDecimal totalNetAsset;

        private String totalNetAssetCurrencyCode;

        private String ratingOverall;

        private BigDecimal mer;

        private BigDecimal yield1Yr;

        private String riskLvlCde;

        private BigDecimal annualReportOngoingCharge;

        private BigDecimal actualManagementFee;

        private String endDate;

        private String DayEndDate;

        public String getCategoryName() {
            return this.categoryName;
        }

        public void setCategoryName(final String categoryName) {
            this.categoryName = categoryName;
        }

        public String getDayEndNAVCurrencyCode() {
            return this.dayEndNAVCurrencyCode;
        }

        public void setDayEndNAVCurrencyCode(final String dayEndNAVCurrencyCode) {
            this.dayEndNAVCurrencyCode = dayEndNAVCurrencyCode;
        }


        public BigDecimal getDayEndNAV() {
            return this.dayEndNAV;
        }


        public void setDayEndNAV(final BigDecimal dayEndNAV) {
            this.dayEndNAV = dayEndNAV;
        }


        public String getExchangeUpdatedTime() {
            return this.exchangeUpdatedTime;
        }


        public void setExchangeUpdatedTime(final String exchangeUpdatedTime) {
            this.exchangeUpdatedTime = exchangeUpdatedTime;
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


        public BigDecimal getAssetsUnderManagement() {
            return this.assetsUnderManagement;
        }


        public void setAssetsUnderManagement(final BigDecimal assetsUnderManagement) {
            this.assetsUnderManagement = assetsUnderManagement;
        }


        public BigDecimal getTotalNetAsset() {
            return this.totalNetAsset;
        }


        public void setTotalNetAsset(final BigDecimal totalNetAsset) {
            this.totalNetAsset = totalNetAsset;
        }


        public String getRatingOverall() {
            return this.ratingOverall;
        }


        public void setRatingOverall(final String ratingOverall) {
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


        public String getDayEndDate() {
            return this.DayEndDate;
        }


        public void setDayEndDate(final String dayEndDate) {
            this.DayEndDate = dayEndDate;
        }


    }

    public class Profile {

        private String inceptionDate;

        private BigDecimal turnoverRatio;

        private BigDecimal stdDev3Yr;

        private String equityStylebox;

        private String hhhhCategoryName;

        private String hhhhCategoryCode;

        private BigDecimal distributionYield;

        private String distributionFrequency;

        private String piFundInd;

        private String deAuthFundInd;

        private String surveyedFundNetAssetsDate;

        private BigDecimal annualManagementFee;

        private BigDecimal initialCharge;

        private BigDecimal expenseRatio;

        private String esgInd;

        private String gbaAcctTrdb;

        public String getEsgInd() {
            return esgInd;
        }

        public void setEsgInd(String esgInd) {
            this.esgInd = esgInd;
        }

        public String getGbaAcctTrdb() {
            return gbaAcctTrdb;
        }

        public void setGbaAcctTrdb(String gbaAcctTrdb) {
            this.gbaAcctTrdb = gbaAcctTrdb;
        }

        public BigDecimal getAnnualManagementFee() {
            return annualManagementFee;
        }

        public void setAnnualManagementFee(BigDecimal annualManagementFee) {
            this.annualManagementFee = annualManagementFee;
        }

        public BigDecimal getInitialCharge() {
            return initialCharge;
        }

        public void setInitialCharge(BigDecimal initialCharge) {
            this.initialCharge = initialCharge;
        }

        public BigDecimal getExpenseRatio() {
            return expenseRatio;
        }

        public void setExpenseRatio(BigDecimal expenseRatio) {
            this.expenseRatio = expenseRatio;
        }

        public String gethhhhCategoryName() {
            return this.hhhhCategoryName;
        }

        public void sethhhhCategoryName(final String hhhhCategoryName) {
            this.hhhhCategoryName = hhhhCategoryName;
        }

        public String gethhhhCategoryCode() {
            return this.hhhhCategoryCode;
        }

        public void sethhhhCategoryCode(final String hhhhCategoryCode) {
            this.hhhhCategoryCode = hhhhCategoryCode;
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

        public String getEquityStylebox() {
            return this.equityStylebox;
        }

        public void setEquityStylebox(final String equityStylebox) {
            this.equityStylebox = equityStylebox;
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

        private String taxAdjustedRating;

        private String averageCreditQualityName;

        private String averageCreditQuality;

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


        public String getTaxAdjustedRating() {
            return this.taxAdjustedRating;
        }


        public void setTaxAdjustedRating(final String taxAdjustedRating) {
            this.taxAdjustedRating = taxAdjustedRating;
        }


        public String getAverageCreditQualityName() {
            return this.averageCreditQualityName;
        }


        public void setAverageCreditQualityName(final String averageCreditQualityName) {
            this.averageCreditQualityName = averageCreditQualityName;
        }


        public String getAverageCreditQuality() {
            return this.averageCreditQuality;
        }


        public void setAverageCreditQuality(final String averageCreditQuality) {
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

            private BigDecimal return1Mth;
            private BigDecimal return3Mth;
            private BigDecimal return6Mth;
            private BigDecimal return1Yr;
            private BigDecimal return3Yr;
            private BigDecimal return5Yr;
            private BigDecimal return10Yr;
            private BigDecimal returnSinceInception;
            private String monthEndDate;


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


            public String getMonthEndDate() {
                return this.monthEndDate;
            }


            public void setMonthEndDate(final String monthEndDate) {
                this.monthEndDate = monthEndDate;
            }

        }

        public class CalendarReturns {

            private BigDecimal returnYTD;
            private BigDecimal year1;
            private BigDecimal year2;
            private BigDecimal year3;
            private BigDecimal year4;
            private BigDecimal year5;


            public BigDecimal getReturnYTD() {
                return this.returnYTD;
            }


            public void setReturnYTD(final BigDecimal returnYTD) {
                this.returnYTD = returnYTD;
            }


            public BigDecimal getYear1() {
                return this.year1;
            }


            public void setYear1(final BigDecimal year1) {
                this.year1 = year1;
            }


            public BigDecimal getYear2() {
                return this.year2;
            }


            public void setYear2(final BigDecimal year2) {
                this.year2 = year2;
            }


            public BigDecimal getYear3() {
                return this.year3;
            }


            public void setYear3(final BigDecimal year3) {
                this.year3 = year3;
            }


            public BigDecimal getYear4() {
                return this.year4;
            }


            public void setYear4(final BigDecimal year4) {
                this.year4 = year4;
            }


            public BigDecimal getYear5() {
                return this.year5;
            }


            public void setYear5(final BigDecimal year5) {
                this.year5 = year5;
            }
        }

    }

    public class HoldingsAllocation {
        private String assetClass;
        private String assetWeight;
        private String sectorClass;
        private String sectorWeight;
        private String geographicRegion;
        private String geographicRegionWeight;


        public String getAssetClass() {
            return this.assetClass;
        }


        public void setAssetClass(final String assetClass) {
            this.assetClass = assetClass;
        }


        public String getAssetWeight() {
            return this.assetWeight;
        }


        public void setAssetWeight(final String assetWeight) {
            this.assetWeight = assetWeight;
        }


        public String getSectorClass() {
            return this.sectorClass;
        }


        public void setSectorClass(final String sectorClass) {
            this.sectorClass = sectorClass;
        }


        public String getSectorWeight() {
            return this.sectorWeight;
        }


        public void setSectorWeight(final String sectorWeight) {
            this.sectorWeight = sectorWeight;
        }


        public String getGeographicRegion() {
            return this.geographicRegion;
        }


        public void setGeographicRegion(final String geographicRegion) {
            this.geographicRegion = geographicRegion;
        }


        public String getGeographicRegionWeight() {
            return this.geographicRegionWeight;
        }


        public void setGeographicRegionWeight(final String geographicRegionWeight) {
            this.geographicRegionWeight = geographicRegionWeight;
        }

    }

    public class PurchaseInfo {
        private BigDecimal minimumInitial;
        private String minimumInitialCurrencyCode;
        private BigDecimal minimumSubsequent;
        private String minimumSubsequentCurrencyCode;
        private BigDecimal minimumIRA;
        private String minimumIRACurrencyCode;
        private Boolean rrsp;
        private String loadType;

        private BigDecimal minInitInvst;
        private String minInitInvstCurrencyCode;
        private BigDecimal minSubqInvst;
        private String minSubqInvstCurrencyCode;

        private BigDecimal hhhhMinInitInvst;
        private String hhhhMinInitInvstCurrencyCode;
        private BigDecimal hhhhMinSubqInvst;
        private String hhhhMinSubqInvstCurrencyCode;


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


        public String getMinimumIRACurrencyCode() {
            return this.minimumIRACurrencyCode;
        }


        public void setMinimumIRACurrencyCode(final String minimumIRACurrencyCode) {
            this.minimumIRACurrencyCode = minimumIRACurrencyCode;
        }

    }
}
