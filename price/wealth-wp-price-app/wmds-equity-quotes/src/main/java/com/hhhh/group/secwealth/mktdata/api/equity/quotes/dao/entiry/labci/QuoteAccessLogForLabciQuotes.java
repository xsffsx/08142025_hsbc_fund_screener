/**
 * @Title QuoteAccessLog.java
 * @description TODO
 * @author OJim
 * @time Jul 8, 2019 11:10:51 AM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.labci;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
/**
 * @Title QuoteAccessLog.java
 * @description TODO
 * @author OJim
 * @time Jul 8, 2019 11:10:51 AM
 */
@Getter
@Setter
@Entity
@Table(name = "MKT_DATA_LABCI_CHNL_ACCES_LOG")
public class QuoteAccessLogForLabciQuotes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "MKT_DATA_LABCI_CHNL_LOG_SEQ", allocationSize = 1)
	@Column(nullable = false, name = "CHNL_ACCES_LOG_ID")
	private Long chnlAccesLogId;

	@Column(nullable = false, name = "USER_ID")
	private Long userReferenceId;

	@Column(nullable = false, name = "MKT_CDE")
	private String marketCode;

	@Column(nullable = false, name = "MKT_PRD_EXCH_CDE")
	private String exchangeCode;

	@Column(nullable = false, name = "APP_CDE")
	private String applicationCode;

	@Column(nullable = false, name = "CTRY_CDE")
	private String countryCode;

	@Column(nullable = false, name = "GRP_MEMB")
	private String groupMember;

	@Column(nullable = false, name = "CHNL_ID")
	private String channelId;

	@Column(nullable = false, name = "FUNC_ID")
	private String funtionId;

	@Column(nullable = false, name = "ACCES_CMND_CDE")
	private String accessCommand;

	@Column(nullable = false, name = "CHRG_CAT_CDE")
	private String chargeCategory;

	@Column(nullable = false, name = "RQST_TYPE")
	private String requestType;

	@Column(nullable = false, name = "INQ_ACCES_CNT")
	private int accessCount;

	@Column(nullable = false, name = "RESP_TYPE")
	private String responseType;

	@Column(nullable = false, name = "RESP_TEXT")
	private String responseText;

	@Column(nullable = false, name = "CMNT_TEXT")
	private String commentText;

	@Column(nullable = false, name = "REC_UPDT_DT_TM")
	private Timestamp updatedDateTime;

}
