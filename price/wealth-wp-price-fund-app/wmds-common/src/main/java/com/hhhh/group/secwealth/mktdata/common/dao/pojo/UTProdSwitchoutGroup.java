
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "v_UT_PROD_SW_GROUP")
public class UTProdSwitchoutGroup {

    @EmbeddedId
    private UTProdSwitchoutGroupId utProdSwitchoutGroupId;

    @Column(name = "REC_UPDT_DT_TM")
    private Date updaetdOn;

    @Column(name = "USER_UPDT_NUM")
    private String updatedBy;


    
    public UTProdSwitchoutGroupId getUtProdSwitchoutGroupId() {
        return this.utProdSwitchoutGroupId;
    }

    
    public void setUtProdSwitchoutGroupId(final UTProdSwitchoutGroupId utProdSwitchoutGroupId) {
        this.utProdSwitchoutGroupId = utProdSwitchoutGroupId;
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
