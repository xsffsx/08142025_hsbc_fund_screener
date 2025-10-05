
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UtProdCatId implements Serializable {

    private static final long serialVersionUID = 6751479490488197613L;

    @Column(nullable = false, name = "JOB_BATCH_EXEC_ID")
    private Long batchId;

    @Column(nullable = false, name = "PROD_ID")
    private Integer productId;

    @Column(nullable = false, name = "PROD_CAT_CDE")
    private String hhhhCategoryCode;

    
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

    
    public String gethhhhCategoryCode() {
        return this.hhhhCategoryCode;
    }

    
    public void sethhhhCategoryCode(final String hhhhCategoryCode) {
        this.hhhhCategoryCode = hhhhCategoryCode;
    }
}
