/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.markethour.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "MARKET_TYPE_INFO")
public class MarketTypeInfo implements Serializable {

    private static final long serialVersionUID = 217919310316498674L;

    @Id
    @GeneratedValue()
    @Column(name = "MARKET_TYPE_ID")
    private Long marketTypeId;
    @Column(name = "MARKET_TYPE")
    private String marketType;
    @Column(name = "MARKET_TYPE_CODE")
    private String marketTypeCode;
    @Column(name = "MARKET_TYPE_DESC")
    private String marketTypeDesc;
    @Column(name = "MARKET_TIME_ZONE")
    private String marketTimeZone;

    public String toString() {
        return "MarketTypeInfo [marketTypeId=" + this.marketTypeId + ", marketType=" + this.marketType + ", marketTypeCode="
            + this.marketTypeCode + ", marketTypeDesc=" + this.marketTypeDesc + ", marketTimeZone=" + this.marketTimeZone + "]";
    }


}
