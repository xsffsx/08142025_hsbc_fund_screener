/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> Exchange info for table MKT_PRD_EXCH_INFO. </b>
 * </p>
 */
@Entity
@Table(name = "MKT_PRD_EXCH_INFO")
@Getter
@Setter
public class ExchangeInfo {

    @Id
    @Column(name = "MKT_PRD_EXCH_ID", unique = true, nullable = false)
    private int id;

    @Column(name = "MKT_PRD_EXCH_CDE")
    private String exchangeCode;

    @Column(name = "MKT_PRD_EXCH_DESC")
    private String exchangeDesc;

    @Column(name = "USER_UPDT_NUM")
    private String updateBy;


    @Column(name = "REC_UPDT_DT_TM")
    private Timestamp updateTime;
}
