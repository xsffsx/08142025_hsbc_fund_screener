/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.dao.entiry;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
    @Table(name = "MKT_DATA_INQ_CNT")
@IdClass(IndexQuoteCounterPrimaryUSPo.class)
public class IndexQuoteCounterUSPo extends IndexQuoteCounterPrimaryUSPo {

    private static final long serialVersionUID = 2275946714710612188L;

    @Column(name = "INQ_CNT")
    private long quoteCounter;

    @Column(name = "REC_UPDT_LA_DT_TM")
    private Timestamp lastUpdate;
}