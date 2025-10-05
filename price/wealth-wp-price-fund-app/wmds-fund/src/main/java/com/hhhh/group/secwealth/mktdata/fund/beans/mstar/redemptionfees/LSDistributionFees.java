
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"distributionFee"})
@XmlRootElement(name = "LS-DistributionFees")
public class LSDistributionFees {

    @XmlElement(name = "DistributionFee", required = true)
    protected List<DistributionFee> distributionFee;


    public List<DistributionFee> getDistributionFee() {
        return this.distributionFee;
    }


    public void setDistributionFee(final List<DistributionFee> distributionFee) {
        this.distributionFee = distributionFee;
    }
}
