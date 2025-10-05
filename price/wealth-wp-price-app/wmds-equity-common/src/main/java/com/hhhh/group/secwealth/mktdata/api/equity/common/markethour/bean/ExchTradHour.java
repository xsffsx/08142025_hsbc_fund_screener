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
public class ExchTradHour implements Serializable {

    private static final long serialVersionUID = -284653159335898496L;
    @Id
    @GeneratedValue()
    @Column(name = "SESSN_MAP_ID")
    private Long sessnMapId;
    @Column(name = "MARKET_TYPE_CODE")
    private String marketTypeCode;
    @Column(name = "OPEN_TIME")
    private String openTime;
    @Column(name = "CLOSE_TIME")
    private String closeTime;
    @Column(name = "DAY_OF_WEEK")
    private Integer dayOfweek;

    public String toString() {
        return "ExchTradHour [sessnMapId=" + this.sessnMapId + ", marketTypeCode=" + this.marketTypeCode + ", openTime="
            + this.openTime + ", closeTime=" + this.closeTime + ", dayOfweek=" + this.dayOfweek + "]";
    }


}
