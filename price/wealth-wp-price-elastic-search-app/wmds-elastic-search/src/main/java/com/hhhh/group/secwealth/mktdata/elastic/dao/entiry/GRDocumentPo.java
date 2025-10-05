package com.hhhh.group.secwealth.mktdata.elastic.dao.entiry;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "MKT_DATA_GR_DOCUMENT")
public class GRDocumentPo implements Serializable {

    @Id
    @Column(nullable = false, name = "DOC_ID")
    private Long documentId;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "REP_UDI")
    private String udi;

    @Column(name = "INTNL_REP_URL")
    private String urlInternal;

    @Column(name = "DOC_TYPE")
    private Integer docType;

    @Column(name = "ACT_TYPE")
    private String action;

    @Column(name = "REP_PUB_DATE")
    private String publishedDate;

    @Column(name = "REP_UPDT_TM")
    private Timestamp updatedTimestamp;

    @Column(nullable = false, name = "FIRST_DISSEMINATE_TM")
    private Timestamp firstDisseminationDate;

    @Column(nullable = false, name = "LAST_DISSEMINATE_TM")
    private Timestamp lastDisseminationDate;

    @Column(nullable = false, name = "REC_UPDT_LA_DT_TM")
    private Timestamp lastUpdate;
}
