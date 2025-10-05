package com.dummy.wpb.product.jpo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "TB_PROD_ALT_ID")
public class ProdAltIdPo implements Serializable {

    @Id
    @Column(name = "ROWID")
    private String rowid;
    @Column(name = "PROD_ID")
    private Long prodId;
    @Column(name = "CTRY_REC_CDE")
    private String ctryRecCde;
    @Column(name = "GRP_MEMBR_REC_CDE")
    private String grpMembrRecCde;
    @Column(name = "PROD_CDE_ALT_CLASS_CDE")
    private String prodCdeAltClassCde;
    @Column(name = "PROD_TYPE_CDE")
    private String prodTypeCde;
    @Column(name = "PROD_ALT_NUM")
    private String prodAltNum;
    @Column(name = "REC_CREAT_DT_TM")
    private Date recCreatDtTm;
    @Column(name = "REC_UPDT_DT_TM")
    private Date recUpdtDtTm;
}
