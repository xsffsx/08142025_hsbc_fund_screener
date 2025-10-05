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
public class ExchHlday implements Serializable {

    /**
     * <p>
     * <b> TODO : Insert description of the field. </b>
     * </p>
     */
    private static final long serialVersionUID = -1011312789569301753L;
    @Id
    @GeneratedValue()
    @Column(name = "EXCH_HLDAY_ID")
    private Long exchHldayId;
    @Column(name = "HOLIDAY")
    private String holiday;
    @Column(name = "MARKET_TYPE_CODE")
    private String marketTypeCode;
    @Column(name = "HOLIDAY_DESC")
    private String holidayDesc;
    @Column(name = "OPEN_TIME")
    private String openTime;
    @Column(name = "CLOSE_TIME")
    private String closeTime;

    public String toString() {
        return "ExchHlday [exchHldayId=" + this.exchHldayId + ", holiday=" + this.holiday + ", marketTypeCode="
            + this.marketTypeCode + ", holidayDesc=" + this.holidayDesc + ", openTime=" + this.openTime + ", closeTime="
            + this.closeTime + "]";
    }

}
