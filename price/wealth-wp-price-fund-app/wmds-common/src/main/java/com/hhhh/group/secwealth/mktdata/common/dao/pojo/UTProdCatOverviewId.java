
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;


public class UTProdCatOverviewId implements Serializable {

    private static final long serialVersionUID = -3451981504915982925L;

    @Column(name = "JOB_BATCH_EXEC_ID")
    private long batchId;

    @Column(nullable = false, name = "PROD_ID")
    private Integer productId;


    
    public long getBatchId() {
        return this.batchId;
    }

    
    public void setBatchId(final long batchId) {
        this.batchId = batchId;
    }

    
    public Integer getProductId() {
        return this.productId;
    }

    
    public void setProductId(final Integer productId) {
        this.productId = productId;
    }

}
