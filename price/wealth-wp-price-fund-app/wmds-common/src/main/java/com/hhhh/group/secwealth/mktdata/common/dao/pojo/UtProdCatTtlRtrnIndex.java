
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "V_UT_PROD_CAT_TTL_RTRN_INDEX")
public class UtProdCatTtlRtrnIndex implements Serializable {

    private static final long serialVersionUID = 8265973103794918277L;

    @EmbeddedId
    private UtProdCatTtlRtrnIndexId utProdCatTtlRtrnIndexId;

    @Column(nullable = false, name = "PROD_SUBTP_CDE")
    private String prodSbtypeCd;

    @Column(nullable = false, name = "PROD_TYPE_CDE")
    private String productTypeCode;

    @Column(nullable = false, name = "CAT_NAV_PRC_AMT")
    private BigDecimal value;

    @Column(nullable = false, name = "REC_UPDT_DT_TM")
    private Timestamp updatedOn;

    @Transient
    private BigDecimal change;

    
    public UtProdCatTtlRtrnIndexId getUtProdCatTtlRtrnIndexId() {
        return this.utProdCatTtlRtrnIndexId;
    }

    
    public void setUtProdCatTtlRtrnIndexId(final UtProdCatTtlRtrnIndexId utProdCatTtlRtrnIndexId) {
        this.utProdCatTtlRtrnIndexId = utProdCatTtlRtrnIndexId;
    }

    
    public String getProdSbtypeCd() {
        return this.prodSbtypeCd;
    }

    
    public void setProdSbtypeCd(final String prodSbtypeCd) {
        this.prodSbtypeCd = prodSbtypeCd;
    }

    
    public String getProductTypeCode() {
        return this.productTypeCode;
    }

    
    public void setProductTypeCode(final String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    
    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    
    public Timestamp getUpdatedOn() {
        return this.updatedOn;
    }

    
    public void setUpdatedOn(final Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    
    public BigDecimal getChange() {
        return this.change;
    }

    
    public void setChange(final BigDecimal change) {
        this.change = change;
    }

}
