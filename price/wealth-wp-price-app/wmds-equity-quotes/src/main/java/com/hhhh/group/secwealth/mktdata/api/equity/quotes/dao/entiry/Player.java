/*
 */

package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;

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

@Getter
@Setter
@Entity
@Table(name = "MKT_DATA_USER")
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "MKT_DATA_USER_SEQ", allocationSize = 1)
	@Column(nullable = false, name = "USER_ID")
	private long playerReferenceNumber;
	
	@Column(name = "USER_TYPE_CDE")
	private String playerType;

	@Column(name = "USER_EXTNL_ID_NUM")
	private String playerId;

	@Column(name = "LOGON_LAST_TM")
	private Timestamp lastLogonTime;

	@Column(name = "REC_UPDT_LA_DT_TM")
	private Timestamp lastUpdate;

	@Column(name = "CTRY_REC_CDE")
	private String countryCode;

	@Column(name = "GRP_MEMBR_CDE")
	private String groupMember;

}
