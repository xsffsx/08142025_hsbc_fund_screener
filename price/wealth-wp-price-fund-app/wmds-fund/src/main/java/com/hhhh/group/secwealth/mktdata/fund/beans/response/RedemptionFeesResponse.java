
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.math.BigDecimal;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesDistributionFee;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesFrontLoad;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesItem;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesManagementFee;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.redemptionfees.RedemptionFeesProspectusCustodianFee;

public class RedemptionFeesResponse {

    private BigDecimal maximumFrontLoad;
    private List<RedemptionFeesFrontLoad> frontLoads;
    private BigDecimal maximumRedemptionFee;
    private List<RedemptionFeesItem> redemptionFees;
    private BigDecimal maximumManagementFee;
    private List<RedemptionFeesManagementFee> managementFees;
    private List<RedemptionFeesDistributionFee> distributionFees;
    private String creationUnitDate;
    private List<RedemptionFeesProspectusCustodianFee> prospectusCustodianFees;
    private BigDecimal actualFrontLoad;

    
    public BigDecimal getMaximumFrontLoad() {
        return this.maximumFrontLoad;
    }

    
    public void setMaximumFrontLoad(final BigDecimal maximumFrontLoad) {
        this.maximumFrontLoad = maximumFrontLoad;
    }

    
    public List<RedemptionFeesFrontLoad> getFrontLoads() {
        return this.frontLoads;
    }

    
    public void setFrontLoads(final List<RedemptionFeesFrontLoad> frontLoads) {
        this.frontLoads = frontLoads;
    }

    
    public BigDecimal getMaximumRedemptionFee() {
        return this.maximumRedemptionFee;
    }

    
    public void setMaximumRedemptionFee(final BigDecimal maximumRedemptionFee) {
        this.maximumRedemptionFee = maximumRedemptionFee;
    }

    
    public List<RedemptionFeesItem> getRedemptionFees() {
        return this.redemptionFees;
    }

    
    public void setRedemptionFees(final List<RedemptionFeesItem> redemptionFees) {
        this.redemptionFees = redemptionFees;
    }

    
    public BigDecimal getMaximumManagementFee() {
        return this.maximumManagementFee;
    }

    
    public void setMaximumManagementFee(final BigDecimal maximumManagementFee) {
        this.maximumManagementFee = maximumManagementFee;
    }

    
    public List<RedemptionFeesManagementFee> getManagementFees() {
        return this.managementFees;
    }

    
    public void setManagementFees(final List<RedemptionFeesManagementFee> managementFees) {
        this.managementFees = managementFees;
    }

    
    public List<RedemptionFeesDistributionFee> getDistributionFees() {
        return this.distributionFees;
    }

    
    public void setDistributionFees(final List<RedemptionFeesDistributionFee> distributionFees) {
        this.distributionFees = distributionFees;
    }

    
    public String getCreationUnitDate() {
        return this.creationUnitDate;
    }

    
    public void setCreationUnitDate(final String creationUnitDate) {
        this.creationUnitDate = creationUnitDate;
    }

    
    public List<RedemptionFeesProspectusCustodianFee> getProspectusCustodianFees() {
        return this.prospectusCustodianFees;
    }

    
    public void setProspectusCustodianFees(final List<RedemptionFeesProspectusCustodianFee> prospectusCustodianFees) {
        this.prospectusCustodianFees = prospectusCustodianFees;
    }

    
    public BigDecimal getActualFrontLoad() {
        return this.actualFrontLoad;
    }

    
    public void setActualFrontLoad(final BigDecimal actualFrontLoad) {
        this.actualFrontLoad = actualFrontLoad;
    }
}
