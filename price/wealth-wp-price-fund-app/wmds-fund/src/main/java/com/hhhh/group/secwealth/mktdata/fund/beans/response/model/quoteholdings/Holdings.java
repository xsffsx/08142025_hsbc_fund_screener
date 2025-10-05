
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteholdings;

import java.math.BigDecimal;


public class Holdings {

    private Fundamentals fundamentals;

    public Fundamentals getFundamentals() {
        return this.fundamentals;
    }

    public void setFundamentals(final Fundamentals fundamentals) {
        this.fundamentals = fundamentals;
    }


    public class Fundamentals {
        private AllHoldings allHoldings;
        private StockHoldings stockHoldings;
        private FixedIncomeHoldings fixedIncomeHoldings;
        private String lastUpdatedDate;

        public AllHoldings getAllHoldings() {
            return this.allHoldings;
        }

        public void setAllHoldings(final AllHoldings allHoldings) {
            this.allHoldings = allHoldings;
        }

        public StockHoldings getStockHoldings() {
            return this.stockHoldings;
        }

        public void setStockHoldings(final StockHoldings stockHoldings) {
            this.stockHoldings = stockHoldings;
        }

        public FixedIncomeHoldings getFixedIncomeHoldings() {
            return this.fixedIncomeHoldings;
        }

        public void setFixedIncomeHoldings(final FixedIncomeHoldings fixedIncomeHoldings) {
            this.fixedIncomeHoldings = fixedIncomeHoldings;
        }

        public String getLastUpdatedDate() {
            return this.lastUpdatedDate;
        }

        public void setLastUpdatedDate(final String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }


        public class AllHoldings {
            private BigDecimal assetsUnderManagement;
            private String assetsUnderManagementCurrencyCode;
            private BigDecimal assetsUnderManagementCategoryAvg;
            private String assetsUnderManagementCategoryAvgCurrencyCode;
            private BigDecimal annualPortfolioTurnover;
            private BigDecimal annualPortfolioTurnoverCategoryAvg;


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


            public BigDecimal getAssetsUnderManagementCategoryAvg() {
                return this.assetsUnderManagementCategoryAvg;
            }


            public void setAssetsUnderManagementCategoryAvg(final BigDecimal assetsUnderManagementCategoryAvg) {
                this.assetsUnderManagementCategoryAvg = assetsUnderManagementCategoryAvg;
            }

            public String getAssetsUnderManagementCategoryAvgCurrencyCode() {
                return this.assetsUnderManagementCategoryAvgCurrencyCode;
            }

            public void setAssetsUnderManagementCategoryAvgCurrencyCode(final String assetsUnderManagementCategoryAvgCurrencyCode) {
                this.assetsUnderManagementCategoryAvgCurrencyCode = assetsUnderManagementCategoryAvgCurrencyCode;
            }


            public BigDecimal getAnnualPortfolioTurnover() {
                return this.annualPortfolioTurnover;
            }


            public void setAnnualPortfolioTurnover(final BigDecimal annualPortfolioTurnover) {
                this.annualPortfolioTurnover = annualPortfolioTurnover;
            }


            public BigDecimal getAnnualPortfolioTurnoverCategoryAvg() {
                return this.annualPortfolioTurnoverCategoryAvg;
            }


            public void setAnnualPortfolioTurnoverCategoryAvg(final BigDecimal annualPortfolioTurnoverCategoryAvg) {
                this.annualPortfolioTurnoverCategoryAvg = annualPortfolioTurnoverCategoryAvg;
            }

        }


        public class StockHoldings {
            private BigDecimal priceEarnings;
            private BigDecimal priceEarningsCategoryAvg;
            private BigDecimal priceBook;
            private BigDecimal priceBookCategoryAvg;
            private BigDecimal returnOnAssets;
            private BigDecimal returnOnAssetsCategoryAvg;
            private BigDecimal returnOnEquity;
            private BigDecimal returnOnEquityCategoryAvg;
            private BigDecimal dividendYield;
            private BigDecimal dividendYieldCategoryAvg;


            public BigDecimal getPriceEarnings() {
                return this.priceEarnings;
            }


            public void setPriceEarnings(final BigDecimal priceEarnings) {
                this.priceEarnings = priceEarnings;
            }


