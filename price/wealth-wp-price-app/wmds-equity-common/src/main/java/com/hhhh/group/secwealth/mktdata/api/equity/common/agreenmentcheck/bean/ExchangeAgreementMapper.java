/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * <b> Exchange agreement mapper for table EXCH_AGREE_MAP. </b>
 * </p>
 */
@Entity
@Table(name = "EXCH_AGREE_MAP")
public class ExchangeAgreementMapper {

    @Id
    @Column(name = "DOC_ID")
    private int documentId;

    @Column(name = "MKT_PRD_EXCH_ID")
    private int exchangeId;

    @Column(name = "USER_UPDT_NUM")
    private String updatedBy;

    @Column(name = "REC_UPDT_DT_TM")
    private Timestamp updatedTime;
}
