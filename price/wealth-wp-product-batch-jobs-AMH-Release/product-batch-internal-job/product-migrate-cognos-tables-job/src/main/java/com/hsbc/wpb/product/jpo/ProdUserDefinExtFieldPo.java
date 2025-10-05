package com.dummy.wpb.product.jpo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "TB_PROD_USER_DEFIN_EXT_FIELD")
public class ProdUserDefinExtFieldPo implements Serializable {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "ID", unique = true, nullable = false)
    @Column(name = "ROWID")
    private String rowid;
    @Column(name = "PROD_ID")
    private Long prodId;
    @Column(name = "FIELD_TYPE_CDE")
    private String fieldTypeCde;
    @Column(name = "FIELD_CDE")
    private String fieldCde;
    @Column(name = "FIELD_SEQ_NUM")
    private Long fieldSeqNum;
    @Column(name = "CTRY_REC_CDE")
    private String ctryRecCde;
    @Column(name = "GRP_MEMBR_REC_CDE")
    private String grpMembrRecCde;
    @Column(name = "FIELD_DATA_TYPE_TEXT")
    private String fieldDataTypeText;
    @Column(name = "FIELD_CHAR_VALUE_TEXT")
    private String fieldCharValueText;
    @Column(name = "FIELD_STRNG_VALUE_TEXT")
    private String fieldStringValueText;
    @Column(name = "FIELD_INTG_VALUE_NUM")
    private Long fieldIntgValueText;
    @Column(name = "FIELD_DCML_VALUE_NUM")
    private Double fieldDcmlValueText;
    @Column(name = "FIELD_DT_VALUE_DT")
    private Date fieldDtValueText;
    @Column(name = "FIELD_TS_VALUE_DT_TM")
    private Date fieldTsValueText;
    @Column(name = "REC_CREAT_DT_TM")
    private Date recCreatDtTm;
    @Column(name = "REC_UPDT_DT_TM")
    private Date recUpdtDtTm;
}