            public BigDecimal getPriceEarningsCategoryAvg() {
                return this.priceEarningsCategoryAvg;
            }


            public void setPriceEarningsCategoryAvg(final BigDecimal priceEarningsCategoryAvg) {
                this.priceEarningsCategoryAvg = priceEarningsCategoryAvg;
            }


            public BigDecimal getPriceBook() {
                return this.priceBook;
            }


            public void setPriceBook(final BigDecimal priceBook) {
                this.priceBook = priceBook;
            }


            public BigDecimal getPriceBookCategoryAvg() {
                return this.priceBookCategoryAvg;
            }


            public void setPriceBookCategoryAvg(final BigDecimal priceBookCategoryAvg) {
                this.priceBookCategoryAvg = priceBookCategoryAvg;
            }


            public BigDecimal getReturnOnAssets() {
                return this.returnOnAssets;
            }


            public void setReturnOnAssets(final BigDecimal returnOnAssets) {
                this.returnOnAssets = returnOnAssets;
            }


            public BigDecimal getReturnOnAssetsCategoryAvg() {
                return this.returnOnAssetsCategoryAvg;
            }


            public void setReturnOnAssetsCategoryAvg(final BigDecimal returnOnAssetsCategoryAvg) {
                this.returnOnAssetsCategoryAvg = returnOnAssetsCategoryAvg;
            }


            public BigDecimal getReturnOnEquity() {
                return this.returnOnEquity;
            }


            public void setReturnOnEquity(final BigDecimal returnOnEquity) {
                this.returnOnEquity = returnOnEquity;
            }


            public BigDecimal getReturnOnEquityCategoryAvg() {
                return this.returnOnEquityCategoryAvg;
            }


            public void setReturnOnEquityCategoryAvg(final BigDecimal returnOnEquityCategoryAvg) {
                this.returnOnEquityCategoryAvg = returnOnEquityCategoryAvg;
            }


            public BigDecimal getDividendYield() {
                return this.dividendYield;
            }


            public void setDividendYield(final BigDecimal dividendYield) {
                this.dividendYield = dividendYield;
            }


            public BigDecimal getDividendYieldCategoryAvg() {
                return this.dividendYieldCategoryAvg;
            }


            public void setDividendYieldCategoryAvg(final BigDecimal dividendYieldCategoryAvg) {
                this.dividendYieldCategoryAvg = dividendYieldCategoryAvg;
            }
        }


        public class FixedIncomeHoldings {
            private String creditRating;
            private String creditRatingCategoryAvg;
            private BigDecimal effectiveMaturity;
            private String effectiveMaturityCategoryAvg;
            private BigDecimal yieldToMaturity;
            private String yieldToMaturityCategoryAvg;

            public String getCreditRating() {
                return this.creditRating;
            }

            public void setCreditRating(final String creditRating) {
                this.creditRating = creditRating;
            }

            public String getCreditRatingCategoryAvg() {
                return this.creditRatingCategoryAvg;
            }

            public void setCreditRatingCategoryAvg(final String creditRatingCategoryAvg) {
                this.creditRatingCategoryAvg = creditRatingCategoryAvg;
            }


            public BigDecimal getEffectiveMaturity() {
                return this.effectiveMaturity;
            }


            public void setEffectiveMaturity(final BigDecimal effectiveMaturity) {
                this.effectiveMaturity = effectiveMaturity;
            }

            public String getEffectiveMaturityCategoryAvg() {
                return this.effectiveMaturityCategoryAvg;
            }

            public void setEffectiveMaturityCategoryAvg(final String effectiveMaturityCategoryAvg) {
                this.effectiveMaturityCategoryAvg = effectiveMaturityCategoryAvg;
            }

            public BigDecimal getYieldToMaturity() {
                return this.yieldToMaturity;
            }

            public void setYieldToMaturity(final BigDecimal yieldToMaturity) {
                this.yieldToMaturity = yieldToMaturity;
            }

            public String getYieldToMaturityCategoryAvg() {
                return this.yieldToMaturityCategoryAvg;
            }

            public void setYieldToMaturityCategoryAvg(final String yieldToMaturityCategoryAvg) {
                this.yieldToMaturityCategoryAvg = yieldToMaturityCategoryAvg;
            }

        }
    }


}
