
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"cumulativeReturnSeries"})
@XmlRootElement(name = "Security")
public class Security {

    @XmlAttribute(name = "Id")
    protected String id;

    @XmlElement(name = "CumulativeReturnSeries", required = true)
    protected List<CumulativeReturnSeries> cumulativeReturnSeries;


    public String getId() {
        return this.id;
    }


    public void setId(final String id) {
        this.id = id;
    }


    public List<CumulativeReturnSeries> getCumulativeReturnSeries() {
        return this.cumulativeReturnSeries;
    }


    public void setCumulativeReturnSeries(final List<CumulativeReturnSeries> cumulativeReturnSeries) {
        this.cumulativeReturnSeries = cumulativeReturnSeries;
    }


}
