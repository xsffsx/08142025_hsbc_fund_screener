/**
 * @Title QuoteAccessLog.java
 * @description TODO
 * @author OJim
 * @time Jul 8, 2019 11:10:51 AM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.dao.entiry;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "MKT_DATA_USER")
public class QuoteUserForTopMover implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "MKT_DATA_USER_SEQ", allocationSize = 1)
	@Column(nullable = false, name = "USER_ID")
	private Long userReferenceId;

	@Column(nullable = false, name = "USER_TYPE_CDE")
	private String userType;

	@Column(nullable = false, name = "USER_EXTNL_ID_NUM")
	private String userExtnlId;

	@Column(nullable = false, name = "GRP_MEMBR_CDE")
	private String groupMember;

	@Column(nullable = false, name = "CTRY_REC_CDE")
	private String userMarketCode;

	@Column(nullable = true, name = "LOGON_LAST_TM")
	private Date lastLogin;

	@Column(nullable = false, name = "REC_UPDT_LA_DT_TM")
	private Date lastUpdate;

	@Column(nullable = true, name = "MONITOR_FLAG")
	private Long monitorFlag;
}
