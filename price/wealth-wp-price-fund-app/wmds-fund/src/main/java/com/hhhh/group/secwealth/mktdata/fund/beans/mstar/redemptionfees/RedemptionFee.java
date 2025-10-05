
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"unit", "breakpointUnit", "lowBreakpoint", "highBreakpoint", "value"})
@XmlRootElement(name = "RedemptionFee")
public class RedemptionFee {

    @XmlElement(name = "Unit", required = true)
    protected String unit;
    @XmlElement(name = "BreakpointUnit", required = true)
    protected String breakpointUnit;
    @XmlElement(name = "LowBreakpoint")
    protected String lowBreakpoint;
    @XmlElement(name = "HighBreakpoint")
    protected String highBreakpoint;
    @XmlElement(name = "Value", required = true)
    protected String value;


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


    public String getLowBreakpoint() {
        return this.lowBreakpoint;
    }


    public void setLowBreakpoint(final String lowBreakpoint) {
        this.lowBreakpoint = lowBreakpoint;
    }


    public String getHighBreakpoint() {
        return this.highBreakpoint;
    }


    public void setHighBreakpoint(final String highBreakpoint) {
        this.highBreakpoint = highBreakpoint;
    }


    public String getValue() {
        return this.value;
    }


    public void setValue(final String value) {
        this.value = value;
    }
}
