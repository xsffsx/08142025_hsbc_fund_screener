
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UTProdSwitchoutGroupId implements Serializable {

    private static final long serialVersionUID = 1044211927454374661L;

    @Column(name = "PROD_ID")
    private Integer productId;

    @Column(name = "JOB_BATCH_EXEC_ID")
    private long batchId;

    @Column(name = "SWITCH_TABLE_GROUP")
    private String switchTableGroup;

    
    public Integer getProductId() {
        return this.productId;
    }

    
    public void setProductId(final Integer productId) {
        this.productId = productId;
    }

    
    public long getBatchId() {
        return this.batchId;
    }

    
    public void setBatchId(final long batchId) {
        this.batchId = batchId;
    }

    
    public String getSwitchTableGroup() {
        return this.switchTableGroup;
    }

    
    public void setSwitchTableGroup(final String switchTableGroup) {
        this.switchTableGroup = switchTableGroup;
    }

}
