/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> TODO : check the exchange is open or close</b>
 * </p>
 */
@Setter
@Getter
@Entity
public class ExchangeOpenTime implements Serializable {
    private static final long serialVersionUID = -627470924166124075L;
    @Column(name = "TRD_SESS_ID")
    private String trdSessId;
    @Column(name = "HR_SESS_START_TM_NUM")
    private Integer hrSessStartTmNum;
    @Column(name = "MINUT_SESS_START_TM_NUM")
    private Integer minutSessStartTmNum;
    @Column(name = "SECND_SESS_START_TM_NUM")
    private Integer secndSessStartTmNum;
    @Column(name = "HR_SESS_CLOSE_TM_NUM")
    private Integer hrSessCloseTmNum;
    @Column(name = "MINUT_SESS_CLOSE_TM_NUM")
    private Integer minutSessCloseTmNum;
    @Column(name = "SECND_SESS_CLOSE_TM_NUM")
    private Integer secndSessCloseTmNum;
    @Column(name = "UTC_OFST_NUM")
    private Integer utcOfstNum;
    @Column(name = "TRD_DT")
    private String trdDt;
    @Id
    @GeneratedValue()
    @Column(name = "RN", unique = true, nullable = false)
    private Integer rn;

    public String toString() {
        return "ExchangeOpenTime [trdSessId=" + this.trdSessId + ", hrSessStartTmNum=" + this.hrSessStartTmNum
            + ", minutSessStartTmNum=" + this.minutSessStartTmNum + ", secndSessStartTmNum=" + this.secndSessStartTmNum
            + ", hrSessCloseTmNum=" + this.hrSessCloseTmNum + ", minutSessCloseTmNum=" + this.minutSessCloseTmNum
            + ", secndSessCloseTmNum=" + this.secndSessCloseTmNum + ", utcOfstNum=" + this.utcOfstNum + ", trdDt=" + this.trdDt
            + ", rn=" + this.rn + "]";
    }

}
