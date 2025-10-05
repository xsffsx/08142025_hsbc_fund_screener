
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees;

import java.math.BigDecimal;

public class RedemptionFeesProspectusCustodianFee {

    private String unit;
    private String breakpointUnit;
    private BigDecimal lowBreakpoint;
    private BigDecimal value;


    public String getUnit() {
        return this.unit;
    }


    public void setUnit(final String unit) {
        this.unit = unit;
    }


    public String getBreakpointUnit() {
        return this.breakpointUnit;
    }


    public void setBreakpointUnit(final String breakpointUnit) {
        this.breakpointUnit = breakpointUnit;
    }


    public BigDecimal getLowBreakpoint() {
        return this.lowBreakpoint;
    }


    public void setLowBreakpoint(final BigDecimal lowBreakpoint) {
        this.lowBreakpoint = lowBreakpoint;
    }


    public BigDecimal getValue() {
        return this.value;
    }


    public void setValue(final BigDecimal value) {
        this.value = value;
    }
}
