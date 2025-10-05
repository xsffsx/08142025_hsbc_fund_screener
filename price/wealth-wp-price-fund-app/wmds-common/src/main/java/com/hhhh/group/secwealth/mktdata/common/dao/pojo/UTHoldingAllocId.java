
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;


public class UTHoldingAllocId implements Serializable {

    private static final long serialVersionUID = -3451981504915982925L;

    @Column(nullable = false, name = "JOB_BATCH_EXEC_ID")
    private long batchId;

    @Column(nullable = false, name = "PERFORMANCE_ID")
    private String performanceId;

    @Column(nullable = false, name = "HLDG_ALLOC_CLASS_NAME")
    private String holdingAllocClassName;

    @Column(nullable = true, name = "HLDG_ALLOC_CLASS_TYPE")
    private String holdingAllocClassType;

    
    public long getBatchId() {
        return this.batchId;
    }

    
    public void setBatchId(final long batchId) {
        this.batchId = batchId;
    }

    
    public String getPerformanceId() {
        return this.performanceId;
    }

    
    public void setPerformanceId(final String performanceId) {
        this.performanceId = performanceId;
    }

    
    public String getHoldingAllocClassName() {
        return this.holdingAllocClassName;
    }

    
    public void setHoldingAllocClassName(final String holdingAllocClassName) {
        this.holdingAllocClassName = holdingAllocClassName;
    }

    
    public String getHoldingAllocClassType() {
        return this.holdingAllocClassType;
    }

    
    public void setHoldingAllocClassType(final String holdingAllocClassType) {
        this.holdingAllocClassType = holdingAllocClassType;
    }

}
