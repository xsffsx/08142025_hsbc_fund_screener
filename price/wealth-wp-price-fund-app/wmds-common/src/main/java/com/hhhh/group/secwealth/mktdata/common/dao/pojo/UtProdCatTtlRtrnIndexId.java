
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UtProdCatTtlRtrnIndexId implements Serializable {

    private static final long serialVersionUID = 8914902808334021639L;

    @Column(nullable = false, name = "JOB_BATCH_EXEC_ID")
    private Long batchId;

    @Column(nullable = false, name = "PROD_CAT_CDE")
    private String hhhhCategoryCode;

    @Column(nullable = false, name = "FREQ_TTL_RTRN_INDEX_TEXT")
    private String frequency;

    @Column(nullable = false, name = "END_DT", columnDefinition = "Date")
    private Date endDate;

    
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

    
    public String getFrequency() {
        return this.frequency;
    }

    
    public void setFrequency(final String frequency) {
        this.frequency = frequency;
    }

    
    public Date getEndDate() {
        return this.endDate;
    }

    
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

}
