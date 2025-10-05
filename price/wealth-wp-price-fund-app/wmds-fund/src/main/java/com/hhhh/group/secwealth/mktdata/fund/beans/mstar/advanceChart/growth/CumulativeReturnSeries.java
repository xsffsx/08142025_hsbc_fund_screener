
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"historyDetail"})
@XmlRootElement(name = "CumulativeReturnSeries")
public class CumulativeReturnSeries {

    @XmlElement(name = "HistoryDetail", required = true)
    protected List<HistoryDetail> historyDetail;

    
    public List<HistoryDetail> getHistoryDetail() {
        return this.historyDetail;
    }

    
    public void setHistoryDetail(final List<HistoryDetail> historyDetail) {
        this.historyDetail = historyDetail;
    }


}
