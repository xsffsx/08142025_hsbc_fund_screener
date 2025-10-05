/*
 */

package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MKT_DATA_INQ_CNT")
@IdClass(QuoteSubCounter.class)
public class QuoteCounter extends QuoteSubCounter{

	private static final long serialVersionUID = 2175017938211111988L;
	@Column(name = "INQ_CNT")
	private long quoteCounter;
	@Column(name = "REC_UPDT_LA_DT_TM")
	private Timestamp lastUpdate;

}
