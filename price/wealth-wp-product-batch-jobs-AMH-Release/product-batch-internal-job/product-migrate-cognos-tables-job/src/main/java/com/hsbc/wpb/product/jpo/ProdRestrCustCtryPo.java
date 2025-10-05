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
@Table(name = "PROD_RESTR_CUST_CTRY")
public class ProdRestrCustCtryPo implements Serializable {

    @Id
    @Column(name = "ROWID")
    private String rowid;
    @Column(name = "PROD_ID")
    private Long prodId;
    @Column(name = "CTRY_ISO_CDE")
    private String ctryIsoCde;
    @Column(name = "RESTR_CTRY_TYPE_CDE")
    private String restrCtryTypeCde;
    @Column(name = "RESTR_CDE")
    private String restrCde;
    @Column(name = "REC_CREAT_DT_TM")
    private Date recCreatDtTm;
    @Column(name = "REC_UPDT_DT_TM")
    private Date recUpdtDtTm;
}
