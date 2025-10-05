
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "V_UT_PROD_SW")
public class UTProdSwitchout {

    @EmbeddedId
    private UTProdSwitchoutId utProdSwitchoutId;

    @Column(name = "REC_UPDT_DT_TM")
    private Date updaetdOn;

    @Column(name = "USER_UPDT_NUM")
    private String updatedBy;


    
    public UTProdSwitchoutId getUtProdSwitchoutId() {
        return this.utProdSwitchoutId;
    }

    
    public void setUtProdSwitchoutId(final UTProdSwitchoutId utProdSwitchoutId) {
        this.utProdSwitchoutId = utProdSwitchoutId;
    }

    
    public Date getUpdaetdOn() {
        return this.updaetdOn;
    }

    
    public void setUpdaetdOn(final Date updaetdOn) {
        this.updaetdOn = updaetdOn;
    }

    
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
