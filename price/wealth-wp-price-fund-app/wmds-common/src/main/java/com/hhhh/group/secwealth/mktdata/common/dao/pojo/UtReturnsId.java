
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UtReturnsId implements Serializable {

    private static final long serialVersionUID = -2524845849535407110L;

    @Column(nullable = false, name = "JOB_BATCH_EXEC_ID")
    private long batchId;

    @Column(nullable = false, name = "PERFORMANCE_ID")
    private String performanceId;

    @Column(nullable = false, name = "FUND_RETURN_TYPE_CODE")
    private String fundReturnTypeCode;

    
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

    
    public String getFundReturnTypeCode() {
        return this.fundReturnTypeCode;
    }

    
    public void setFundReturnTypeCode(final String fundReturnTypeCode) {
        this.fundReturnTypeCode = fundReturnTypeCode;
    }
}
