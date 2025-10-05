
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"managementFee"})
@XmlRootElement(name = "LS-ManagementFees")
public class LSManagementFees {

    @XmlElement(name = "ManagementFee", required = true)
    protected List<ManagementFee> managementFee;


    public List<ManagementFee> getManagementFee() {
        return this.managementFee;
    }


    public void setManagementFee(final List<ManagementFee> managementFee) {
        this.managementFee = managementFee;
    }
}
