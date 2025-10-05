
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"security"})
@XmlRootElement(name = "TimeSeries")
public class AdvanceChartGrowthData extends ResponseData {

    @XmlElement(name = "Security", required = true)
    protected List<Security> security;


    public List<Security> getSecurity() {
        return this.security;
    }


    public void setSecurity(final List<Security> security) {
        this.security = security;
    }


}
