
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;


public class FundCumulativeReturn {

    private String period;
    private BigDecimal trailingTotalReturn;
    private BigDecimal dailyPerformanceNAV;


    public String getPeriod() {
        return this.period;
    }


    public void setPeriod(final String period) {
        this.period = period;
    }


    public BigDecimal getTrailingTotalReturn() {
        return this.trailingTotalReturn;
    }


    public void setTrailingTotalReturn(final BigDecimal trailingTotalReturn) {
        this.trailingTotalReturn = trailingTotalReturn;
    }


    public BigDecimal getDailyPerformanceNAV() {
        return this.dailyPerformanceNAV;
    }


    public void setDailyPerformanceNAV(final BigDecimal dailyPerformanceNAV) {
        this.dailyPerformanceNAV = dailyPerformanceNAV;
    }


}
