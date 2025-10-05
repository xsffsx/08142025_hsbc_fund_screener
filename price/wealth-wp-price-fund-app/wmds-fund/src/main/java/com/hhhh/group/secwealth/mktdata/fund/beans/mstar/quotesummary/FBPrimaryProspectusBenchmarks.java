
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"primaryProspectusBenchmark"})
@XmlRootElement(name = "FB-PrimaryProspectusBenchmarks")
public class FBPrimaryProspectusBenchmarks {

    @XmlElement(name = "PrimaryProspectusBenchmark", required = true)
    protected List<PrimaryProspectusBenchmark> primaryProspectusBenchmark;


    public List<PrimaryProspectusBenchmark> getPrimaryProspectusBenchmark() {
        return this.primaryProspectusBenchmark;
    }


    public void setPrimaryProspectusBenchmark(final List<PrimaryProspectusBenchmark> primaryProspectusBenchmark) {
        this.primaryProspectusBenchmark = primaryProspectusBenchmark;
    }
}
