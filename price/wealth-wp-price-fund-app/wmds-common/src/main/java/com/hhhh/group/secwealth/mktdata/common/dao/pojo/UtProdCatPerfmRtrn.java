
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "V_UT_PROD_CAT_PERFM_RTRN")
public class UtProdCatPerfmRtrn implements Serializable {

    private static final long serialVersionUID = -1633850340842423301L;

    @EmbeddedId
    private UtProdCatPerfmRtrnId utProdCatPerfmRtrnId;

    @Column(nullable = false, name = "RTRN_AMT")
    private BigDecimal trailingTotalReturn;

    @Column(nullable = false, name = "END_DT", columnDefinition = "Date")
    private Date endDate;

    @Column(nullable = false, name = "PROD_SUBTP_CDE")
    private String prodSubtypeCode;

    @Column(nullable = true, name = "PROD_TYPE_CDE")
    private String productType;

    @Column(nullable = false, name = "REC_UPDT_DT_TM")
    private Timestamp updatedOn;

    
    public UtProdCatPerfmRtrnId getUtProdCatPerfmRtrnId() {
        return this.utProdCatPerfmRtrnId;
    }

    
    public void setUtProdCatPerfmRtrnId(final UtProdCatPerfmRtrnId utProdCatPerfmRtrnId) {
        this.utProdCatPerfmRtrnId = utProdCatPerfmRtrnId;
    }

    
    public BigDecimal getTrailingTotalReturn() {
        return this.trailingTotalReturn;
    }

    
    public void setTrailingTotalReturn(final BigDecimal trailingTotalReturn) {
        this.trailingTotalReturn = trailingTotalReturn;
    }

    
    public Date getEndDate() {
        return this.endDate;
    }

    
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    
    public String getProdSubtypeCode() {
        return this.prodSubtypeCode;
    }

    
    public void setProdSubtypeCode(final String prodSubtypeCode) {
        this.prodSubtypeCode = prodSubtypeCode;
    }

    
    public String getProductType() {
        return this.productType;
    }

    
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    
    public Timestamp getUpdatedOn() {
        return this.updatedOn;
    }

    
    public void setUpdatedOn(final Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

}
