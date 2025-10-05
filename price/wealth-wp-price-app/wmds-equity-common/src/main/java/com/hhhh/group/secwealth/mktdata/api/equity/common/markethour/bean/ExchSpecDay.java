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
public class ExchSpecDay implements Serializable {

    private static final long serialVersionUID = -4475522549818271567L;
    @Id
    @GeneratedValue()
    @Column(name = "EXCH_HLDAY_ID")
    private Long exchSpecId;
    @Column(name = "SPEC_DAY")
    private String specDay;
    @Column(name = "MARKET_TYPE_CODE")
    private String marketTypeCode;
    @Column(name = "SPEC_DAY_DESC")
    private String specDayDesc;
    @Column(name = "OPEN_TIME")
    private String openTime;
    @Column(name = "CLOSE_TIME")
    private String closeTime;

    public String toString() {
        return "ExchSpecDay [exchSpecId=" + this.exchSpecId + ", specDay=" + this.specDay + ", marketTypeCode="
            + this.marketTypeCode + ", specDayDesc=" + this.specDayDesc + ", openTime=" + this.openTime + ", closeTime="
            + this.closeTime + "]";
    }


}
