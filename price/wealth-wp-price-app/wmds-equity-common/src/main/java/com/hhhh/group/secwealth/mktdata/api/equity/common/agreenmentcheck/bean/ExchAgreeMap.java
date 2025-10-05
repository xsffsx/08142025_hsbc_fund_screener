/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean;

import java.sql.Timestamp;

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
@Table(name = "exch_agree_map")
public class ExchAgreeMap {
    @Id
    @GeneratedValue()
    @Column(name = "doc_id")
    private Long docId;
    @Column(name = "mkt_prd_exch_Id")
    private Integer mktPrdExchId;
    @Column(name = "rec_updt_dt_tm")
    private Timestamp recUpdtDtTm;
    @Column(name = "user_updt_num")
    private String userUpdtNum;
}
