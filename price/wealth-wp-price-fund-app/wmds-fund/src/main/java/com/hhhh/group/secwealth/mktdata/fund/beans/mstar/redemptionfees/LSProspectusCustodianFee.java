
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"feeSchedule"})
@XmlRootElement(name = "LS-ProspectusCustodianFee")
public class LSProspectusCustodianFee {

    @XmlElement(name = "FeeSchedule", required = true)
    protected List<FeeSchedule> feeSchedule;


    public List<FeeSchedule> getFeeSchedule() {
        return this.feeSchedule;
    }


    public void setFeeSchedule(final List<FeeSchedule> feeSchedule) {
        this.feeSchedule = feeSchedule;
    }


}
