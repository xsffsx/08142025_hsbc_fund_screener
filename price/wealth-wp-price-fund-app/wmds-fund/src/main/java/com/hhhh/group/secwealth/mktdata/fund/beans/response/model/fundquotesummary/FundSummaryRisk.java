
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary;

import java.math.BigDecimal;

public class FundSummaryRisk {

    private YearRisk yearRisk;


    public YearRisk getYearRisk() {
        return this.yearRisk;
    }

    public void setYearRisk(final YearRisk yearRisk) {
        this.yearRisk = yearRisk;
    }

    public class YearRisk {
        private Integer year;
        private BigDecimal beta;
        private BigDecimal stdDev;
        private BigDecimal alpha;
        private BigDecimal sharpeRatio;
        private BigDecimal rSquared;
        private BigDecimal totalReturn;// Annualised return
        private String endDate;

        public Integer getYear() {
            return this.year;
        }

        public void setYear(final Integer year) {
            this.year = year;
        }

        
        public BigDecimal getBeta() {
            return this.beta;
        }

        
        public void setBeta(final BigDecimal beta) {
            this.beta = beta;
        }

        
        public BigDecimal getStdDev() {
            return this.stdDev;
        }

        
        public void setStdDev(final BigDecimal stdDev) {
            this.stdDev = stdDev;
        }

        
        public BigDecimal getAlpha() {
            return this.alpha;
        }

        
        public BigDecimal getSharpeRatio() {
            return this.sharpeRatio;
        }

        
        public void setSharpeRatio(final BigDecimal sharpeRatio) {
            this.sharpeRatio = sharpeRatio;
        }

        
        public BigDecimal getrSquared() {
            return this.rSquared;
        }

        
        public void setrSquared(final BigDecimal rSquared) {
            this.rSquared = rSquared;
        }

        
        public void setAlpha(final BigDecimal alpha) {
            this.alpha = alpha;
        }

        
        public BigDecimal getTotalReturn() {
            return this.totalReturn;
        }

        
        public void setTotalReturn(final BigDecimal totalReturn) {
            this.totalReturn = totalReturn;
        }

        
        public String getEndDate() {
            return this.endDate;
        }

        
        public void setEndDate(final String endDate) {
            this.endDate = endDate;
        }
    }

}
