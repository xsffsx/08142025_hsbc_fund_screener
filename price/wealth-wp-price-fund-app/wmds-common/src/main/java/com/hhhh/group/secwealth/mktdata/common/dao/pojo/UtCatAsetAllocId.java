
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UtCatAsetAllocId implements Serializable {

    private static final long serialVersionUID = 4802912306346336743L;

    @Column(nullable = false, name = "JOB_BATCH_EXEC_ID")
    private Long batchId;

    @Column(nullable = false, name = "PROD_ID")
    private Integer productId;

    @Column(nullable = false, name = "ALLOC_CLASS_TYPE_CDE")
    private String classTypeCode;

    @Column(nullable = false, name = "ASET_ALLOC_CLASS_CDE")
    private String assetClsCde;

    
    public Long getBatchId() {
        return this.batchId;
    }

    
    public void setBatchId(final Long batchId) {
        this.batchId = batchId;
    }

    
    public Integer getProductId() {
        return this.productId;
    }

    
    public void setProductId(final Integer productId) {
        this.productId = productId;
    }

    
    public String getClassTypeCode() {
        return this.classTypeCode;
    }

    
    public void setClassTypeCode(final String classTypeCode) {
        this.classTypeCode = classTypeCode;
    }

    
    public String getAssetClsCde() {
        return this.assetClsCde;
    }

    
    public void setAssetClsCde(final String assetClsCde) {
        this.assetClsCde = assetClsCde;
    }

}
