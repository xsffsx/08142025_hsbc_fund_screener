
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"lsMaximumFrontLoad", "lsFrontLoads", "lsMaximumRedemptionFee", "lsRedemptionFees",
    "lsMaximumManagementFee", "lsManagementFees", "lsDistributionFees", "lsCreationUnitDate", "lsProspectusCustodianFee",
    "atActualFrontLoad"})
@XmlRootElement(name = "api")
public class Api {

    @XmlElement(name = "LS-MaximumFrontLoad", required = true)
    protected String lsMaximumFrontLoad;

    @XmlElement(name = "LS-FrontLoads", required = true)
    protected LSFrontLoads lsFrontLoads;

    @XmlElement(name = "LS-MaximumRedemptionFee", required = true)
    protected String lsMaximumRedemptionFee;

    @XmlElement(name = "LS-RedemptionFees", required = true)
    protected LSRedemptionFees lsRedemptionFees;

    @XmlElement(name = "LS-MaximumManagementFee", required = true)
    protected String lsMaximumManagementFee;

    @XmlElement(name = "LS-ManagementFees", required = true)
    protected LSManagementFees lsManagementFees;

    @XmlElement(name = "LS-DistributionFees", required = true)
    protected LSDistributionFees lsDistributionFees;

    @XmlElement(name = "LS-CreationUnitDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected String lsCreationUnitDate;

    @XmlElement(name = "LS-ProspectusCustodianFee", required = true)
    protected LSProspectusCustodianFee lsProspectusCustodianFee;

    @XmlElement(name = "AT-ActualFrontLoad")
    protected String atActualFrontLoad;

    @XmlAttribute(name = "_id")
    protected String id;

    
    public String getLsMaximumFrontLoad() {
        return this.lsMaximumFrontLoad;
    }

    
    public void setLsMaximumFrontLoad(final String lsMaximumFrontLoad) {
        this.lsMaximumFrontLoad = lsMaximumFrontLoad;
    }

    
    public LSFrontLoads getLsFrontLoads() {
        return this.lsFrontLoads;
    }

    
    public void setLsFrontLoads(final LSFrontLoads lsFrontLoads) {
        this.lsFrontLoads = lsFrontLoads;
    }

    
    public String getLsMaximumRedemptionFee() {
        return this.lsMaximumRedemptionFee;
    }

    
    public void setLsMaximumRedemptionFee(final String lsMaximumRedemptionFee) {
        this.lsMaximumRedemptionFee = lsMaximumRedemptionFee;
    }

    
    public LSRedemptionFees getLsRedemptionFees() {
        return this.lsRedemptionFees;
    }

    
    public void setLsRedemptionFees(final LSRedemptionFees lsRedemptionFees) {
        this.lsRedemptionFees = lsRedemptionFees;
    }

    
    public String getLsMaximumManagementFee() {
        return this.lsMaximumManagementFee;
    }

    
    public void setLsMaximumManagementFee(final String lsMaximumManagementFee) {
        this.lsMaximumManagementFee = lsMaximumManagementFee;
    }

    
    public LSManagementFees getLsManagementFees() {
        return this.lsManagementFees;
    }

    
    public void setLsManagementFees(final LSManagementFees lsManagementFees) {
        this.lsManagementFees = lsManagementFees;
    }

    
    public LSDistributionFees getLsDistributionFees() {
        return this.lsDistributionFees;
    }

    
    public void setLsDistributionFees(final LSDistributionFees lsDistributionFees) {
        this.lsDistributionFees = lsDistributionFees;
    }

    
    public String getLsCreationUnitDate() {
        return this.lsCreationUnitDate;
    }

    
    public void setLsCreationUnitDate(final String lsCreationUnitDate) {
        this.lsCreationUnitDate = lsCreationUnitDate;
    }

    
    public LSProspectusCustodianFee getLsProspectusCustodianFee() {
        return this.lsProspectusCustodianFee;
    }

    
    public void setLsProspectusCustodianFee(final LSProspectusCustodianFee lsProspectusCustodianFee) {
        this.lsProspectusCustodianFee = lsProspectusCustodianFee;
    }

    
    public String getAtActualFrontLoad() {
        return this.atActualFrontLoad;
    }

    
    public void setAtActualFrontLoad(final String atActualFrontLoad) {
        this.atActualFrontLoad = atActualFrontLoad;
    }

    
    public String getId() {
        return this.id;
    }

    
    public void setId(final String id) {
        this.id = id;
    }
}
