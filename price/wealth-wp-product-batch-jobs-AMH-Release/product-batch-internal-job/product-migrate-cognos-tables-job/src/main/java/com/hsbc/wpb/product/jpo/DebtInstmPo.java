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
@Table(name = "TB_DEBT_INSTM")
public class DebtInstmPo implements Serializable {

    @Id
    @Column(name = "PROD_ID_DEBT_INSTM")
    private Long prodIdDebtInstm;
    @Column(name = "ISR_BOND_NAME")
    private String isrBondName;
    @Column(name = "ISSUE_NUM")
    private String issueNum;
    @Column(name = "PROD_ISS_DT")
    private Date prodIssDt;
    @Column(name = "PDCY_COUPN_PYMT_CDE")
    private String pdcyCoupnPymtCde;
    @Column(name = "COUPN_ANNL_RATE")
    private BigDecimal coupnAnnlRate;
    @Column(name = "COUPN_EXT_INSTM_RATE")
    private BigDecimal coupnExtInstmRate;
    @Column(name = "PYMT_COUPN_NEXT_DT")
    private Date pymtCoupnNextDt;
    @Column(name = "FLEX_MAT_OPT_IND")
    private String flexMatOptInd;
    @Column(name = "INT_IND_ACCR_AMT")
    private BigDecimal intIndAccrAmt;
    @Column(name = "INVST_INCRM_MIN_AMT")
    private BigDecimal invstIncrmMinAmt;
    @Column(name = "PROD_BOD_LOT_QTY_CNT")
    private BigDecimal prodBodLotQtyCnt;
    @Column(name = "MTUR_EXT_DT")
    private Date mturExtDt;
    @Column(name = "SUB_DEBT_IND")
    private String subDebtInd;
    @Column(name = "BOND_STAT_CDE")
    private String bondStatCde;
    @Column(name = "CTRY_BOND_ISSUE_CDE")
    private String ctryBondIssueCde;
    @Column(name = "GRNTR_NAME")
    private String grntrName;
    @Column(name = "CPTL_TIER_TEXT")
    private String cptlTierText;
    @Column(name = "COUPN_TYPE")
    private String coupnType;
    @Column(name = "COUPN_PREV_RATE")
    private BigDecimal coupnPrevRate;
    @Column(name = "INDEX_FLT_RATE_NAME")
    private String indexFltRateName;
    @Column(name = "BOND_FLT_SPRD_RATE")
    private BigDecimal bondFltSprdRate;
    @Column(name = "COUPN_CURR_START_DT")
    private Date coupnCurrStartDt;
    @Column(name = "COUPN_PREV_START_DT")
    private Date coupnPrevStartDt;
    @Column(name = "BOND_CALL_NEXT_DT")
    private Date bondCallNextDt;
    @Column(name = "INT_BASIS_CALC_TEXT")
    private String intBasisCalcText;
    @Column(name = "INT_ACCR_DAY_CNT")
    private BigDecimal intAccrDayCnt;
    @Column(name = "INVST_SOLD_LEST_AMT")
    private BigDecimal invstSoldLestAmt;
    @Column(name = "INVST_INCRM_SOLD_AMT")
    private BigDecimal invstIncrmSoldAmt;
    @Column(name = "SHR_BID_CNT")
    private BigDecimal shrBidCnt;
    @Column(name = "SHR_OFFR_CNT")
    private BigDecimal shrOffrCnt;
    @Column(name = "PROD_CLOSE_BID_PRC_AMT")
    private BigDecimal prodCloseBidPrcAmt;
    @Column(name = "PROD_CLOSE_OFFR_PRC_AMT")
    private BigDecimal prodCloseOffrPrcAmt;
    @Column(name = "BOND_CLOSE_DT")
    private Date bondCloseDt;
    @Column(name = "BOND_SETL_DT")
    private Date bondSetlDt;
    @Column(name = "DSCNT_MRGN_BSEL_PCT")
    private BigDecimal dscntMrgnBselPct;
    @Column(name = "DSCNT_MRGN_BBUY_PCT")
    private BigDecimal dscntMrgnBbuyPct;
    @Column(name = "PRC_BOND_RECV_DT_TM")
    private Date prcBondRecvDtTm;
    @Column(name = "YIELD_OFFER_TEXT")
    private String yieldOfferText;
    @Column(name = "COUPN_ANNL_TEXT")
    private String coupnAnnlText;
    @Column(name = "YIELD_EFF_DT")
    private Date yieldEffDt;
    @Column(name = "YIELD_BID_PCT")
    private BigDecimal yieldBidPct;
    @Column(name = "YIELD_TO_CALL_BID_PCT")
    private BigDecimal yieldToCallBidPct;
    @Column(name = "YIELD_TO_MTUR_BID_PCT")
    private BigDecimal yieldToMturBidPct;
    @Column(name = "YIELD_BID_CLOSE_PCT")
    private BigDecimal yieldBidClosePct;
    @Column(name = "YIELD_OFFER_PCT")
    private BigDecimal yieldOfferPct;
    @Column(name = "YIELD_TO_CALL_OFFER_PCT")
    private BigDecimal yieldToCallOfferPct;
    @Column(name = "YIELD_TO_MTUR_OFFER_TEXT")
    private String yieldToMturOfferText;
    @Column(name = "YIELD_OFFER_CLOSE_PCT")
    private BigDecimal yieldOfferClosePct;
    @Column(name = "YIELD_DT")
    private Date yieldDt;
    @Column(name = "ISR_DESC")
    private String isrDesc;
    @Column(name = "SR_TYPE_CDE")
    private String srTypeCde;
    @Column(name = "PROD_GBM_BID_PRC_AMT")
    private BigDecimal prodGbmBidPrcAmt;
    @Column(name = "PROD_GBM_OFFR_PRC_AMT")
    private BigDecimal prodGbmOffrPrcAmt;
}
