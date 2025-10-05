
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UtProdCatPerfmRtrnId implements Serializable {

    private static final long serialVersionUID = 8774930349709861076L;

    @Column(nullable = false, name = "JOB_BATCH_EXEC_ID")
    private Long batchId;

    @Column(nullable = false, name = "PROD_CAT_CDE")
    private String hhhhCategoryCode;

    @Column(nullable = false, name = "RTRN_AMT_TYPE_CDE")
    private String rtrnAmtTtpeCde;

    
    public Long getBatchId() {
        return this.batchId;
    }

    
    public void setBatchId(final Long batchId) {
        this.batchId = batchId;
    }

    
    public String gethhhhCategoryCode() {
        return this.hhhhCategoryCode;
    }

    
    public void sethhhhCategoryCode(final String hhhhCategoryCode) {
        this.hhhhCategoryCode = hhhhCategoryCode;
    }

    
    public String getRtrnAmtTtpeCde() {
        return this.rtrnAmtTtpeCde;
    }

    
    public void setRtrnAmtTtpeCde(final String rtrnAmtTtpeCde) {
        this.rtrnAmtTtpeCde = rtrnAmtTtpeCde;
    }

}
