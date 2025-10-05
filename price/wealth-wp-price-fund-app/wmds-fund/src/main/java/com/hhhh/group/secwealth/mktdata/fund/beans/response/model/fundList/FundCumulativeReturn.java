
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList;

import java.math.BigDecimal;
import java.util.List;

public class FundCumulativeReturn {

    private String period;
    private BigDecimal totalReturn;
    private BigDecimal totalDailyReturn;
    private BigDecimal ttrBestFitIndexReturn;
    private BigDecimal categoryReturn;
    private BigDecimal categoryDailyReturn;
    private BigDecimal primaryIndexReturn;
    private List<Prospectus> prospectusPrimaryIndexReturns;


    public String getPeriod() {
        return this.period;
    }


    public void setPeriod(final String period) {
        this.period = period;
    }



    public BigDecimal getTotalReturn() {
        return this.totalReturn;
    }


    public void setTotalReturn(final BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }


    public BigDecimal getTotalDailyReturn() {
        return this.totalDailyReturn;
    }


    public void setTotalDailyReturn(final BigDecimal totalDailyReturn) {
        this.totalDailyReturn = totalDailyReturn;
    }


    public BigDecimal getTtrBestFitIndexReturn() {
        return this.ttrBestFitIndexReturn;
    }


    public void setTtrBestFitIndexReturn(final BigDecimal ttrBestFitIndexReturn) {
        this.ttrBestFitIndexReturn = ttrBestFitIndexReturn;
    }


    public BigDecimal getCategoryReturn() {
        return this.categoryReturn;
    }


    public void setCategoryReturn(final BigDecimal categoryReturn) {
        this.categoryReturn = categoryReturn;
    }


    public BigDecimal getCategoryDailyReturn() {
        return this.categoryDailyReturn;
    }


    public void setCategoryDailyReturn(final BigDecimal categoryDailyReturn) {
        this.categoryDailyReturn = categoryDailyReturn;
    }


    public BigDecimal getPrimaryIndexReturn() {
        return this.primaryIndexReturn;
    }


    public void setPrimaryIndexReturn(final BigDecimal primaryIndexReturn) {
        this.primaryIndexReturn = primaryIndexReturn;
    }


    public List<Prospectus> getProspectusPrimaryIndexReturns() {
        return this.prospectusPrimaryIndexReturns;
    }


    public void setProspectusPrimaryIndexReturns(final List<Prospectus> prospectusPrimaryIndexReturns) {
        this.prospectusPrimaryIndexReturns = prospectusPrimaryIndexReturns;
    }

}
