/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "MKT_DATA_ACCES_LOG")
public class IndexAccessLogUSPo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "MKT_DATA_ACCES_LOG_SEQ", allocationSize = 1)
    @Column(name = "ACCES_LOG_ID")
    private long chanelAccessLogId;

    @Column(name = "USER_ID")
    private long playerReferenceNumber;

    @Column(name = "CTRY_PROD_EXCHG_MKT_CDE")
    private String tradingMarketCode;

    @Column(name = "CHRG_ACCES_IND")
    private String chargeableFlag;

    @Column(name = "CHRG_CAT_CDE")
    private String chargeCategory;

    @Column(name = "INQ_ACCES_CNT")
    private long quoteUsage;

    @Column(name = "ACCES_TYPE")
    private String accessType;

    @Column(name = "ACCES_CMND_CDE")
    private String accessCommand;

    @Column(name = "ACCES_DETL_TEXT")
    private String accessDetail;

    @Column(name = "CMNT_TEXT")
    private String remarkField;

    @Column(name = "REC_UPDT_LA_DT_TM")
    private Timestamp lastUpdate;
}
