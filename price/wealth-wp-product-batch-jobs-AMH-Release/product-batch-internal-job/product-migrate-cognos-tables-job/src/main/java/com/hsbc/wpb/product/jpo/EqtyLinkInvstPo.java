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
@Table(name = "TB_EQTY_LINK_INVST")
public class EqtyLinkInvstPo implements Serializable {

    @Id
    @Column(name = "PROD_ID_EQTY_LINK_INVST")
    private Long prodIdEqtyLinkInvst;
    @Column(name = "PROD_EXTNL_CDE")
    private String prodExtnlCde;
    @Column(name = "PROD_EXTNL_TYPE_CDE")
    private String prodExtnlTypeCde;
    @Column(name = "EQTY_LINK_INVST_TYPE_CDE")
    private String eqtyLinkInvstTypeCde;
    @Column(name = "TRD_DT")
    private Date trdDt;
    @Column(name = "DSCNT_BUY_PCT")
    private BigDecimal dscntBuyPct;
    @Column(name = "DSCNT_SELL_PCT")
    private BigDecimal dscntSellPct;
    @Column(name = "YIELD_TO_MTUR_PCT")
    private BigDecimal yieldToMturPct;
    @Column(name = "DEN_AMT")
    private BigDecimal denAmt;
    @Column(name = "TRD_MIN_AMT")
    private BigDecimal trdMinAmt;
    @Column(name = "SUPT_AON_IND")
    private String suptAonInd;
    @Column(name = "PYMT_DT")
    private Date pymtDt;
    @Column(name = "VALN_DT")
    private Date valnDt;
    @Column(name = "OFFER_TYPE_CDE")
    private String offerTypeCde;
    @Column(name = "CUST_SELL_QTA_CNT")
    private Long custSellQtaCnt;
    @Column(name = "RULE_QTA_ALTMT_CDE")
    private String ruleQtaAltmtCde;
    @Column(name = "SETL_DT")
    private Date setlDt;
    @Column(name = "LNCH_PROD_IND")
    private String lnchProdInd;
    @Column(name = "RTRV_PROD_EXTNL_IND")
    private String rtrvProdExtnlInd;
    @Column(name = "PROD_EXTNL_CAT_CDE")
    private String prodExtnlCatCde;
    @Column(name = "PDCY_CALL_CDE")
    private String pdcyCallCde;
    @Column(name = "PDCY_KNOCK_IN_CDE")
    private String pdcyKnockInCde;
}
