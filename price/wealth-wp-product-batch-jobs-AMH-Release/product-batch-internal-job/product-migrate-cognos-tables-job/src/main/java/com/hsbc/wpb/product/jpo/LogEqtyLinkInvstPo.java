package com.dummy.wpb.product.jpo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "LOG_EQTY_LINK_INVST")
public class LogEqtyLinkInvstPo implements Serializable {

    @Id
    @Column(name = "ROWID")
    private String id;
    @Column(name = "LOG_ID")
    private BigDecimal logId;
    @Column(name = "PROC_NAME")
    private String procName;
    @Column(name = "LOG_LEVEL")
    private String logLevel;
    @Column(name = "LOG_MSG")
    private String logMsg;
    @Column(name = "SQL_ERRM")
    private String sqlErrm;
    @Column(name = "ERRM_STAT")
    private String errmStat;
    @Column(name = "CTRY_CDE")
    private String ctryCde;
    @Column(name = "ORGN_CDE")
    private String orgnCde;
    @Column(name = "PROD_ID")
    private String prodId;
    @Column(name = "LOG_DATE")
    private Date logDate;
}
