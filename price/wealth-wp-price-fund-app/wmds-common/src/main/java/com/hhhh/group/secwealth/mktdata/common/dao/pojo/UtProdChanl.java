
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "V_UT_PROD_CHANL")
public class UtProdChanl {

    @EmbeddedId
    private UtProdChanlId utProdChanlId;

    @Column(name = "CTRY_REC_CDE")
    private String countryCode;

    @Column(name = "GRP_MEMBR_REC_CDE")
    private String groupMember;

    @Column(name = "CHANL_COMN_CDE")
    private String channelComnCde;

    @Column(name = "CHANL_CDE")
    private String channelCde;

    @Column(name = "ALLOW_BUY_PROD_IND", columnDefinition = "char")
    private String allowBuyProdInd;

    @Column(name = "ALLOW_SELL_PROD_IND", columnDefinition = "char")
    private String allowSellProdInd;

    @Column(name = "ALLOW_SW_OUT_PROD_IND", columnDefinition = "char")
    private String allowSwitchOut;

    @Column(name = "ALLOW_SW_IN_PROD_IND", columnDefinition = "char")
    private String allowSwitchIn;

    @Column(name = "REC_UPDT_DT_TM")
    private Date updaetdOn;

    @Column(name = "USER_UPDT_NUM")
    private String updatedBy;

    public String getAllowSwitchOut() {
        return allowSwitchOut;
    }

    public void setAllowSwitchOut(String allowSwitchOut) {
        this.allowSwitchOut = allowSwitchOut;
    }

    public String getAllowSwitchIn() {
        return allowSwitchIn;
    }

    public void setAllowSwitchIn(String allowSwitchIn) {
        this.allowSwitchIn = allowSwitchIn;
    }

    /**
     * @return the utProdChanlId
     */
    public UtProdChanlId getUtProdChanlId() {
        return this.utProdChanlId;
    }

    
    public void setUtProdChanlId(final UtProdChanlId utProdChanlId) {
        this.utProdChanlId = utProdChanlId;
    }

    
    public String getCountryCode() {
        return this.countryCode;
    }

    
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    
    public String getGroupMember() {
        return this.groupMember;
    }

    
    public void setGroupMember(final String groupMember) {
        this.groupMember = groupMember;
    }

    
    public String getChannelComnCde() {
        return this.channelComnCde;
    }

    
    public void setChannelComnCde(final String channelComnCde) {
        this.channelComnCde = channelComnCde;
    }

    
    public String getChannelCde() {
        return this.channelCde;
    }

    
    public void setChannelCde(final String channelCde) {
        this.channelCde = channelCde;
    }

    
    public String getAllowBuyProdInd() {
        return this.allowBuyProdInd;
    }

    
    public void setAllowBuyProdInd(final String allowBuyProdInd) {
        this.allowBuyProdInd = allowBuyProdInd;
    }

    
    public String getAllowSellProdInd() {
        return this.allowSellProdInd;
    }

    
    public void setAllowSellProdInd(final String allowSellProdInd) {
        this.allowSellProdInd = allowSellProdInd;
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
