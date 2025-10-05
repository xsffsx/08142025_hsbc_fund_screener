/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Setter
@Getter
public class ExchangeOpenClose implements Serializable {

    private static final long serialVersionUID = -4175521837579450910L;
    private String date;
    // the open time of exchange(millisecond)
    private Long openTime;
    // the closed time of exchange(millisecond)
    private Long closeTime;

    private String trdSessId;

    public String toString() {
        return "ExchangeOpenClose [date=" + this.date + ", openTime=" + this.openTime + ", closeTime=" + this.closeTime
            + ", trdSessId=" + this.trdSessId + "]";
    }
}
