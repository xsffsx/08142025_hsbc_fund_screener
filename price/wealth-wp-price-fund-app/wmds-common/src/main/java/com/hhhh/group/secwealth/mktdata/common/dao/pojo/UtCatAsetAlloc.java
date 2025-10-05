
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;




@Entity
@Table(name = "V_UT_CAT_ASET_ALLOC")
public class UtCatAsetAlloc implements Serializable {

    private static final long serialVersionUID = 978517570900164315L;

    @EmbeddedId
    private UtCatAsetAllocId utCatAsetAllocId;

    @Column(nullable = false, name = "FUND_ALLOC_WGHT")
    private BigDecimal fundAllocation;

    @Column(nullable = false, name = "PROD_NLS_NAME1")
    private String prodnName1;

    @Column(nullable = false, name = "PROD_NLS_NAME2")
    private String prodnName2;

    @Column(nullable = false, name = "PROD_NLS_NAME3")
    private String prodnName3;

    @Column(nullable = false, name = "FUND_ALLOC_WGHT_IND")
    private String isFundShortPosition;

    @Column(nullable = false, name = "ALLOC_CAT_AVE")
    private BigDecimal categoryAllocation;

    @Column(nullable = false, name = "ALLOC_CAT_AVE_IND")
    private String isCategoryShortPosition;

    @Column(nullable = false, name = "REC_UPDT_DT_TM")
    private Date updatedOn;

    @Column(nullable = false, name = "USER_UPDT_NUM")
    private String updatedBy;

    
    public UtCatAsetAllocId getUtCatAsetAllocId() {
        return this.utCatAsetAllocId;
    }

    
    public void setUtCatAsetAllocId(final UtCatAsetAllocId utCatAsetAllocId) {
        this.utCatAsetAllocId = utCatAsetAllocId;
    }

    
    public BigDecimal getFundAllocation() {
        return this.fundAllocation;
    }

    
    public void setFundAllocation(final BigDecimal fundAllocation) {
        this.fundAllocation = fundAllocation;
    }

    
    public String getProdnName1() {
        return this.prodnName1;
    }

    
    public void setProdnName1(final String prodnName1) {
        this.prodnName1 = prodnName1;
    }

    
    public String getProdnName2() {
        return this.prodnName2;
    }

    
    public void setProdnName2(final String prodnName2) {
        this.prodnName2 = prodnName2;
    }

    
    public String getProdnName3() {
        return this.prodnName3;
    }

    
    public void setProdnName3(final String prodnName3) {
        this.prodnName3 = prodnName3;
    }

    
    public String getIsFundShortPosition() {
        return this.isFundShortPosition;
    }

    
    public void setIsFundShortPosition(final String isFundShortPosition) {
        this.isFundShortPosition = isFundShortPosition;
    }

    
    public BigDecimal getCategoryAllocation() {
        return this.categoryAllocation;
    }

    
    public void setCategoryAllocation(final BigDecimal categoryAllocation) {
        this.categoryAllocation = categoryAllocation;
    }

    
    public String getIsCategoryShortPosition() {
        return this.isCategoryShortPosition;
    }

    
    public void setIsCategoryShortPosition(final String isCategoryShortPosition) {
        this.isCategoryShortPosition = isCategoryShortPosition;
    }

    
    public Date getUpdatedOn() {
        return this.updatedOn;
    }

    
    public void setUpdatedOn(final Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
