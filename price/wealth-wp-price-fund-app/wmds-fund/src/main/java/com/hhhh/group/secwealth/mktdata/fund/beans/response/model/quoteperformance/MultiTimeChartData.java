package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance;

import java.math.BigDecimal;


public class MultiTimeChartData {

    private String multipleTimePeriod;
    private BigDecimal q0;
    private BigDecimal q25;
    private BigDecimal q50;
    private BigDecimal q75;
    private BigDecimal q100;
    private BigDecimal div;

    public String getMultipleTimePeriod() {
        return this.multipleTimePeriod;
    }

    public void setMultipleTimePeriod(final String multipleTimePeriod) {
        this.multipleTimePeriod = multipleTimePeriod;
    }

    
    public BigDecimal getQ0() {
        return this.q0;
    }

    
    public void setQ0(final BigDecimal q0) {
        this.q0 = q0;
    }

    
    public BigDecimal getQ25() {
        return this.q25;
    }

    
    public void setQ25(final BigDecimal q25) {
        this.q25 = q25;
    }

    
    public BigDecimal getQ50() {
        return this.q50;
    }

    
    public void setQ50(final BigDecimal q50) {
        this.q50 = q50;
    }

    
    public BigDecimal getQ75() {
        return this.q75;
    }

    
    public void setQ75(final BigDecimal q75) {
        this.q75 = q75;
    }

    
    public BigDecimal getQ100() {
        return this.q100;
    }

    
    public void setQ100(final BigDecimal q100) {
        this.q100 = q100;
    }

    
    public BigDecimal getDiv() {
        return this.div;
    }

    
    public void setDiv(final BigDecimal div) {
        this.div = div;
    }

}
