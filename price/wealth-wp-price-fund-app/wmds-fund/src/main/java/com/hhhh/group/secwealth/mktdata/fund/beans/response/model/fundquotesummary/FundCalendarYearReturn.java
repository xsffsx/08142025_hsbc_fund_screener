package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundquotesummary;

import java.math.BigDecimal;
import java.util.List;

public class FundCalendarYearReturn {

    private BigDecimal year;
    private BigDecimal fundCalendarYearReturn;
    private BigDecimal fundStubYearEndReturn;
    private Character stubYearEndReturnIndicator;
    private BigDecimal bestFitIndexCalendarYearReturn;
    private BigDecimal categoryCalendarYearReturn;
    private List<Prospectus> prospectusPrimaryIndexYearReturns;


    public BigDecimal getYear() {
        return this.year;
    }


    public void setYear(final BigDecimal year) {
        this.year = year;
    }


    public BigDecimal getFundCalendarYearReturn() {
        return this.fundCalendarYearReturn;
    }


    public void setFundCalendarYearReturn(final BigDecimal fundCalendarYearReturn) {
        this.fundCalendarYearReturn = fundCalendarYearReturn;
    }


    public BigDecimal getFundStubYearEndReturn() {
        return this.fundStubYearEndReturn;
    }


    public void setFundStubYearEndReturn(final BigDecimal fundStubYearEndReturn) {
        this.fundStubYearEndReturn = fundStubYearEndReturn;
    }


    public Character getStubYearEndReturnIndicator() {
        return this.stubYearEndReturnIndicator;
    }


    public void setStubYearEndReturnIndicator(final Character stubYearEndReturnIndicator) {
        this.stubYearEndReturnIndicator = stubYearEndReturnIndicator;
    }


    public BigDecimal getBestFitIndexCalendarYearReturn() {
        return this.bestFitIndexCalendarYearReturn;
    }


    public void setBestFitIndexCalendarYearReturn(final BigDecimal bestFitIndexCalendarYearReturn) {
        this.bestFitIndexCalendarYearReturn = bestFitIndexCalendarYearReturn;
    }


    public BigDecimal getCategoryCalendarYearReturn() {
        return this.categoryCalendarYearReturn;
    }


    public void setCategoryCalendarYearReturn(final BigDecimal categoryCalendarYearReturn) {
        this.categoryCalendarYearReturn = categoryCalendarYearReturn;
    }


    public List<Prospectus> getProspectusPrimaryIndexYearReturns() {
        return this.prospectusPrimaryIndexYearReturns;
    }


    public void setProspectusPrimaryIndexYearReturns(final List<Prospectus> prospectusPrimaryIndexYearReturns) {
        this.prospectusPrimaryIndexYearReturns = prospectusPrimaryIndexYearReturns;
    }
}
