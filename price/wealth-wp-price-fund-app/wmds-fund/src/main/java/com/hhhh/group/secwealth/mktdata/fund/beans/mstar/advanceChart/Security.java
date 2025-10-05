
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"historyDetail"})
@XmlRootElement(name = "Security")
public class Security {


    @XmlAttribute(name = "Id")
    protected String id;

    @XmlElement(name = "HistoryDetail", required = true)
    protected List<HistoryDetail> historyDetail;


    public String getId() {
        return this.id;
    }


    public void setId(final String id) {
        this.id = id;
    }


    public List<HistoryDetail> getHistoryDetail() {
        return this.historyDetail;
    }


    public void setHistoryDetail(final List<HistoryDetail> historyDetail) {
        this.historyDetail = historyDetail;
    }


}
