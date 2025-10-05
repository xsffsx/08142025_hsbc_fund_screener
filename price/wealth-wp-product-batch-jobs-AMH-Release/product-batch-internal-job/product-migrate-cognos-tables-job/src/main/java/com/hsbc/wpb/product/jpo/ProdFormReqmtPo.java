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
@Table(name = "PROD_FORM_REQMT")
public class ProdFormReqmtPo implements Serializable {

    @Id
    @Column(name = "ROWID")
    private String rowid;
    @Column(name = "PROD_ID")
    private Long prodId;
    @Column(name = "FORM_REQ_CDE")
    private String formReqCde;
    @Column(name = "REC_CREAT_DT_TM")
    private Date recCreatDtTm;
}
