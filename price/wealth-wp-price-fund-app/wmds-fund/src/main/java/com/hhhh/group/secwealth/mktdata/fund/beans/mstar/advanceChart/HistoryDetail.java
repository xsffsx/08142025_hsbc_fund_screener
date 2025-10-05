
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"endDate", "value", "originalDate"})
@XmlRootElement(name = "HistoryDetail")
public class HistoryDetail {

    @XmlElement(required = true, name = "EndDate")
    protected String endDate;
    @XmlElement(required = true, name = "Value")
    protected String value;
    @XmlElement(required = true, name = "OriginalDate")
    protected String originalDate;

    
    public String getEndDate() {
        return this.endDate;
    }

    
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    
    public String getValue() {
        return this.value;
    }

    
    public void setValue(final String value) {
        this.value = value;
    }

    
    public String getOriginalDate() {
        return this.originalDate;
    }

    
    public void setOriginalDate(final String originalDate) {
        this.originalDate = originalDate;
    }


}
