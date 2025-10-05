
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;




@Entity
@Table(name = "V_UT_HLDG_ALLOC")
public class UTHoldingAlloc implements Serializable {

    private static final long serialVersionUID = 3612360837081138819L;

    @EmbeddedId
    private UTHoldingAllocId utHoldingAllocId;

    @Column(nullable = true, name = "HLDG_ALLOC_WGHT")
    private BigDecimal holdingAllocWeight;

    @Column(nullable = true, name = "HLDG_ALLOC_WGHT_NET")
    private BigDecimal holdingAllocWeightNet;

    @Column(nullable = true, name = "PORTF_DATE")
    private Date portfolioDate;

    @Column(nullable = false, name = "USER_UPDT_NUM")
    private String updatedBy;

    @Column(nullable = false, name = "REC_UPDT_DT_TM", columnDefinition = "Date")
    private Date updatedOn;

    
    public UTHoldingAllocId getUtHoldingAllocId() {
        return this.utHoldingAllocId;
    }

    
    public void setUtHoldingAllocId(final UTHoldingAllocId utHoldingAllocId) {
        this.utHoldingAllocId = utHoldingAllocId;
    }

    
    public BigDecimal getHoldingAllocWeight() {
        return this.holdingAllocWeight;
    }

    
    public void setHoldingAllocWeight(final BigDecimal holdingAllocWeight) {
        this.holdingAllocWeight = holdingAllocWeight;
    }

    
    public BigDecimal getHoldingAllocWeightNet() {
        return this.holdingAllocWeightNet;
    }

    
    public void setHoldingAllocWeightNet(final BigDecimal holdingAllocWeightNet) {
        this.holdingAllocWeightNet = holdingAllocWeightNet;
    }

    
    public Date getPortfolioDate() {
        return this.portfolioDate;
    }

    
    public void setPortfolioDate(final Date portfolioDate) {
        this.portfolioDate = portfolioDate;
    }

    
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    
    public Date getUpdatedOn() {
        return this.updatedOn;
    }

    
    public void setUpdatedOn(final Date updatedOn) {
        this.updatedOn = updatedOn;
    }

}