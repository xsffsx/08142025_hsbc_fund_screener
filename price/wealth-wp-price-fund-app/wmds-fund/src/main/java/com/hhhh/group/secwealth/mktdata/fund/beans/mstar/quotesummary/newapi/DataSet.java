

package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.newapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"quickTake"})
@XmlRootElement(name = "DataSet")
public class DataSet {
    @XmlElement(name = "QuickTake", required = true)
    protected DataSet.QuickTake quickTake;


    public DataSet.QuickTake getQuickTake() {
        return this.quickTake;
    }


    public void setQuickTake(final DataSet.QuickTake value) {
        this.quickTake = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"fundInfo", "fee", "frontFee", "redemptionFee", "calendarReturn", "monthlyReturn",
        "dailyReturn", "price", "priceInfo", "dividend", "yield", "risk", "rating", "fundManager", "portfolioSummary", "topHolding",
        "bondHolding", "sectorBreakdown", "pieChart", "rdbTable", "pdfrdbTable"})
    public static class QuickTake {

        @XmlElement(name = "FundInfo", required = true)
        protected DataSet.QuickTake.FundInfo fundInfo;
        @XmlElement(name = "Fee", required = true)
        protected DataSet.QuickTake.Fee fee;
        @XmlElement(name = "FrontFee")
        protected List<DataSet.QuickTake.FrontFee> frontFee;
        @XmlElement(name = "RedemptionFee", required = true)
        protected DataSet.QuickTake.RedemptionFee redemptionFee;
        @XmlElement(name = "CalendarReturn")
        protected List<DataSet.QuickTake.CalendarReturn> calendarReturn;
        @XmlElement(name = "MonthlyReturn", required = true)
        protected DataSet.QuickTake.MonthlyReturn monthlyReturn;
        @XmlElement(name = "DailyReturn", required = true)
        protected DataSet.QuickTake.DailyReturn dailyReturn;
        @XmlElement(name = "Price", required = true)
        protected DataSet.QuickTake.Price price;
        @XmlElement(name = "PriceInfo", required = true)
        protected DataSet.QuickTake.PriceInfo priceInfo;
        @XmlElement(name = "Dividend", required = true)
        protected DataSet.QuickTake.Dividend dividend;
        @XmlElement(name = "Yield", required = true)
        protected DataSet.QuickTake.Yield yield;
        @XmlElement(name = "Risk", required = true)
        protected DataSet.QuickTake.Risk risk;
        @XmlElement(name = "Rating", required = true)
        protected DataSet.QuickTake.Rating rating;
        @XmlElement(name = "FundManager", required = true)
        protected DataSet.QuickTake.FundManager fundManager;
        @XmlElement(name = "PortfolioSummary", required = true)
        protected DataSet.QuickTake.PortfolioSummary portfolioSummary;
        @XmlElement(name = "TopHolding")
        protected List<DataSet.QuickTake.TopHolding> topHolding;
        @XmlElement(name = "BondHolding", required = true)
        protected List<DataSet.QuickTake.BondHolding> bondHolding;
        @XmlElement(name = "SectorBreakdown")
        protected List<DataSet.QuickTake.SectorBreakdown> sectorBreakdown;
        @XmlElement(name = "PieChart")
        protected List<DataSet.QuickTake.PieChart> pieChart;
        @XmlElement(name = "RDBTable")
        protected List<DataSet.QuickTake.RDBTable> rdbTable;
        @XmlElement(name = "PDFRDBTable")
        protected List<DataSet.QuickTake.PDFRDBTable> pdfrdbTable;


        public DataSet.QuickTake.FundInfo getFundInfo() {
            return this.fundInfo;
        }


        public void setFundInfo(final DataSet.QuickTake.FundInfo value) {
            this.fundInfo = value;
        }


        public DataSet.QuickTake.Fee getFee() {
            return this.fee;
        }


        public void setFee(final DataSet.QuickTake.Fee value) {
            this.fee = value;
        }

        
        public List<DataSet.QuickTake.FrontFee> getFrontFee() {
            if (this.frontFee == null) {
                this.frontFee = new ArrayList<DataSet.QuickTake.FrontFee>();
            }
            return this.frontFee;
        }

        
        public DataSet.QuickTake.RedemptionFee getRedemptionFee() {
            return this.redemptionFee;
        }

        
        public void setRedemptionFee(final DataSet.QuickTake.RedemptionFee value) {
            this.redemptionFee = value;
        }

        
        public List<DataSet.QuickTake.CalendarReturn> getCalendarReturn() {
            if (this.calendarReturn == null) {
                this.calendarReturn = new ArrayList<DataSet.QuickTake.CalendarReturn>();
            }
            return this.calendarReturn;
        }

        
        public DataSet.QuickTake.MonthlyReturn getMonthlyReturn() {
            return this.monthlyReturn;
        }

        
        public void setMonthlyReturn(final DataSet.QuickTake.MonthlyReturn value) {
            this.monthlyReturn = value;
        }

        
        public DataSet.QuickTake.DailyReturn getDailyReturn() {
            return this.dailyReturn;
        }

        
        public void setDailyReturn(final DataSet.QuickTake.DailyReturn value) {
            this.dailyReturn = value;
        }

        
        public DataSet.QuickTake.Price getPrice() {
            return this.price;
        }

        
        public void setPrice(final DataSet.QuickTake.Price value) {
            this.price = value;
        }

        
        public DataSet.QuickTake.PriceInfo getPriceInfo() {
            return this.priceInfo;
        }

        
        public void setPriceInfo(final DataSet.QuickTake.PriceInfo value) {
            this.priceInfo = value;
        }

        
        public DataSet.QuickTake.Dividend getDividend() {
            return this.dividend;
        }

        
        public void setDividend(final DataSet.QuickTake.Dividend value) {
            this.dividend = value;
        }

        
        public DataSet.QuickTake.Yield getYield() {
            return this.yield;
        }

        
        public void setYield(final DataSet.QuickTake.Yield value) {
            this.yield = value;
        }

        
        public DataSet.QuickTake.Risk getRisk() {
            return this.risk;
        }

        
        public void setRisk(final DataSet.QuickTake.Risk value) {
            this.risk = value;
        }

        
        public DataSet.QuickTake.Rating getRating() {
            return this.rating;
        }

        
        public void setRating(final DataSet.QuickTake.Rating value) {
            this.rating = value;
        }

        
        public DataSet.QuickTake.FundManager getFundManager() {
            return this.fundManager;
        }

        
        public void setFundManager(final DataSet.QuickTake.FundManager value) {
            this.fundManager = value;
        }

        
        public DataSet.QuickTake.PortfolioSummary getPortfolioSummary() {
            return this.portfolioSummary;
        }

        
        public void setPortfolioSummary(final DataSet.QuickTake.PortfolioSummary value) {
            this.portfolioSummary = value;
        }

        
        public List<DataSet.QuickTake.TopHolding> getTopHolding() {
            if (this.topHolding == null) {
                this.topHolding = new ArrayList<DataSet.QuickTake.TopHolding>();
            }
            return this.topHolding;
        }

        
        public List<DataSet.QuickTake.BondHolding> getBondHolding() {
            if (this.bondHolding == null) {
                this.bondHolding = new ArrayList<DataSet.QuickTake.BondHolding>();
            }
            return this.bondHolding;
        }

        
        public void setBondHolding(final List<DataSet.QuickTake.BondHolding> bondHolding) {
            this.bondHolding = bondHolding;
        }


        
        public List<DataSet.QuickTake.SectorBreakdown> getSectorBreakdown() {
            if (this.sectorBreakdown == null) {
                this.sectorBreakdown = new ArrayList<DataSet.QuickTake.SectorBreakdown>();
            }
            return this.sectorBreakdown;
        }

        
        public List<DataSet.QuickTake.PieChart> getPieChart() {
            if (this.pieChart == null) {
                this.pieChart = new ArrayList<DataSet.QuickTake.PieChart>();
            }
            return this.pieChart;
        }

        
        public List<DataSet.QuickTake.RDBTable> getRDBTable() {
            if (this.rdbTable == null) {
                this.rdbTable = new ArrayList<DataSet.QuickTake.RDBTable>();
            }
            return this.rdbTable;
        }

        
        public List<DataSet.QuickTake.PDFRDBTable> getPDFRDBTable() {
            if (this.pdfrdbTable == null) {
                this.pdfrdbTable = new ArrayList<DataSet.QuickTake.PDFRDBTable>();
            }
            return this.pdfrdbTable;
        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "holdingId", "holdingName", "holdingNameEn", "marketValue",
            "netAssetPercentage"})
        public static class BondHolding {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "HoldingId")
            protected int holdingId;
            @XmlElement(name = "HoldingName", required = true)
            protected String holdingName;
            @XmlElement(name = "HoldingNameEn")
            protected String holdingNameEn;
            @XmlElement(name = "MarketValue")
            protected int marketValue;
            @XmlElement(name = "NetAssetPercentage")
            protected float netAssetPercentage;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public int getHoldingId() {
                return this.holdingId;
            }

            
            public void setHoldingId(final int value) {
                this.holdingId = value;
            }

            
            public String getHoldingName() {
                return this.holdingName;
            }

            
            public void setHoldingName(final String value) {
                this.holdingName = value;
            }

            
            public String getHoldingNameEn() {
                return this.holdingNameEn;
            }

            
            public void setHoldingNameEn(final String holdingNameEn) {
                this.holdingNameEn = holdingNameEn;
            }

            
            public int getMarketValue() {
                return this.marketValue;
            }

            
            public void setMarketValue(final int value) {
                this.marketValue = value;
            }

            
            public float getNetAssetPercentage() {
                return this.netAssetPercentage;
            }

            
            public void setNetAssetPercentage(final float value) {
                this.netAssetPercentage = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "year", "returnY"})
        public static class CalendarReturn {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "Year")
            protected short year;
            @XmlElement(name = "ReturnY")
            protected float returnY;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public short getYear() {
                return this.year;
            }

            
            public void setYear(final short value) {
                this.year = value;
            }

            
            public float getReturnY() {
                return this.returnY;
            }

            
            public void setReturnY(final float value) {
                this.returnY = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "effectiveDate", "return1Month", "return1MonthToCat", "return3Month",
            "return3MonthToCat", "return6Month", "return6MonthToCat", "returnYTD", "returnYTDToCat", "return1Year",
            "return1YearToCat", "return2Year", "return2YearToCat", "return3Year", "return3YearToCat", "return5Year",
            "return5YearToCat"})
        public static class DailyReturn {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "EffectiveDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar effectiveDate;
            @XmlElement(name = "Return1Month")
            protected float return1Month;
            @XmlElement(name = "Return1MonthToCat")
            protected float return1MonthToCat;
            @XmlElement(name = "Return3Month")
            protected float return3Month;
            @XmlElement(name = "Return3MonthToCat")
            protected float return3MonthToCat;
            @XmlElement(name = "Return6Month")
            protected float return6Month;
            @XmlElement(name = "Return6MonthToCat")
            protected float return6MonthToCat;
            @XmlElement(name = "ReturnYTD")
            protected float returnYTD;
            @XmlElement(name = "ReturnYTDToCat")
            protected float returnYTDToCat;
            @XmlElement(name = "Return1Year")
            protected float return1Year;
            @XmlElement(name = "Return1YearToCat")
            protected float return1YearToCat;
            @XmlElement(name = "Return2Year")
            protected float return2Year;
            @XmlElement(name = "Return2YearToCat")
            protected float return2YearToCat;
            @XmlElement(name = "Return3Year")
            protected float return3Year;
            @XmlElement(name = "Return3YearToCat")
            protected float return3YearToCat;
            @XmlElement(name = "Return5Year")
            protected float return5Year;
            @XmlElement(name = "Return5YearToCat")
            protected float return5YearToCat;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public XMLGregorianCalendar getEffectiveDate() {
                return this.effectiveDate;
            }

            
            public void setEffectiveDate(final XMLGregorianCalendar value) {
                this.effectiveDate = value;
            }

            
            public float getReturn1Month() {
                return this.return1Month;
            }

            
            public void setReturn1Month(final float value) {
                this.return1Month = value;
            }

            
            public float getReturn1MonthToCat() {
                return this.return1MonthToCat;
            }

            
            public void setReturn1MonthToCat(final float value) {
                this.return1MonthToCat = value;
            }

            
            public float getReturn3Month() {
                return this.return3Month;
            }

            
            public void setReturn3Month(final float value) {
                this.return3Month = value;
            }

            
            public float getReturn3MonthToCat() {
                return this.return3MonthToCat;
            }

            
            public void setReturn3MonthToCat(final float value) {
                this.return3MonthToCat = value;
            }

            
            public float getReturn6Month() {
                return this.return6Month;
            }

            
            public void setReturn6Month(final float value) {
                this.return6Month = value;
            }

            
            public float getReturn6MonthToCat() {
                return this.return6MonthToCat;
            }

            
            public void setReturn6MonthToCat(final float value) {
                this.return6MonthToCat = value;
            }

            
            public float getReturnYTD() {
                return this.returnYTD;
            }

            
            public void setReturnYTD(final float value) {
                this.returnYTD = value;
            }

            
            public float getReturnYTDToCat() {
                return this.returnYTDToCat;
            }

            
            public void setReturnYTDToCat(final float value) {
                this.returnYTDToCat = value;
            }

            
            public float getReturn1Year() {
                return this.return1Year;
            }

            
            public void setReturn1Year(final float value) {
                this.return1Year = value;
            }

            
            public float getReturn1YearToCat() {
                return this.return1YearToCat;
            }

            
            public void setReturn1YearToCat(final float value) {
                this.return1YearToCat = value;
            }

            
            public float getReturn2Year() {
                return this.return2Year;
            }

            
            public void setReturn2Year(final float value) {
                this.return2Year = value;
            }

            
            public float getReturn2YearToCat() {
                return this.return2YearToCat;
            }

            
            public void setReturn2YearToCat(final float value) {
                this.return2YearToCat = value;
            }

            
            public float getReturn3Year() {
                return this.return3Year;
            }

            
            public void setReturn3Year(final float value) {
                this.return3Year = value;
            }

            
            public float getReturn3YearToCat() {
                return this.return3YearToCat;
            }

            
            public void setReturn3YearToCat(final float value) {
                this.return3YearToCat = value;
            }

            
            public float getReturn5Year() {
                return this.return5Year;
            }

            
            public void setReturn5Year(final float value) {
                this.return5Year = value;
            }

            
            public float getReturn5YearToCat() {
                return this.return5YearToCat;
            }

            
            public void setReturn5YearToCat(final float value) {
                this.return5YearToCat = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "totalDistribution", "declaresDailyDividend", "excludingDate", "reinvestDate",
            "announceDate"})
        public static class Dividend {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "TotalDistribution")
            protected float totalDistribution;
            @XmlElement(name = "DeclaresDailyDividend", required = true)
            protected String declaresDailyDividend;
            @XmlElement(name = "ExcludingDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar excludingDate;
            @XmlElement(name = "ReinvestDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar reinvestDate;
            @XmlElement(name = "AnnounceDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar announceDate;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public float getTotalDistribution() {
                return this.totalDistribution;
            }

            
            public void setTotalDistribution(final float value) {
                this.totalDistribution = value;
            }

            
            public String getDeclaresDailyDividend() {
                return this.declaresDailyDividend;
            }

            
            public void setDeclaresDailyDividend(final String value) {
                this.declaresDailyDividend = value;
            }

            
            public XMLGregorianCalendar getExcludingDate() {
                return this.excludingDate;
            }

            
            public void setExcludingDate(final XMLGregorianCalendar value) {
                this.excludingDate = value;
            }

            
            public XMLGregorianCalendar getReinvestDate() {
                return this.reinvestDate;
            }

            
            public void setReinvestDate(final XMLGregorianCalendar value) {
                this.reinvestDate = value;
            }

            
            public XMLGregorianCalendar getAnnounceDate() {
                return this.announceDate;
            }

            
            public void setAnnounceDate(final XMLGregorianCalendar value) {
                this.announceDate = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "actualManagementFee", "managementFee", "custodialFee"})
        public static class Fee {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "ActualManagementFee")
            protected float actualManagementFee;
            @XmlElement(name = "ManagementFee")
            protected float managementFee;
            @XmlElement(name = "CustodialFee")
            protected float custodialFee;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public float getActualManagementFee() {
                return this.actualManagementFee;
            }

            
            public void setActualManagementFee(final float value) {
                this.actualManagementFee = value;
            }

            
            public float getManagementFee() {
                return this.managementFee;
            }

            
            public void setManagementFee(final float value) {
                this.managementFee = value;
            }

            
            public float getCustodialFee() {
                return this.custodialFee;
            }

            
            public void setCustodialFee(final float value) {
                this.custodialFee = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "key", "value"})
        public static class FrontFee {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "Key", required = true)
            protected String key;
            @XmlElement(name = "Value", required = true)
            protected String value;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public String getKey() {
                return this.key;
            }

            
            public void setKey(final String value) {
                this.key = value;
            }

            
            public String getValue() {
                return this.value;
            }

            
            public void setValue(final String value) {
                this.value = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"hhhhFundCode", "symbol", "secId", "performanceId", "displayNameLocal", "hhhhRiskLevel",
            "fundGroup", "mrfNorth", "hhhhTopPerformers", "hhhhBestsellers", "hhhhFocusFund", "hhhhNewFund", "categoryId",
            "category", "inceptionDate", "fundProfileLocal", "baseCurrencyLocal", "currencyId", "fundCompanyNameLocal",
            "custodianNameLocal", "fundCompanyTel", "fundCompanyFax", "fundCompanyHomePage", "fundCompanyAddressLine1Local",
            "fundTotalNetAssets", "fundTotalNetAssetsDate", "legalNameCHS", "companyNameCHS", "documentNumber", "designatedMedia",
            "companyWebsite"})
        public static class FundInfo {

            @XmlElement(name = "hhhh_FundCode")
            protected int hhhhFundCode;
            @XmlElement(name = "Symbol")
            protected int symbol;
            @XmlElement(name = "SecId", required = true)
            protected String secId;
            @XmlElement(name = "PerformanceId", required = true)
            protected String performanceId;
            @XmlElement(name = "DisplayNameLocal", required = true)
            protected String displayNameLocal;
            @XmlElement(name = "hhhh_RiskLevel")
            protected byte hhhhRiskLevel;
            @XmlElement(name = "FundGroup", required = true)
            protected String fundGroup;
            @XmlElement(name = "MRFNorth", required = true)
            protected String mrfNorth;
            @XmlElement(name = "hhhh_TopPerformers", required = true)
            protected DataSet.QuickTake.FundInfo.hhhhTopPerformers hhhhTopPerformers;
            @XmlElement(name = "hhhh_Bestsellers", required = true)
            protected String hhhhBestsellers;
            @XmlElement(name = "hhhh_FocusFund", required = true)
            protected String hhhhFocusFund;
            @XmlElement(name = "hhhh_NewFund", required = true)
            protected String hhhhNewFund;
            @XmlElement(name = "CategoryId", required = true)
            protected String categoryId;
            @XmlElement(name = "Category", required = true)
            protected String category;
            @XmlElement(name = "InceptionDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar inceptionDate;
            @XmlElement(name = "FundProfileLocal", required = true)
            protected String fundProfileLocal;
            @XmlElement(name = "BaseCurrencyLocal", required = true)
            protected String baseCurrencyLocal;
            @XmlElement(name = "CurrencyId", required = true)
            protected String currencyId;
            @XmlElement(name = "FundCompanyNameLocal", required = true)
            protected String fundCompanyNameLocal;
            @XmlElement(name = "CustodianNameLocal", required = true)
            protected String custodianNameLocal;
            @XmlElement(name = "FundCompanyTel", required = true)
            protected String fundCompanyTel;
            @XmlElement(name = "FundCompanyFax", required = true)
            protected String fundCompanyFax;
            @XmlElement(name = "FundCompanyHomePage", required = true)
            @XmlSchemaType(name = "anyURI")
            protected String fundCompanyHomePage;
            @XmlElement(name = "FundCompanyAddressLine1Local", required = true)
            protected String fundCompanyAddressLine1Local;
            @XmlElement(name = "FundTotalNetAssets")
            protected float fundTotalNetAssets;
            @XmlElement(name = "FundTotalNetAssetsDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar fundTotalNetAssetsDate;
            @XmlElement(name = "LegalName_CHS", required = true)
            protected String legalNameCHS;
            @XmlElement(name = "CompanyName_CHS", required = true)
            protected String companyNameCHS;
            @XmlElement(name = "DocumentNumber", required = true)
            protected String documentNumber;
            @XmlElement(name = "DesignatedMedia", required = true)
            protected String designatedMedia;
            @XmlElement(name = "CompanyWebsite", required = true)
            @XmlSchemaType(name = "anyURI")
            protected String companyWebsite;

            
            public int gethhhhFundCode() {
                return this.hhhhFundCode;
            }

            
            public void sethhhhFundCode(final int value) {
                this.hhhhFundCode = value;
            }

            
            public int getSymbol() {
                return this.symbol;
            }

            
            public void setSymbol(final int value) {
                this.symbol = value;
            }

            
            public String getSecId() {
                return this.secId;
            }

            
            public void setSecId(final String value) {
                this.secId = value;
            }

            
            public String getPerformanceId() {
                return this.performanceId;
            }

            
            public void setPerformanceId(final String value) {
                this.performanceId = value;
            }

            
            public String getDisplayNameLocal() {
                return this.displayNameLocal;
            }

            
            public void setDisplayNameLocal(final String value) {
                this.displayNameLocal = value;
            }

            
            public byte gethhhhRiskLevel() {
                return this.hhhhRiskLevel;
            }

            
            public void sethhhhRiskLevel(final byte value) {
                this.hhhhRiskLevel = value;
            }

            
            public String getFundGroup() {
                return this.fundGroup;
            }

            
            public void setFundGroup(final String value) {
                this.fundGroup = value;
            }

            
            public String getMRFNorth() {
                return this.mrfNorth;
            }

            
            public void setMRFNorth(final String value) {
                this.mrfNorth = value;
            }

            
            public DataSet.QuickTake.FundInfo.hhhhTopPerformers gethhhhTopPerformers() {
                return this.hhhhTopPerformers;
            }

            
            public void sethhhhTopPerformers(final DataSet.QuickTake.FundInfo.hhhhTopPerformers value) {
                this.hhhhTopPerformers = value;
            }

            
            public String gethhhhBestsellers() {
                return this.hhhhBestsellers;
            }

            
            public void sethhhhBestsellers(final String value) {
                this.hhhhBestsellers = value;
            }

            
            public String gethhhhFocusFund() {
                return this.hhhhFocusFund;
            }

            
            public void sethhhhFocusFund(final String value) {
                this.hhhhFocusFund = value;
            }

            
            public String gethhhhNewFund() {
                return this.hhhhNewFund;
            }

            
            public void sethhhhNewFund(final String value) {
                this.hhhhNewFund = value;
            }

            
            public String getCategoryId() {
                return this.categoryId;
            }

            
            public void setCategoryId(final String value) {
                this.categoryId = value;
            }

            
            public String getCategory() {
                return this.category;
            }

            
            public void setCategory(final String value) {
                this.category = value;
            }

            
            public XMLGregorianCalendar getInceptionDate() {
                return this.inceptionDate;
            }

            
            public void setInceptionDate(final XMLGregorianCalendar value) {
                this.inceptionDate = value;
            }

            
            public String getFundProfileLocal() {
                return this.fundProfileLocal;
            }

            
            public void setFundProfileLocal(final String value) {
                this.fundProfileLocal = value;
            }

            
            public String getBaseCurrencyLocal() {
                return this.baseCurrencyLocal;
            }

            
            public void setBaseCurrencyLocal(final String value) {
                this.baseCurrencyLocal = value;
            }

            
            public String getCurrencyId() {
                return this.currencyId;
            }

            
            public void setCurrencyId(final String value) {
                this.currencyId = value;
            }

            
            public String getFundCompanyNameLocal() {
                return this.fundCompanyNameLocal;
            }

            
            public void setFundCompanyNameLocal(final String value) {
                this.fundCompanyNameLocal = value;
            }

            
            public String getCustodianNameLocal() {
                return this.custodianNameLocal;
            }

            
            public void setCustodianNameLocal(final String value) {
                this.custodianNameLocal = value;
            }

            
            public String getFundCompanyTel() {
                return this.fundCompanyTel;
            }

            
            public void setFundCompanyTel(final String value) {
                this.fundCompanyTel = value;
            }

            
            public String getFundCompanyFax() {
                return this.fundCompanyFax;
            }

            
            public void setFundCompanyFax(final String value) {
                this.fundCompanyFax = value;
            }

            
            public String getFundCompanyHomePage() {
                return this.fundCompanyHomePage;
            }

            
            public void setFundCompanyHomePage(final String value) {
                this.fundCompanyHomePage = value;
            }

            
            public String getFundCompanyAddressLine1Local() {
                return this.fundCompanyAddressLine1Local;
            }

            
            public void setFundCompanyAddressLine1Local(final String value) {
                this.fundCompanyAddressLine1Local = value;
            }

            
            public float getFundTotalNetAssets() {
                return this.fundTotalNetAssets;
            }

            
            public void setFundTotalNetAssets(final float value) {
                this.fundTotalNetAssets = value;
            }

            
            public XMLGregorianCalendar getFundTotalNetAssetsDate() {
                return this.fundTotalNetAssetsDate;
            }

            
            public void setFundTotalNetAssetsDate(final XMLGregorianCalendar value) {
                this.fundTotalNetAssetsDate = value;
            }

            
            public String getLegalNameCHS() {
                return this.legalNameCHS;
            }

            
            public void setLegalNameCHS(final String value) {
                this.legalNameCHS = value;
            }

            
            public String getCompanyNameCHS() {
                return this.companyNameCHS;
            }

            
            public void setCompanyNameCHS(final String value) {
                this.companyNameCHS = value;
            }

            
            public String getDocumentNumber() {
                return this.documentNumber;
            }

            
            public void setDocumentNumber(final String value) {
                this.documentNumber = value;
            }

            
            public String getDesignatedMedia() {
                return this.designatedMedia;
            }

            
            public void setDesignatedMedia(final String value) {
                this.designatedMedia = value;
            }

            
            public String getCompanyWebsite() {
                return this.companyWebsite;
            }

            
            public void setCompanyWebsite(final String value) {
                this.companyWebsite = value;
            }


            
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"value"})
            public static class hhhhTopPerformers {

                @XmlValue
                protected String value;

                
                public String getValue() {
                    return this.value;
                }

                
                public void setValue(final String value) {
                    this.value = value;
                }

            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "managerName", "startDate", "biography", "managerWorkDate"})
        public static class FundManager {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "ManagerName", required = true)
            protected String managerName;
            @XmlElement(name = "StartDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected String startDate;
            @XmlElement(name = "Biography", required = true)
            protected String biography;
            @XmlElement(name = "ManagerWorkDate", required = true)
            protected String managerWorkDate;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public String getManagerName() {
                return this.managerName;
            }

            
            public void setManagerName(final String value) {
                this.managerName = value;
            }

            
            public String getStartDate() {
                return this.startDate;
            }

            
            public void setStartDate(final String startDate) {
                this.startDate = startDate;
            }

            
            public String getBiography() {
                return this.biography;
            }

            
            public void setBiography(final String value) {
                this.biography = value;
            }

            
            public String getManagerWorkDate() {
                return this.managerWorkDate;
            }

            
            public void setManagerWorkDate(final String value) {
                this.managerWorkDate = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "effectiveDate", "returnYTD", "returnYTDRank", "returnYTDToCat",
            "returnYTDCatSize", "return1Year", "return1YearToCat", "return1YearRank", "return1YearCatSize", "return2Year",
            "return2YearToCat", "return2YearRank", "return2YearCatSize", "return3Year", "return3YearToCat", "return3YearRank",
            "return3YearCatSize", "return5Year", "return5YearToCat", "return5YearRank", "return5YearCatSize"})
        public static class MonthlyReturn {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "EffectiveDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar effectiveDate;
            @XmlElement(name = "ReturnYTD")
            protected float returnYTD;
            @XmlElement(name = "ReturnYTDRank")
            protected short returnYTDRank;
            @XmlElement(name = "ReturnYTDToCat")
            protected float returnYTDToCat;
            @XmlElement(name = "ReturnYTDCatSize")
            protected short returnYTDCatSize;
            @XmlElement(name = "Return1Year")
            protected float return1Year;
            @XmlElement(name = "Return1YearToCat")
            protected float return1YearToCat;
            @XmlElement(name = "Return1YearRank")
            protected byte return1YearRank;
            @XmlElement(name = "Return1YearCatSize")
            protected short return1YearCatSize;
            @XmlElement(name = "Return2Year")
            protected float return2Year;
            @XmlElement(name = "Return2YearToCat")
            protected float return2YearToCat;
            @XmlElement(name = "Return2YearRank")
            protected byte return2YearRank;
            @XmlElement(name = "Return2YearCatSize")
            protected short return2YearCatSize;
            @XmlElement(name = "Return3Year")
            protected float return3Year;
            @XmlElement(name = "Return3YearToCat")
            protected float return3YearToCat;
            @XmlElement(name = "Return3YearRank")
            protected byte return3YearRank;
            @XmlElement(name = "Return3YearCatSize")
            protected short return3YearCatSize;
            @XmlElement(name = "Return5Year")
            protected float return5Year;
            @XmlElement(name = "Return5YearToCat")
            protected float return5YearToCat;
            @XmlElement(name = "Return5YearRank")
            protected byte return5YearRank;
            @XmlElement(name = "Return5YearCatSize")
            protected short return5YearCatSize;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public XMLGregorianCalendar getEffectiveDate() {
                return this.effectiveDate;
            }

            
            public void setEffectiveDate(final XMLGregorianCalendar value) {
                this.effectiveDate = value;
            }

            
            public float getReturnYTD() {
                return this.returnYTD;
            }

            
            public void setReturnYTD(final float value) {
                this.returnYTD = value;
            }

            
            public short getReturnYTDRank() {
                return this.returnYTDRank;
            }

            
            public void setReturnYTDRank(final short value) {
                this.returnYTDRank = value;
            }

            
            public float getReturnYTDToCat() {
                return this.returnYTDToCat;
            }

            
            public void setReturnYTDToCat(final float value) {
                this.returnYTDToCat = value;
            }


            public short getReturnYTDCatSize() {
                return this.returnYTDCatSize;
            }


            public void setReturnYTDCatSize(final short value) {
                this.returnYTDCatSize = value;
            }


            public float getReturn1Year() {
                return this.return1Year;
            }


            public void setReturn1Year(final float value) {
                this.return1Year = value;
            }


            public float getReturn1YearToCat() {
                return this.return1YearToCat;
            }


            public void setReturn1YearToCat(final float value) {
                this.return1YearToCat = value;
            }


            public byte getReturn1YearRank() {
                return this.return1YearRank;
            }

            public void setReturn1YearRank(final byte value) {
                this.return1YearRank = value;
            }


            public short getReturn1YearCatSize() {
                return this.return1YearCatSize;
            }


            public void setReturn1YearCatSize(final short value) {
                this.return1YearCatSize = value;
            }


            public float getReturn2Year() {
                return this.return2Year;
            }


            public void setReturn2Year(final float value) {
                this.return2Year = value;
            }


            public float getReturn2YearToCat() {
                return this.return2YearToCat;
            }


            public void setReturn2YearToCat(final float value) {
                this.return2YearToCat = value;
            }


            public byte getReturn2YearRank() {
                return this.return2YearRank;
            }


            public void setReturn2YearRank(final byte value) {
                this.return2YearRank = value;
            }


            public short getReturn2YearCatSize() {
                return this.return2YearCatSize;
            }


            public void setReturn2YearCatSize(final short value) {
                this.return2YearCatSize = value;
            }


            public float getReturn3Year() {
                return this.return3Year;
            }


            public void setReturn3Year(final float value) {
                this.return3Year = value;
            }


            public float getReturn3YearToCat() {
                return this.return3YearToCat;
            }


            public void setReturn3YearToCat(final float value) {
                this.return3YearToCat = value;
            }


            public byte getReturn3YearRank() {
                return this.return3YearRank;
            }


            public void setReturn3YearRank(final byte value) {
                this.return3YearRank = value;
            }


            public short getReturn3YearCatSize() {
                return this.return3YearCatSize;
            }


            public void setReturn3YearCatSize(final short value) {
                this.return3YearCatSize = value;
            }


            public float getReturn5Year() {
                return this.return5Year;
            }


            public void setReturn5Year(final float value) {
                this.return5Year = value;
            }


            public float getReturn5YearToCat() {
                return this.return5YearToCat;
            }


            public void setReturn5YearToCat(final float value) {
                this.return5YearToCat = value;
            }


            public byte getReturn5YearRank() {
                return this.return5YearRank;
            }


            public void setReturn5YearRank(final byte value) {
                this.return5YearRank = value;
            }


            public short getReturn5YearCatSize() {
                return this.return5YearCatSize;
            }


            public void setReturn5YearCatSize(final short value) {
                this.return5YearCatSize = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"language", "id", "item", "title"})
        public static class PDFRDBTable {

            @XmlElement(name = "Language", required = true)
            protected String language;
            @XmlElement(name = "Id")
            protected byte id;
            @XmlElement(name = "Item", required = true)
            protected String item;
            @XmlElement(name = "Title", required = true)
            protected String title;


            public String getLanguage() {
                return this.language;
            }


            public void setLanguage(final String value) {
                this.language = value;
            }


            public byte getId() {
                return this.id;
            }


            public void setId(final byte value) {
                this.id = value;
            }


            public String getItem() {
                return this.item;
            }


            public void setItem(final String value) {
                this.item = value;
            }


            public String getTitle() {
                return this.title;
            }


            public void setTitle(final String value) {
                this.title = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"code", "pieChartType", "nameField", "valueField", "colorField"})
        public static class PieChart {

            @XmlElement(name = "Code")
            protected int code;
            @XmlElement(name = "PieChartType", required = true)
            protected String pieChartType;
            @XmlElement(name = "NameField", required = true)
            protected String nameField;
            @XmlElement(name = "ValueField", required = true)
            protected String valueField;
            @XmlElement(name = "ColorField", required = true)
            protected String colorField;


            public int getCode() {
                return this.code;
            }


            public void setCode(final int value) {
                this.code = value;
            }


            public String getPieChartType() {
                return this.pieChartType;
            }


            public void setPieChartType(final String value) {
                this.pieChartType = value;
            }


            public String getNameField() {
                return this.nameField;
            }


            public void setNameField(final String value) {
                this.nameField = value;
            }


            public String getValueField() {
                return this.valueField;
            }


            public void setValueField(final String value) {
                this.valueField = value;
            }


            public String getColorField() {
                return this.colorField;
            }


            public void setColorField(final String value) {
                this.colorField = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "assetAllocatioStocks", "assetAllocatioBonds", "assetAllocatioCash",
            "assetAllocatioOthers", "top10HldgPercent", "top5BondHldgPercent", "styleBox", "styleBoxDate", "effectiveDate",
            "styleboxStyle", "styleboxCap", "investmentInstrument", "totalNumOfStockHldg", "totalNumOfBondHldg"})
        public static class PortfolioSummary {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "AssetAllocatio_Stocks")
            protected float assetAllocatioStocks;
            @XmlElement(name = "AssetAllocatio_Bonds")
            protected float assetAllocatioBonds;
            @XmlElement(name = "AssetAllocatio_Cash")
            protected float assetAllocatioCash;
            @XmlElement(name = "AssetAllocatio_Others")
            protected float assetAllocatioOthers;
            @XmlElement(name = "Top10HldgPercent")
            protected float top10HldgPercent;
            @XmlElement(name = "Top5BondHldgPercent")
            protected float top5BondHldgPercent;
            @XmlElement(name = "StyleBox")
            protected String styleBox;
            @XmlElement(name = "StyleBoxDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar styleBoxDate;
            @XmlElement(name = "EffectiveDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected String effectiveDate;
            @XmlElement(name = "StyleboxStyle", required = true)
            protected String styleboxStyle;
            @XmlElement(name = "StyleboxCap", required = true)
            protected String styleboxCap;
            @XmlElement(name = "InvestmentInstrument", required = true)
            protected String investmentInstrument;
            @XmlElement(name = "TotalNumOfStockHldg")
            protected byte totalNumOfStockHldg;
            @XmlElement(name = "TotalNumOfBondHldg")
            protected byte totalNumOfBondHldg;


            public int getFundCode() {
                return this.fundCode;
            }


            public void setFundCode(final int value) {
                this.fundCode = value;
            }


            public float getAssetAllocatioStocks() {
                return this.assetAllocatioStocks;
            }


            public void setAssetAllocatioStocks(final float value) {
                this.assetAllocatioStocks = value;
            }


            public float getAssetAllocatioBonds() {
                return this.assetAllocatioBonds;
            }


            public void setAssetAllocatioBonds(final float value) {
                this.assetAllocatioBonds = value;
            }


            public float getAssetAllocatioCash() {
                return this.assetAllocatioCash;
            }


            public void setAssetAllocatioCash(final float value) {
                this.assetAllocatioCash = value;
            }


            public float getAssetAllocatioOthers() {
                return this.assetAllocatioOthers;
            }


            public void setAssetAllocatioOthers(final float value) {
                this.assetAllocatioOthers = value;
            }


            public float getTop10HldgPercent() {
                return this.top10HldgPercent;
            }


            public void setTop10HldgPercent(final float value) {
                this.top10HldgPercent = value;
            }


            public float getTop5BondHldgPercent() {
                return this.top5BondHldgPercent;
            }


            public void setTop5BondHldgPercent(final float value) {
                this.top5BondHldgPercent = value;
            }

            public String getStyleBox() {
                return this.styleBox;
            }


            public void setStyleBox(final String styleBox) {
                this.styleBox = styleBox;
            }


            public XMLGregorianCalendar getStyleBoxDate() {
                return this.styleBoxDate;
            }


            public void setStyleBoxDate(final XMLGregorianCalendar value) {
                this.styleBoxDate = value;
            }


            public String getEffectiveDate() {
                return this.effectiveDate;
            }


            public void setEffectiveDate(final String effectiveDate) {
                this.effectiveDate = effectiveDate;
            }

            public String getStyleboxStyle() {
                return this.styleboxStyle;
            }


            public void setStyleboxStyle(final String value) {
                this.styleboxStyle = value;
            }


            public String getStyleboxCap() {
                return this.styleboxCap;
            }


            public void setStyleboxCap(final String value) {
                this.styleboxCap = value;
            }


            public String getInvestmentInstrument() {
                return this.investmentInstrument;
            }


            public void setInvestmentInstrument(final String value) {
                this.investmentInstrument = value;
            }


            public byte getTotalNumOfStockHldg() {
                return this.totalNumOfStockHldg;
            }


            public void setTotalNumOfStockHldg(final byte value) {
                this.totalNumOfStockHldg = value;
            }


            public byte getTotalNumOfBondHldg() {
                return this.totalNumOfBondHldg;
            }


            public void setTotalNumOfBondHldg(final byte value) {
                this.totalNumOfBondHldg = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "nav", "dailyChange", "high52Week", "low52Week", "effectiveDate"})
        public static class Price {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "NAV")
            protected float nav;
            @XmlElement(name = "DailyChange")
            protected float dailyChange;
            @XmlElement(name = "High52Week")
            protected float high52Week;
            @XmlElement(name = "Low52Week")
            protected float low52Week;
            @XmlElement(name = "EffectiveDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar effectiveDate;


            public int getFundCode() {
                return this.fundCode;
            }


            public void setFundCode(final int value) {
                this.fundCode = value;
            }


            public float getNAV() {
                return this.nav;
            }


            public void setNAV(final float value) {
                this.nav = value;
            }


            public float getDailyChange() {
                return this.dailyChange;
            }


            public void setDailyChange(final float value) {
                this.dailyChange = value;
            }


            public float getHigh52Week() {
                return this.high52Week;
            }


            public void setHigh52Week(final float value) {
                this.high52Week = value;
            }


            public float getLow52Week() {
                return this.low52Week;
            }


            public void setLow52Week(final float value) {
                this.low52Week = value;
            }


            public XMLGregorianCalendar getEffectiveDate() {
                return this.effectiveDate;
            }


            public void setEffectiveDate(final XMLGregorianCalendar value) {
                this.effectiveDate = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "priceDate", "price", "accPrice"})
        public static class PriceInfo {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "PriceDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar priceDate;
            @XmlElement(name = "Price")
            protected float price;
            @XmlElement(name = "AccPrice")
            protected float accPrice;


            public int getFundCode() {
                return this.fundCode;
            }


            public void setFundCode(final int value) {
                this.fundCode = value;
            }


            public XMLGregorianCalendar getPriceDate() {
                return this.priceDate;
            }


            public void setPriceDate(final XMLGregorianCalendar value) {
                this.priceDate = value;
            }


            public float getPrice() {
                return this.price;
            }


            public void setPrice(final float value) {
                this.price = value;
            }


            public float getAccPrice() {
                return this.accPrice;
            }


            public void setAccPrice(final float value) {
                this.accPrice = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"language", "id", "item", "title"})
        public static class RDBTable {

            @XmlElement(name = "Language", required = true)
            protected String language;
            @XmlElement(name = "Id")
            protected byte id;
            @XmlElement(name = "Item", required = true)
            protected String item;
            @XmlElement(name = "Title", required = true)
            protected String title;


            public String getLanguage() {
                return this.language;
            }


            public void setLanguage(final String value) {
                this.language = value;
            }


            public byte getId() {
                return this.id;
            }


            public void setId(final byte value) {
                this.id = value;
            }


            public String getItem() {
                return this.item;
            }


            public void setItem(final String value) {
                this.item = value;
            }


            public String getTitle() {
                return this.title;
            }


            public void setTitle(final String value) {
                this.title = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "ratingDate", "rating3Year", "rating5Year"})
        public static class Rating {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "RatingDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar ratingDate;
            @XmlElement(name = "Rating3Year")
            protected byte rating3Year;
            @XmlElement(name = "Rating5Year")
            protected byte rating5Year;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public XMLGregorianCalendar getRatingDate() {
                return this.ratingDate;
            }

            
            public void setRatingDate(final XMLGregorianCalendar value) {
                this.ratingDate = value;
            }

            
            public byte getRating3Year() {
                return this.rating3Year;
            }

            
            public void setRating3Year(final byte value) {
                this.rating3Year = value;
            }

            
            public byte getRating5Year() {
                return this.rating5Year;
            }

            
            public void setRating5Year(final byte value) {
                this.rating5Year = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "key", "value"})
        public static class RedemptionFee {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "Key", required = true)
            protected String key;
            @XmlElement(name = "Value", required = true)
            protected String value;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public String getKey() {
                return this.key;
            }

            
            public void setKey(final String value) {
                this.key = value;
            }

            
            public String getValue() {
                return this.value;
            }

            
            public void setValue(final String value) {
                this.value = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "ratingDate", "std3Year", "sRatio3Year", "alpha3YearIdx", "beta3YearIdx"})
        public static class Risk {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "RatingDate", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar ratingDate;
            @XmlElement(name = "Std3Year")
            protected float std3Year;
            @XmlElement(name = "SRatio3Year")
            protected float sRatio3Year;
            @XmlElement(name = "Alpha3YearIdx")
            protected float alpha3YearIdx;
            @XmlElement(name = "Beta3YearIdx")
            protected float beta3YearIdx;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public XMLGregorianCalendar getRatingDate() {
                return this.ratingDate;
            }

            
            public void setRatingDate(final XMLGregorianCalendar value) {
                this.ratingDate = value;
            }

            
            public float getStd3Year() {
                return this.std3Year;
            }

            
            public void setStd3Year(final float value) {
                this.std3Year = value;
            }

            
            public float getSRatio3Year() {
                return this.sRatio3Year;
            }

            
            public void setSRatio3Year(final float value) {
                this.sRatio3Year = value;
            }

            
            public float getAlpha3YearIdx() {
                return this.alpha3YearIdx;
            }

            
            public void setAlpha3YearIdx(final float value) {
                this.alpha3YearIdx = value;
            }

            
            public float getBeta3YearIdx() {
                return this.beta3YearIdx;
            }

            
            public void setBeta3YearIdx(final float value) {
                this.beta3YearIdx = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "type", "sectorNameLocal", "tBreakdownValue"})
        public static class SectorBreakdown {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "Type")
            protected short type;
            @XmlElement(name = "SectorNameLocal", required = true)
            protected String sectorNameLocal;
            @XmlElement(name = "TBreakdownValue")
            protected Float tBreakdownValue;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public short getType() {
                return this.type;
            }

            
            public void setType(final short value) {
                this.type = value;
            }

            
            public String getSectorNameLocal() {
                return this.sectorNameLocal;
            }

            
            public void setSectorNameLocal(final String value) {
                this.sectorNameLocal = value;
            }

            
            public Float getTBreakdownValue() {
                return this.tBreakdownValue;
            }

            
            public void setTBreakdownValue(final Float value) {
                this.tBreakdownValue = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "holdingId", "holdingName", "holdingNameEn", "marketValue",
            "netAssetPercentage"})
        public static class TopHolding {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "HoldingId")
            protected int holdingId;
            @XmlElement(name = "HoldingName", required = true)
            protected String holdingName;
            @XmlElement(name = "HoldingNameEn")
            protected String holdingNameEn;
            @XmlElement(name = "MarketValue")
            protected int marketValue;
            @XmlElement(name = "NetAssetPercentage")
            protected float netAssetPercentage;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public int getHoldingId() {
                return this.holdingId;
            }

            
            public void setHoldingId(final int value) {
                this.holdingId = value;
            }

            
            public String getHoldingName() {
                return this.holdingName;
            }

            
            public void setHoldingName(final String value) {
                this.holdingName = value;
            }

            
            public String getHoldingNameEn() {
                return this.holdingNameEn;
            }

            
            public void setHoldingNameEn(final String holdingNameEn) {
                this.holdingNameEn = holdingNameEn;
            }

            
            public int getMarketValue() {
                return this.marketValue;
            }

            
            public void setMarketValue(final int value) {
                this.marketValue = value;
            }

            
            public float getNetAssetPercentage() {
                return this.netAssetPercentage;
            }

            
            public void setNetAssetPercentage(final float value) {
                this.netAssetPercentage = value;
            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fundCode", "endDate", "value"})
        public static class Yield {

            @XmlElement(name = "FundCode")
            protected int fundCode;
            @XmlElement(name = "EndDate", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar endDate;
            @XmlElement(name = "Value")
            protected float value;

            
            public int getFundCode() {
                return this.fundCode;
            }

            
            public void setFundCode(final int value) {
                this.fundCode = value;
            }

            
            public XMLGregorianCalendar getEndDate() {
                return this.endDate;
            }

            
            public void setEndDate(final XMLGregorianCalendar value) {
                this.endDate = value;
            }

            
            public float getValue() {
                return this.value;
            }

            
            public void setValue(final float value) {
                this.value = value;
            }

        }

    }
}
