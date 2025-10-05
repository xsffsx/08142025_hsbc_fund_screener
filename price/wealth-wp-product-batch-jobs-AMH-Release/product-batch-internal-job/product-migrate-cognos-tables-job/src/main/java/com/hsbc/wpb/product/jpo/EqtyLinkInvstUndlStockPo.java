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
@Table(name = "TB_EQTY_LINK_INVST_UNDL_STOCK")
public class EqtyLinkInvstUndlStockPo implements Serializable {

    @Id
    @Column(name = "ROWID")
    private String rowid;
    @Column(name = "PROD_ID_EQTY_LINK_INVST")
    private Long prodIdEqtyLinkInvst;
    @Column(name = "PROD_EARL_CALL_TRIG_PRC_AMT")
    private BigDecimal prodEarlCallTrigPrcAmt;
    @Column(name = "PROD_EXPI_CLOSE_PRC_AMT")
    private BigDecimal prodExpiClosePrcAmt;
    @Column(name = "PROD_DOWN_OUT_BAR_PRC_AMT")
    private BigDecimal prodDownOutBarPrcAmt;
    @Column(name = "INSTM_UNDL_CDE")
    private String instmUndlCde;
    @Column(name = "INSTM_UNDL_TEXT")
    private String instmUndlText;
    @Column(name = "CRNCY_INSTM_UNDL_PRICE_CDE")
    private String crncyInstmUndlPriceCde;
    @Column(name = "PROD_STRK_PRC_AMT")
    private BigDecimal prodStrkPrcAmt;
    @Column(name = "PROD_STRK_CALL_PRC_AMT")
    private BigDecimal prodStrkCallPrcAmt;
    @Column(name = "PROD_STRK_PUT_PRC_AMT")
    private BigDecimal prodStrkPutPrcAmt;
    @Column(name = "PROD_CLOSE_PRC_AMT")
    private BigDecimal prodClosePrcAmt;
    @Column(name = "PROD_CLOSE_LOW_PRC_AMT")
    private BigDecimal prodCloseLowPrcAmt;
    @Column(name = "PROD_CLOSE_UPPR_PRC_AMT")
    private BigDecimal prodCloseUpprPrcAmt;
    @Column(name = "PROD_CLOSE_PUT_PRC_AMT")
    private BigDecimal prodClosePutPrcAmt;
    @Column(name = "PROD_CLOSE_CALL_PRC_AMT")
    private BigDecimal prodCloseCallPrcAmt;
    @Column(name = "PROD_EXER_PRC_AMT")
    private BigDecimal prodExerPrcAmt;
    @Column(name = "PROD_BREAK_EVEN_PRC_AMT")
    private BigDecimal prodBreakEvenPrcAmt;
    @Column(name = "PROD_BREAK_EVEN_LOW_PRC_AMT")
    private BigDecimal prodBreakEvenLowPrcAmt;
    @Column(name = "PROD_BREAK_EVEN_UPPR_PRC_AMT")
    private BigDecimal prodBreakEvenUpprPrcAmt;
    @Column(name = "PROD_BREAK_EVEN_PUT_PRC_AMT")
    private BigDecimal prodBreakEvenPutPrcAmt;
    @Column(name = "PROD_BREAK_EVEN_CALL_PRC_AMT")
    private BigDecimal prodBreakEvenCallPrcAmt;
    @Column(name = "SPRD_CNT")
    private Long sprdCnt;
    @Column(name = "INSTM_ENTL_CNT")
    private BigDecimal instmEntlCnt;
    @Column(name = "PROD_KNOCK_IN_PRICE_AMT")
    private BigDecimal prodKnockInPriceAmt;
    @Column(name = "PROD_STRK_PRICE_INIT_PCT")
    private BigDecimal prodStrkPriceInitPct;
    @Column(name = "PROD_STRK_CALL_PRICE_INIT_PCT")
    private BigDecimal prodStrkCallPriceInitPct;
    @Column(name = "PROD_KNOCK_IN_PRICE_INIT_PCT")
    private BigDecimal prodKnockInPriceInitPct;
    @Column(name = "REC_CREAT_DT_TM")
    private Date recCreatDtTm;
    @Column(name = "REC_UPDT_DT_TM")
    private Date recUpdtDtTm;
    @Column(name = "PROD_ID_UNDL_INSTM")
    private Long prodIdUndlInstm;
    @Column(name = "PROD_INIT_SPOT_PRICE_AMT")
    private BigDecimal prodInitSpotPriceAmt;
    @Column(name = "PROD_FLR_PRC_AMT")
    private BigDecimal prodFlrPrcAmt;
    @Column(name = "PROD_BAR_PRC_AMT")
    private BigDecimal prodBarPrcAmt;
    @Column(name = "PROD_KNOCK_IN_TRIG_PRC_AMT")
    private BigDecimal prodKnockInTrigPrcAmt;
}
