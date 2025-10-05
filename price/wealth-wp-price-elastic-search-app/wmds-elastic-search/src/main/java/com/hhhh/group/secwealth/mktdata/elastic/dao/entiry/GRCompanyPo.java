package com.hhhh.group.secwealth.mktdata.elastic.dao.entiry;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "MKT_DATA_GR_COMPANY")
public class GRCompanyPo implements Serializable {

    @Id
    @Column(nullable = false, name = "COM_ID")
    private Long companyId;

    @Column(name = "PROD_MKT_CDE")
    private String market;

    @Column(name = "COM_NAME")
    private String companyName;

    @Column(name = "PROD_RIC_CDE")
    private String ricCode;

    @Column(name = "PROD_EXTNL_CDE")
    private String symbol;

    @Column(name = "CUR_RECOM_VAL")
    private String rating;

    @Column(name = "EXPIR_IND")
    private String expire;

    @Column(nullable = false, name = "REC_UPDT_LA_DT_TM")
    private Timestamp lastUpdate;

    @ManyToMany(targetEntity = GRDocumentPo.class)
    @OrderBy("publishedDate DESC")
    @JoinTable(name = "MKT_DATA_GR_COM_DOC",
            joinColumns = {@JoinColumn(name = "COM_ID", referencedColumnName = "COM_ID")},
            inverseJoinColumns = {@JoinColumn(name = "DOC_ID", referencedColumnName = "DOC_ID")}
    )
    private Set<GRDocumentPo> documents = new HashSet<>();
}
