
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "V_UT_RETURNS")
public class UtReturns implements Serializable {

    private static final long serialVersionUID = -4834093294124841453L;

    @EmbeddedId
    private UtReturnsId utReturnsId;

    @Column(nullable = true, name = "RTRN_AMT")
    private BigDecimal rtrnAmt;

    @Column(nullable = false, name = "REC_UPDT_DT_TM")
    private Date updatedOn;

    
    public UtReturnsId getUtReturnsId() {
        return this.utReturnsId;
    }

    
    public void setUtReturnsId(final UtReturnsId utReturnsId) {
        this.utReturnsId = utReturnsId;
    }

    
    public BigDecimal getRtrnAmt() {
        return this.rtrnAmt;
    }

    
    public void setRtrnAmt(final BigDecimal rtrnAmt) {
        this.rtrnAmt = rtrnAmt;
    }

    
    public Date getUpdatedOn() {
        return this.updatedOn;
    }

    
    public void setUpdatedOn(final Date updatedOn) {
        this.updatedOn = updatedOn;
    }

}
