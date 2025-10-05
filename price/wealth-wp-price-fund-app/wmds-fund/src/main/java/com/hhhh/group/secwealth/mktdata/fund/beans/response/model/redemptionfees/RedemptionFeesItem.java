
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees;

import java.math.BigDecimal;

public class RedemptionFeesItem {

    protected String unit;
    protected String breakpointUnit;
    protected BigDecimal lowBreakpoint;
    protected BigDecimal highBreakpoint;
    protected BigDecimal value;


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


    public BigDecimal getHighBreakpoint() {
        return this.highBreakpoint;
    }


    public void setHighBreakpoint(final BigDecimal highBreakpoint) {
        this.highBreakpoint = highBreakpoint;
    }


    public BigDecimal getValue() {
        return this.value;
    }


    public void setValue(final BigDecimal value) {
        this.value = value;
    }
}
