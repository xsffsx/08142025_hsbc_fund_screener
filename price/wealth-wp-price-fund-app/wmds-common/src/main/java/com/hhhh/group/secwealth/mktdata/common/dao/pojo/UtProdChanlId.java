
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UtProdChanlId implements Serializable {

    private static final long serialVersionUID = 1044211927454374661L;

    @Column(name = "PROD_ID")
    private int prodId;

    @Column(name = "JOB_BATCH_EXEC_ID")
    private long batchId;

    
    public int getProdId() {
        return this.prodId;
    }

    
    public void setProdId(final int prodId) {
        this.prodId = prodId;
    }

    
    public long getBatchId() {
        return this.batchId;
    }

    
    public void setBatchId(final long batchId) {
        this.batchId = batchId;
    }

}
