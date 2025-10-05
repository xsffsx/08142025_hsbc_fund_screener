
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "V_UT_HLDG")
public class UtHoldings implements Serializable {

    private static final long serialVersionUID = 9205586856226777135L;

    @EmbeddedId
    private UtHoldingsId utHoldingsId;

    @Column(nullable = true, name = "WGHT_HLDG_PCT")
    private BigDecimal weight;

    @Column(nullable = false, name = "USER_UPDT_NUM")
    private String updatedBy;

    @Column(nullable = false, name = "REC_UPDT_DT_TM")
    private Date updatedOn;

    
    public UtHoldingsId getUtHoldingsId() {
        return this.utHoldingsId;
    }

    
    public void setUtHoldingsId(final UtHoldingsId utHoldingsId) {
        this.utHoldingsId = utHoldingsId;
    }

    
    public BigDecimal getWeight() {
        return this.weight;
    }

    
    public void setWeight(final BigDecimal weight) {
        this.weight = weight;
    }

    
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    
    public Date getUpdatedOn() {
        return this.updatedOn;
    }

    
    public void setUpdatedOn(final Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
