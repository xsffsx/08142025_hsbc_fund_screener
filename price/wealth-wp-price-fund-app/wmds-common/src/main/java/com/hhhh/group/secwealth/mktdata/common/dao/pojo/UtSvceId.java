
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UtSvceId implements Serializable {

    private static final long serialVersionUID = -3440845795754561111L;

    @Column(nullable = false, name = "JOB_BATCH_EXEC_ID")
    private long batchId;

    @Column(nullable = false, name = "PERFORMANCE_ID")
    private String performanceId;

    @Column(nullable = false, name = "FUND_SVCE_ID")
    private String fundSvcCde;

    
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

    
    public String getFundSvcCde() {
        return this.fundSvcCde;
    }

    
    public void setFundSvcCde(final String fundSvcCde) {
        this.fundSvcCde = fundSvcCde;
    }
}
