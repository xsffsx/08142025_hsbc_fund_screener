
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UtHoldingsId implements Serializable {

    private static final long serialVersionUID = 1197484523656240973L;

    @Column(nullable = false, name = "JOB_BATCH_EXEC_ID")
    private long batchId;

    @Column(nullable = false, name = "PERFORMANCE_ID")
    private String performanceId;

    @Column(nullable = false, name = "HLDG_NAME")
    private String holderName;

    @Column(nullable = false, name = "SER_NUM")
    private Integer holdingId;

    
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

    
    public String getHolderName() {
        return this.holderName;
    }

    
    public void setHolderName(final String holderName) {
        this.holderName = holderName;
    }

    
    public Integer getHoldingId() {
        return this.holdingId;
    }

    
    public void setHoldingId(final Integer holdingId) {
        this.holdingId = holdingId;
    }
}
