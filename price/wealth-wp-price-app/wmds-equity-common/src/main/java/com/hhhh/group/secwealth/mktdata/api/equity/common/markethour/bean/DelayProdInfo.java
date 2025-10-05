/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Setter
@Getter
@Entity
public class DelayProdInfo implements Serializable {

    private static final long serialVersionUID = -7499275610547313634L;
    @Id
    @GeneratedValue()
    @Column(name = "DELAY_PROD_ID")
    private Long delayProdId;
    @Column(name = "BATCH_ID")
    private String batchId;
    @Column(name = "PROD_CODE")
    private String prodCode;
    @Column(name = "ASET_CLASS_CDE")
    private String asetClassCde;
    @Column(name = "MARKET_TYPE_CODE")
    private String marketTypeCode;
    @Column(name = "DELAY_TIME")
    private Integer delayTime;

    public String toString() {
        return "DelayProdInfo [delayProdId=" + this.delayProdId + ", batchId=" + this.batchId + ", prodCode=" + this.prodCode
            + ", asetClassCde=" + this.asetClassCde + ", marketTypeCode=" + this.marketTypeCode + ", delayTime=" + this.delayTime
            + "]";
    }

}
