
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"redemptionFee"})
@XmlRootElement(name = "LS-RedemptionFees")
public class LSRedemptionFees {

    @XmlElement(name = "RedemptionFee", required = true)
    protected List<RedemptionFee> redemptionFee;


    public List<RedemptionFee> getRedemptionFee() {
        return this.redemptionFee;
    }


    public void setRedemptionFee(final List<RedemptionFee> redemptionFee) {
        this.redemptionFee = redemptionFee;
    }
}
