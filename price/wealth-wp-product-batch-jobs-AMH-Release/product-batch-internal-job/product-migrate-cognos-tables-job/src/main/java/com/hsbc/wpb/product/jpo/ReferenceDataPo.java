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
@Table(name = "CDE_DESC_VALUE")
public class ReferenceDataPo implements Serializable {

    @Id
    private Long id;
    @Column(name = "CTRY_REC_CDE")
    private String ctryRecCde;
    @Column(name = "GRP_MEMBR_REC_CDE")
    private String grpMembrRecCde;
    @Column(name = "CDV_TYPE_CDE")
    private String cdvTypeCde;
    @Column(name = "CDV_CDE")
    private String cdvCde;
    @Column(name = "CDV_DESC")
    private String cdvDesc;
    @Column(name = "CDV_PLL_DESC")
    private String cdvPllDesc;
    @Column(name = "CDV_SLL_DESC")
    private String cdvSllDesc;
    @Column(name = "REC_CMNT_TEXT")
    private String recCmntText;
    @Column(name = "CDV_DISP_SEQ_NUM")
    private Long cdvDispSeqNum;
    @Column(name = "CDV_PARNT_TYPE_CDE")
    private String cdvParntTypeCde;
    @Column(name = "CDV_PARNT_CDE")
    private String cdvParntCde;
    @Column(name = "REC_CREAT_DT_TM")
    private Date recCreatDtTm;
    @Column(name = "REC_UPDT_DT_TM")
    private Date recUpdtDtTm;
}
