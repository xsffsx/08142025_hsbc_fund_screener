/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public class IndexQuoteCounterPrimaryUSPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "USER_ID")
    @Id
    private long playerReferenceNumber;
    @Column(name = "CTRY_PROD_EXCHG_MKT_CDE")
    @Id
    private String tradingMarketCode;
    @Column(name = "YR_NUM")
    @Id
    private String quoteYear;
    @Column(name = "MO_NUM")
    @Id
    private String quoteMonth;
    @Column(name = "INQ_TYPE_CDE")
    @Id
    private String quoteType;
    @Column(name = "INQ_SB_TYPE_CDE")
    @Id
    private String subQuoteType;

    @Column(name = "ENTT_CDE")
    @Id
    private String entityCode;
}
