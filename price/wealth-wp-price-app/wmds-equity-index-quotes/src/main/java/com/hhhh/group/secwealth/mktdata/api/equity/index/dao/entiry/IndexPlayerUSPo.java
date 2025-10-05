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
@Table(name = "MKT_DATA_USER")
public class IndexPlayerUSPo {

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
