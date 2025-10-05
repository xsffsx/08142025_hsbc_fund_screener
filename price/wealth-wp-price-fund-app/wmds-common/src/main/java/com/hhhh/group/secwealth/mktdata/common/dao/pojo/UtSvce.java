
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "V_UT_SVCE")
public class UtSvce implements Serializable {

    private static final long serialVersionUID = 2806001170474564015L;

    @EmbeddedId
    private UtSvceId utSvceId;

    @Column(nullable = true, name = "FUND_SVCE_CLASS_TYPE_CDE")
    private String fundSvcClzTypeCd;

    @Column(nullable = true, name = "CTRY_FUND_SVCE_NAME")
    private String country;

    @Column(nullable = false, name = "USER_UPDT_NUM")
    private String updatedBy;

    @Column(nullable = false, name = "REC_UPDT_DT_TM")
    private Date updatedOn;

    @Column(nullable = false, name = "SER_NUM")
    private Integer holdingId;

    
    public UtSvceId getUtSvceId() {
        return this.utSvceId;
    }

    
    public void setUtSvceId(final UtSvceId utSvceId) {
        this.utSvceId = utSvceId;
    }

    
    public String getFundSvcClzTypeCd() {
        return this.fundSvcClzTypeCd;
    }

    
    public void setFundSvcClzTypeCd(final String fundSvcClzTypeCd) {
        this.fundSvcClzTypeCd = fundSvcClzTypeCd;
    }

    
    public String getCountry() {
        return this.country;
    }

    
    public void setCountry(final String country) {
        this.country = country;
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

    
    public Integer getHoldingId() {
        return this.holdingId;
    }

    
    public void setHoldingId(final Integer holdingId) {
        this.holdingId = holdingId;
    }

}
