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
 * <b> Market document for Table DOC. </b>
 * </p>
 */

@Entity
@Table(name = "DOC")
@Setter
@Getter
public class MarketDocument {

    @Id
    @Column(name = "DOC_ID")
    private int documentId;

    @Column(name = "DOC_CDE")
    private String documentCode;

    @Column(name = "DOC_DESC")
    private String updateBy;

    @Column(name = "REC_UPDT_DT_TM")
    private Timestamp updateTime;
}
