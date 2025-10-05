
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hhhh.group.secwealth.mktdata.common.util.MorningStarUtil;


@Entity
@Table(name = "V_UT_PROD_INSTM")
public class UtProdInstm implements Serializable {

    private static final long serialVersionUID = 3612360837081138819L;

    @EmbeddedId
    private UtProdInstmId utProdInstmId;

    @Column(nullable = false, name = "CTRY_PROD_TRADE_CDE", columnDefinition = "char")
    private String market;

    @Column(name = "MSTARID")
    private String mStarID;

    @Column(nullable = false, name = "PROD_TYPE_CDE")
    private String productType;

    @Column(nullable = false, name = "PROD_SUBTP_CDE")
    private String productSubTypeCode;

    @Column(nullable = false, name = "SYMBOL")
    private String symbol;

    @Column(nullable = false, name = "PERFORMANCE_ID")
    private String performanceId;

    @Column(nullable = false, name = "FUNDSERVICE_ID")
    private String fundserviceId;

    @Column(nullable = true, name = "PROD_NAME")
    protected String prodName;

    @Column(nullable = true, name = "PROD_PLL_NAME")
    protected String prodPllName;

    @Column(nullable = true, name = "PROD_SLL_NAME")
    protected String prodSllName;

    @Column(nullable = false, name = "FUND_FM_CDE")
    private String familyCode;

    @Column(nullable = true, name = "PROD_NLS_NAME_FAM1")
    private String familyName1;

    @Column(nullable = true, name = "PROD_NLS_NAME_FAM2")
    private String familyName2;

    @Column(nullable = true, name = "PROD_NLS_NAME_FAM3")
    private String familyName3;

    @Column(nullable = false, name = "FUND_CAT_CDE")
    private String categoryCode;

    @Column(nullable = true, name = "PROD_NLS_NAME_CAT1")
    private String categoryName1;

    @Column(nullable = true, name = "PROD_NLS_NAME_CAT2")
    private String categoryName2;

    @Column(nullable = true, name = "PROD_NLS_NAME_CAT3")
    private String categoryName3;

    @Column(nullable = true, name = "FUND_CAT_LVL0_CDE")
    private String categoryLevel0Code;

    @Column(nullable = true, name = "DISP_SEQ_NUM_LVL0_CAT")
    private Integer categoryLevel0SequencenNum;

    @Column(nullable = true, name = "PROD_NLS_LVL0_NAME1")
    private String categoryLevel0Name1;

    @Column(nullable = true, name = "PROD_NLS_LVL0_NAME2")
    private String categoryLevel0Name2;

    @Column(nullable = true, name = "PROD_NLS_LVL0_NAME3")
    private String categoryLevel0Name3;

    @Column(nullable = true, name = "FUND_CAT_LVL1_CDE")
    private String categoryLevel1Code;

    @Column(nullable = true, name = "DISP_SEQ_NUM_LVL1_CAT")
    private Integer categoryLevel1SequencenNum;

    @Column(nullable = true, name = "PROD_NLS_LVL1_NAME1")
    private String categoryLevel1Name1;

    @Column(nullable = true, name = "PROD_NLS_LVL1_NAME2")
    private String categoryLevel1Name2;

    @Column(nullable = true, name = "PROD_NLS_LVL1_NAME3")
    private String categoryLevel1Name3;

    @Column(nullable = true, name = "CCY_PROD_TRADE_CDE", columnDefinition = "char")
    private String ccyProdTradeCde;

    @Column(nullable = true, name = "FUND_RTAIN_MIN_AMT")
    private BigDecimal fundRtainMinAmt;

    @Column(nullable = true, name = "FUND_SW_IN_MIN_AMT")
    private BigDecimal fundSwInMinAmt;

    @Column(nullable = true, name = "ALLOW_SELL_MIP_PROD_IND", columnDefinition = "char")
    private String allowSellMipProdInd;

    @Column(nullable = true, name = "ALLOW_REGU_CNTB_IND", columnDefinition = "char")
    private String AllowReguCntbInd;

    @Column(nullable = false, name = "CCY_PROD_CDE", columnDefinition = "char")
    private String currency;

    @Column(nullable = true, name = "RISK_LVL_CDE", columnDefinition = "char")
    private String riskLvlCde;


    @Column(nullable = true, name = "INVST_INCRM_MIN_AMT")
    private BigDecimal invstIncrmMinAmt;

    

    @Column(nullable = true, name = "SHARE_CLASS_TYPE_TEXT")
    private String classType;

    @Column(nullable = true, name = "PROD_NAV_PRC_AMT")
    private BigDecimal dayEndNAV;

    @Column(nullable = true, name = "PROD_NAV_CHRG_AMT")
    private BigDecimal changeAmountNAV;

    @Column(nullable = true, name = "ASET_SURVY_NET_AMT", insertable = false, updatable = false)
    private BigDecimal assetsUnderManagement;

    @Column(nullable = true, name = "ASET_SHARE_CLASS_NET_AMT")
    private Long totalNetAsset;

    @Column(nullable = true, name = "PROD_OVRL_RATENG_NUM")
    private Integer ratingOverall;

    @Column(nullable = true, name = "FUND_MGMT_EXPN_RATE")
    private BigDecimal mer;

    @Column(nullable = true, name = "YIELD_1YR_PCT")
    private BigDecimal yield1Yr;

    @Column(nullable = true, name = "PROD_INCPT_DT", columnDefinition = "Date")
    private Date inceptionDate;

    @Column(nullable = true, name = "PROD_TRNVR_RATE")
    private BigDecimal turnoverRatio;

    @Column(nullable = true, name = "RTRN_STD_DVIAT_3YR_NUM")
    private BigDecimal stdDev3Yr;

    @Column(nullable = true, name = "PROD_STYL_BOX_NUM")
    private Integer equityStyle;

    @Column(nullable = true, name = "INCM_FIX_STYL_BOX_NUM")
    private Integer fixIncomestyle;

    @Column(nullable = true, name = "PROD_OVRL_RATENG_DEDUC_TAX_NUM")
    private Integer taxAdjustedRating;

    @Column(nullable = true, name = "CREDIT_QLTY_AVG_NUM")
    private Integer averageCreditQuality;

    @Column(nullable = true, name = "RANK_QTL_1YR_NUM")
    private BigDecimal rank1Yr;

    @Column(nullable = true, name = "RTRN_SINCE_INCPT_AMT")
    private BigDecimal returnSinceInception;

    @Column(nullable = true, name = "RTRN_YTD_AMT")
    private BigDecimal returnYTD;

    @Column(nullable = true, name = "RTRN_1YR_BFORE_AMT")
    private BigDecimal year1;

    @Column(nullable = true, name = "RTRN_2YR_BFORE_AMT")
    private BigDecimal year2;

    @Column(nullable = true, name = "RTRN_3YR_BFORE_AMT")
    private BigDecimal year3;

    @Column(nullable = true, name = "RTRN_4YR_BFORE_AMT")
    private BigDecimal year4;

    @Column(nullable = true, name = "RTRN_5YR_BFORE_AMT")
    private BigDecimal year5;

    @Column(nullable = true, name = "BETA_VALUE_1YR_NUM")
    private BigDecimal beta1;

    @Column(nullable = true, name = "RTRN_STD_DVIAT_1YR_NUM")
    private BigDecimal stdDev1;

    @Column(nullable = true, name = "ALPHA_VALUE_1YR_NUM")
    private BigDecimal alpha1;

    @Column(nullable = true, name = "SHRP_1YR_RATE")
    private BigDecimal sharpeRatio1;

    @Column(nullable = true, name = "PROD_MVMT_INDEX_1YR_PCT")
    private BigDecimal rSquared1;

    @Column(nullable = true, name = "BETA_VALUE_3YR_NUM")
    private BigDecimal beta3;

    @Column(nullable = true, name = "ALPHA_VALUE_3YR_NUM")
    private BigDecimal alpha3;

    @Column(nullable = true, name = "SHRP_3YR_RATE")
    private BigDecimal sharpeRatio3;

    @Column(nullable = true, name = "PROD_MVMT_INDEX_3YR_PCT")
    private BigDecimal rSquared3;

    @Column(nullable = true, name = "BETA_VALUE_5YR_NUM")
    private BigDecimal beta5;

    @Column(nullable = true, name = "RTRN_STD_DVIAT_5YR_NUM")
    private BigDecimal stdDev5;

    @Column(nullable = true, name = "ALPHA_VALUE_5YR_NUM")
    private BigDecimal alpha5;

    @Column(nullable = true, name = "SHRP_5YR_RATE")
    private BigDecimal sharpeRatio5;

    @Column(nullable = true, name = "PROD_MVMT_INDEX_5YR_PCT")
    private BigDecimal rSquared5;

    @Column(nullable = true, name = "BETA_VALUE_10YR_NUM")
    private BigDecimal beta10;

    @Column(nullable = true, name = "RTRN_STD_DVIAT_10YR_NUM")
    private BigDecimal stdDev10;

    @Column(nullable = true, name = "ALPHA_VALUE_10YR_NUM")
    private BigDecimal alpha10;

    @Column(nullable = true, name = "SHRP_10YR_RATE")
    private BigDecimal sharpeRatio10;

    @Column(nullable = true, name = "PROD_MVMT_INDEX_10YR_PCT")
    private BigDecimal rSquared10;

    @Column(nullable = true, name = "INVST_INIT_MIN_AMT")
    private BigDecimal minInitInvst;

    @Column(nullable = true, name = "INVST_SUBQ_MIN_AMT")
    private BigDecimal minSubqInvst;

    @Column(nullable = true, name = "hhhh_INVST_INIT_MIN_AMT")
    private BigDecimal hhhhMinInitInvst;

    @Column(nullable = true, name = "hhhh_INVST_SUBQ_MIN_AMT")
    private BigDecimal hhhhMinSubqInvst;

    @Column(nullable = true, name = "CCY_PUCHASE")
    private String purchaseCcy;

    @Column(nullable = true, name = "REGIS_RATEIRE_SAVNG_PLAN_IND", columnDefinition = "char")
    private String rrspEligibility;

    @Column(nullable = false, name = "USER_UPDT_NUM")
    private String updatedBy;

    @Column(nullable = false, name = "REC_UPDT_DT_TM")
    private Timestamp updatedOn;

    @Column(name = "FUND_DATA_DATE", columnDefinition = "date")
    private Date asOfDate;

    // add the fields from fundCriteriaKeyMapper
    @Column(name = "MEAN_VALUE_5YR_AMT")
    private BigDecimal mean5Yr;

    @Column(nullable = false, name = "TRACKINGERROR1YR", columnDefinition = "number")
    private Float trackingError1Yr;

    @Column(name = "RSK_3YR")
    private String risk3Yr;

    @Column(nullable = false, name = "MARKETPRICECHANGE", columnDefinition = "number")
    private Float marketPriceChange;

    @Column(name = "FREQ_DIV_DISTB_TEXT")
    private String distributionFrequency;

    // @Column(name = "ASET_SHARE_CLASS_NET_AMT")
    // private Long shareClassNetAssets;

    @Column(name = "RANK_QTL_3YR_NUM")
    private BigDecimal rank3Yr;

    @Column(name = "RANK_QTL_5YR_NUM")
    private BigDecimal rank5Yr;

    @Column(name = "RANK_QTL_10YR_NUM")
    private BigDecimal rank10Yr;

    @Column(name = "INVST_IRA_MIN_AMT")
    private Long minimumIRA;

    // add the fields from V_UT_RETURNS
    @Column(name = "RTRN_1_DAY_AMT")
    private BigDecimal return1day;

    // monthly
    @Column(name = "RTRN_1MO_AMT")
    private BigDecimal return1mth;

    @Column(name = "RTRN_3MO_AMT")
    private BigDecimal return3mth;

    @Column(name = "RTRN_6MO_AMT")
    private BigDecimal return6mth;

    @Column(name = "RTRN_1YR_AMT")
    private BigDecimal return1yr;

    @Column(name = "RTRN_3YR_AMT")
    private BigDecimal return3yr;

    @Column(name = "RTRN_5YR_AMT")
    private BigDecimal return5yr;

    @Column(name = "RTRN_10YR_AMT")
    private BigDecimal return10yr;

    @Column(nullable = true, name = "PROD_SHRT_PLL_NAME", length = 80)
    private String productShortPrimaryLanguageName;

    @Column(name = "PAY_CASH_DIV_IND",columnDefinition = "char")
    private String payCashDivInd;

    @Column(nullable = true, name = "PROD_SHRT_SLL_NAME", length = 80)
    private String productShortSecondLanguageName;

    @Column(nullable = true, name = "PROD_SHRT_NAME", length = 80)
    private String productShortName;

    @Column(name = "CCY_ASOF_REP")
    private String ccyAsofRep;

    @Column(name = "CHRG_OG_ANNL_AMT")
    private BigDecimal annualReportOngoingCharge;

    @Column(name = "ACTL_MGMT_FEE")
    private BigDecimal actualManagementFee;

    @Column(name = "ALLOW_SW_OUT_PROD_IND")
    private String allowSwOutProdInd;

    @Column(name = "ALLOW_SW_IN_PROD_IND")
    private String allowSwInProdInd;

    @Column(name = "ALLOW_TRAD_PROD_IND")
    private String allowTradeProdInd;

    @Column(name = "PROD_TAX_FREE_WRAP_ACT_STA_CDE")
    private String prodTaxFreeWrapActStaCde;

    @Column(name = "RATING_DATE", columnDefinition = "Date")
    private Date ratingDate;

    @Column(name = "MONTH_ENDDATE", columnDefinition = "Date")
    private Date monthEndDate;

    @Column(name = "ENDDATE_YEARRISK", columnDefinition = "Date")
    private Date endDateYearRisk;

    @Column(name = "ENDDATE_RISKLVLCDE", columnDefinition = "Date")
    private Date endDateRiskLvlCde;

    @Column(name = "DISP_SEQ_NUM_FAM")
    private Integer familySequencenNum;

    @Column(name = "DISP_SEQ_NUM_CAT")
    private Integer categorySequencenNum;

    @Column(name = "DISP_SEQ_NUM_ACQN")
    private Integer averageCreditQualityNum;

    @Column(name = "DISP_SEQ_NUM_DF")
    private Integer distributionFrequencyNum;

    @Column(nullable = true, name = "NET_EXPENSE_RATIO")
    private BigDecimal expenseRatio;

    @Column(name = "LOAN_PROD_OD_MRGN_PCT")
    private BigDecimal loanProdOdMrgnPct; // The Margin Ratio

    @Column(name = "CHRG_INIT_SALES_PCT")
    private BigDecimal chrgInitSalesPct; // Initial Charge Upon Subscription

    @Column(name = "ANN_MGMT_FEE_PCT")
    private BigDecimal annMgmtFeePct; // Annual Management Fee

    @Column(name = "PRI_SHARE_CLASS_IND")
    private Character priShareClassInd; // Primary Share Class Indicator

    @Column(name = "TOP_PERFORMERS_IND")
    private Character topPerformersInd; // Top Performers Indicator

    @Column(name = "Fund_Id")
    private String fundId;

    @Column(nullable = true, name = "AVG_CR_QLTY_NAME")
    private String averageCreditQualityName;

    @Column(name = "CCY_CDE", columnDefinition = "char")
    private String closingPrcCcy;

    @Column(name = "MSTAR_CCY_CDE", columnDefinition = "char")
    private String mstarCcyCde;

    @Column(nullable = true, name = "CRED_QLTY_BKDN_DATE", columnDefinition = "Date")
    private Date creditQualityBreakdownAsOfDate;

    @Column(name = "CCY_INVST_CDE")
    private String ccyInvstCde;

    @Column(nullable = true, name = "YLD_TO_MAT")
    private BigDecimal yieldToMaturity;

    @Column(nullable = true, name = "EFF_DRTN")
    private BigDecimal effectiveDuration;

    @Column(nullable = true, name = "CURR_YLD")
    private BigDecimal currentYield;

    // daily
    @Column(name = "RTRN_1MO_DPN")
    private BigDecimal return1mthDaily;

    @Column(name = "RTRN_3MO_DPN")
    private BigDecimal return3mthDaily;

    @Column(name = "RTRN_6MO_DPN")
    private BigDecimal return6mthDaily;

    @Column(name = "RTRN_YTD_DPN")
    private BigDecimal returnytdDaily;

    @Column(name = "RTRN_1YR_DPN")
    private BigDecimal return1yrDaily;

    @Column(name = "RTRN_3YR_DPN")
    private BigDecimal return3yrDaily;

    @Column(name = "RTRN_5YR_DPN")
    private BigDecimal return5yrDaily;

    @Column(name = "RTRN_10YR_DPN")
    private BigDecimal return10yrDaily;

    @Column(nullable = true, name = "RTRN_SINCE_INCPT_DPN")
    private BigDecimal returnSinceInceptionDaily;

    @Column(name = "PROD_TOP_SELL_RANK_NUM")
    private BigDecimal prodTopSellRankNum;

    @Column(name = "TOP_SELL_PROD_IND")
    private String topSellProdIndex;

    @Column(name = "PROD_LNCH_DT", columnDefinition = "Date")
    private Date prodLaunchDate;

    @Column(name = "MKT_INVST_CDE")
    private String investmentRegionCode;

    @Column(nullable = true, name = "PROD_NLS_NAME_INVST1")
    private String investmentRegionName1;

    @Column(nullable = true, name = "PROD_NLS_NAME_INVST2")
    private String investmentRegionName2;

    @Column(nullable = true, name = "PROD_NLS_NAME_INVST3")
    private String investmentRegionName3;

    @Column(name = "DISP_SEQ_NUM_INVST")
    private Integer investmentRegionSequencenNum;

    @Column(name = "LIST_PROD_CDE")
    private String listProdCode;

    @Column(name = "LIST_PROD_TYPE")
    private String listProdType;

    @Column(nullable = true, name = "BOND_HOLD_NUM")
    private BigDecimal numberOfBondHoldings;

    @Column(nullable = true, name = "STOCK_HOLD_NUM")
    private BigDecimal numberOfStockHoldings;

    // PortfolioDate - Portfolio Statistics (Most Recent Port)
    @Column(nullable = true, name = "HOLD_ALLOC_PORTF_DATE", columnDefinition = "Date")
    private Date holdingAllocationPortfolioDate;

    @Column(nullable = true, name = "KIID_ONGOING_CHARGE")
    private BigDecimal kiidOngoingCharge;

    @Column(nullable = true, name = "KIID_ONGOING_CHARGE_DATE", columnDefinition = "Date")
    private Date kiidOngoingChargeDate;

    @Column(name = "SHRE_CLS_NAME")
    private String fundShreClsName;

    @Column(name = "SHRE_CLS_NAME_PRI")
    private String fundShreClsNamePriLang;

    @Column(name = "SHRE_CLS_NAME_SEC")
    private String fundShreClsNameSecLang;

    @Column(name = "FUND_CLASS_CDE")
    private String fundClassCde;

    @Column(name = "AMCM_IND", columnDefinition = "char")
    private String amcmAuthorizeIndicator;

    @Column(name = "DEAL_NEXT_DT", columnDefinition = "Date")
    private Date nextDealDate;

    @Column(nullable = true, name = "INDEX_ID")
    private String indexId;

    @Column(nullable = true, name = "INDEX_NAME")
    private String indexName;

    @Column(nullable = true, name = "SURVY_NET_ASET_DATE", columnDefinition = "Date")
    private Date surveyedFundNetAssetsDate;

    @Column(nullable = true, name = "WEEK_RANGE_LOW_DATE", columnDefinition = "Date")
    private Date weekRangeLowDate;

    @Column(nullable = true, name = "WEEK_RANGE_HIGH_DATE", columnDefinition = "Date")
    private Date weekRangeHighDate;

    @Column(nullable = true, name = "RISK_FREE_RATE_NAME")
    private String riskFreeRateName;

    @Column(nullable = true, name = "RLTV_RISK_MEAS_INDEX_NAME")
    private String relativeRiskMeasuresIndexName;

    @Column(name = "PROD_STAT_CDE", columnDefinition = "char")
    private String prodStatCde;

    @Column(name = "ALLOW_BUY_PROD_IND", columnDefinition = "char")
    private String allowBuyProdInd;

    @Column(name = "ALLOW_SELL_PROD_IND", columnDefinition = "char")
    private String allowSellProdInd;

    @Column(name = "ANNL_RPT_Date", columnDefinition = "Date")
    private Date annualReportDate;

    @Column(name = "RESTR_ONLN_SCRIB_IND", columnDefinition = "char")
    private String restrOnlScribInd;

    @Column(name = "FUND_MKT_PRICE_AMT", columnDefinition = "number")
    private BigDecimal marketPrice;

    @Column(name = "PI_FUND_IND", columnDefinition = "char")
    private String piFundInd;

    @Column(name = "DE_AUTHFUND_IND", columnDefinition = "char")
    private String deAuthFundInd;

    @Column(name = "FUND_BID_CLOSE_PRC_AMT")
    private BigDecimal bidclosingPrc;

    @Column(name = "FUND_BID_ASK_PRICE_DT", columnDefinition = "Date")
    private Date bidPriceDate;

    @Column(name = "FUND_ASK_CLOSE_PRICE_AMT")
    private BigDecimal askclosingPrc;

    @Column(name = "UNDL_INDEX_CDE")
    private String undlIndexCde;

    @Column(name = "UNDL_INDEX_NAME")
    private String undlIndexName;

    @Column(name = "UNDL_INDEX_PLL_NAME")
    private String undlIndexPllName;

    @Column(name = "UNDL_INDEX_SLL_NAME")
    private String undlIndexSllName;

    @Column(name = "POPULAR_RANK_NUM")
    private Integer popularRankNum;

    @Column(name = "ESG_IND", columnDefinition = "char")
    private String esgInd;

    @Column(name = "CUT_OFF_TIME", columnDefinition = "Date")
    private Date cutOffTime;

    @Column(name = "IN_DEAL_BEF_DATE", columnDefinition = "Date")
    private Date inDealBefDate;

    @Column(name = "IN_SCRIB_STL_BEF_DATE", columnDefinition = "Date")
    private Date inScribStlBefDate;

    @Column(name = "IN_REDEMP_STL_BEF_DATE", columnDefinition = "Date")
    private Date inRedempStlBefDate;

    @Column(name = "IN_DEAL_AF_DATE", columnDefinition = "Date")
    private Date inDealAftDate;

    @Column(name = "IN_SCRIB_STL_AF_DATE", columnDefinition = "Date")
    private Date inScribStlAftDate;

    @Column(name = "IN_REDEMP_STL_AF_DATE", columnDefinition = "Date")
    private Date inRedempStlAftDate;

    @Column(name = "INTER_DATE", columnDefinition = "Date")
    private Date interfaceDate;

    @Column(name = "SI_FUND_CAT_CDE")
    private String siFundCategoryCode;
    
    @Column(name = "GBA_ACCT_TRDB", columnDefinition = "char")
    private String gbaAcctTrdb;

    @Column(name = "GNR_ACCT_TRDB", columnDefinition = "char")
    private String gnrAcctTrdb;
    
    @Column(name = "SHORT_LST_RPQ_LVL_NUM")
    private Integer shortLstRPQLvlNum;

    @Column(name = "WPB_PROD_IND", columnDefinition = "char")
    private String wpbProductInd;

    @Column(name = "CMB_PROD_IND", columnDefinition = "char")
    private String cmbProductInd;

    @Column(name = "RESTR_CMB_ONLN_SRCH_IND", columnDefinition = "char")
    private String restrCmbOnlSrchInd;


    @Transient
    private String fundName;

    @Column(name = "CURRENCY_ID")
    private String currencyId;
    
    @Column(name = "RETIRE_INVST_IND", columnDefinition = "char")
    private String retireInvstInd;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("productId", this.utProdInstmId.getProductId()).append("batchId", this.utProdInstmId.getBatchId())
            .append("market", this.market).append("productType", this.productType)
            .append("productSubTypeCode", this.productSubTypeCode).append("symbol", this.symbol)
            .append("performanceId", this.performanceId).append("fundserviceId", this.fundserviceId)
            .append("prodName", this.prodName).append("prodPllName", this.prodPllName).append("familyCode", this.familyCode)
            .append("familyName1", this.familyName1).append("familyName2", this.familyName2).append("familyName3", this.familyName3)
            .append("categoryCode", this.categoryCode).append("categoryName1", this.categoryName1)
            .append("categoryName2", this.categoryName2).append("categoryName3", this.categoryName3)
            .append("categoryLevel0Code", this.categoryLevel0Code)
            .append("categoryLevel0SequencenNum", this.categoryLevel0SequencenNum)
            .append("categoryLevel0Name1", this.categoryLevel0Name1).append("categoryLevel0Name2", this.categoryLevel0Name2)
            .append("categoryLevel0Name3", this.categoryLevel0Name3).append("categoryLevel1Code", this.categoryLevel1Code)
            .append("categoryLevel1SequencenNum", this.categoryLevel1SequencenNum)
            .append("categoryLevel1Name1", this.categoryLevel1Name1).append("categoryLevel1Name2", this.categoryLevel1Name2)
            .append("categoryLevel1Name3", this.categoryLevel1Name3).append("ccyProdTradeCde", this.ccyProdTradeCde)
            .append("fundRtainMinAmt", this.fundRtainMinAmt).append("fundSwInMinAmt", this.fundSwInMinAmt)
            .append("allowSellMipProdInd", this.allowSellMipProdInd).append("AllowReguCntbInd", this.AllowReguCntbInd)
            .append("currency", this.currency).append("riskLvlCde", this.riskLvlCde)
            .append("familySequencenNum", this.familySequencenNum).append("categorySequencenNum", this.categorySequencenNum)
            .append("averageCreditQualityNum", this.averageCreditQualityNum)
            .append("distributionFrequencyNum", this.distributionFrequencyNum)
            // .append("invstIncrmMinAmt",
            // this.invstIncrmMinAmt).append("invstMipincrmMinAmt",
            // this.invstMipincrmMinAmt)
            .append("ClassType", this.classType).append("dayEndNAV", this.dayEndNAV).append("changeAmountNAV", this.changeAmountNAV)
            .append("assetsUnderManagement", this.assetsUnderManagement).append("totalNetAsset", this.totalNetAsset)
            .append("ratingOverall", this.ratingOverall).append("mer", this.mer).append("yield1Yr", this.yield1Yr)
            .append("inceptionDate", this.inceptionDate).append("turnoverRatio", this.turnoverRatio)
            .append("stdDev3Yr", this.stdDev3Yr).append("equityStyle", this.equityStyle)
            .append("fixIncomestyle", this.fixIncomestyle).append("taxAdjustedRating", this.taxAdjustedRating)
            .append("averageCreditQuality", this.averageCreditQuality).append("rank1Yr", this.rank1Yr)
            .append("returnSinceInception", this.returnSinceInception).append("returnYTD", this.returnYTD)
            .append("year1", this.year1).append("year2", this.year2).append("year3", this.year3).append("year4", this.year4)
            .append("year5", this.year5).append("beta1", this.beta1).append("stdDev1", this.stdDev1).append("alpha1", this.alpha1)
            .append("sharpeRatio1", this.sharpeRatio1).append("rSquared1", this.rSquared1).append("beta3", this.beta3)
            .append("alpha3", this.alpha3).append("sharpeRatio3", this.sharpeRatio3).append("rSquared3", this.rSquared3)
            .append("beta5", this.beta5).append("stdDev5", this.stdDev5).append("alpha5", this.alpha5)
            .append("sharpeRatio5", this.sharpeRatio5).append("rSquared5", this.rSquared5).append("beta10", this.beta10)
            .append("stdDev10", this.stdDev10).append("alpha10", this.alpha10).append("sharpeRatio10", this.sharpeRatio10)
            .append("rSquared10", this.rSquared10).append("minInitInvst", this.minInitInvst)
            .append("minSubqInvst", this.minSubqInvst).append("rrspEligibility", this.rrspEligibility)
            .append("updatedBy", this.updatedBy).append("updatedOn", this.updatedOn).append("asOfDate", this.asOfDate)
            .append("mean5Yr", this.mean5Yr).append("trackingError1Yr", this.trackingError1Yr).append("risk3Yr", this.risk3Yr)
            .append("marketPriceChange", this.marketPriceChange).append("distributionFrequency", this.distributionFrequency)
            
            .append("rank3Yr", this.rank3Yr).append("rank5Yr", this.rank5Yr).append("rank10Yr", this.rank10Yr)
            .append("minimumIRA", this.minimumIRA).append("return1day", this.return1day).append("return1mth", this.return1mth)
            .append("return3mth", this.return3mth).append("return6mth", this.return6mth).append("return1yr", this.return1yr)
            .append("return3yr", this.return3yr).append("return5yr", this.return5yr).append("return10yr", this.return10yr)
            .append("expenseRatio", this.expenseRatio).append("loanProdOdMrgnPct", this.loanProdOdMrgnPct)
            .append("chrgInitSalesPct", this.chrgInitSalesPct).append("annMgmtFeePct", this.annMgmtFeePct)
            .append("priShareClassInd", this.priShareClassInd).append("topPerformersInd", this.topPerformersInd)
            .append("fundId", this.fundId).append("averageCreditQualityName", this.averageCreditQualityName)
            .append("closingPrcCcy", this.closingPrcCcy).append("mstarCcyCde", this.mstarCcyCde)
            .append("creditQualityBreakdownAsOfDate", this.creditQualityBreakdownAsOfDate).append("currentYield", this.currentYield)
            .append("returnSinceInceptionDaily", this.returnSinceInceptionDaily)
            .append("prodTopSellRankNum", this.prodTopSellRankNum).append("topSellProdIndex", this.topSellProdIndex)
            .append("prodLaunchDate", this.prodLaunchDate).append("fundClassCde", this.fundClassCde)
            .append("amcmAuthorizeIndicator", this.amcmAuthorizeIndicator).append("nextDealDate", this.nextDealDate)
            .append("indexId", this.indexId).append("indexName", this.indexName)
            .append("surveyedFundNetAssetsDate", this.surveyedFundNetAssetsDate).append("weekRangeLowDate", this.weekRangeLowDate)
            .append("weekRangeHighDate", this.weekRangeHighDate).append("riskFreeRateName", this.riskFreeRateName)
            .append("relativeRiskMeasuresIndexName", this.relativeRiskMeasuresIndexName).append("prodStatCde", this.prodStatCde)
            .append("allowBuyProdInd", this.allowBuyProdInd).append("allowSellProdInd", this.allowSellProdInd)
            .append("restrOnlScribInd", this.restrOnlScribInd).append("marketPrice", this.marketPrice)
            .append("piFundInd", this.piFundInd).append("deAuthFundInd", this.deAuthFundInd)
            .append("bidclosingPrc", this.bidclosingPrc).append("bidPriceDate", this.bidPriceDate)
            .append("askclosingPrc", this.askclosingPrc).append("undlIndexCde", this.undlIndexCde)
            .append("undlIndexName", this.undlIndexName).append("undlIndexPllName", this.undlIndexPllName)
            .append("undlIndexSllName", this.undlIndexSllName).append("popularRankNum", this.popularRankNum)
            .append("shortLstRPQLvlNum", this.shortLstRPQLvlNum).append("gnrAcctTrdb", this.gnrAcctTrdb)
            .append("esgInd", this.esgInd)
            .append("cmbProductInd", this.cmbProductInd)
            .append("wpbProductInd", this.wpbProductInd)
            .append("restrCmbOnlSrchInd", this.restrCmbOnlSrchInd)
            .append("currencyId", this.currencyId)
            .append("retireInvstInd", this.retireInvstInd).toString();
    }


    
    public String getFundName() {
        return this.fundName;
    }


    
    public void setFundName(final String fundName) {
        this.fundName = fundName;
    }


    
    public String getCurrencyId() {
        return this.currencyId;
    }

    
    public void setCurrencyId(final String currencyId) {
        this.currencyId = currencyId;
    }


    
    public UtProdInstmId getUtProdInstmId() {
        return this.utProdInstmId;
    }

    
    public void setUtProdInstmId(final UtProdInstmId utProdInstmId) {
        this.utProdInstmId = utProdInstmId;
    }

    
    public String getMarket() {
        return this.market;
    }

    
    public void setMarket(final String market) {
        this.market = market;
    }

    
    public String getProductType() {
        return this.productType;
    }

    
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    
    public String getProductSubTypeCode() {
        return this.productSubTypeCode;
    }


    
    public void setProductSubTypeCode(final String productSubTypeCode) {
        this.productSubTypeCode = productSubTypeCode;
    }


    
    public String getSymbol() {
        return this.symbol;
    }

    
    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    
    public String getPerformanceId() {
        return this.performanceId;
    }

    
    public void setPerformanceId(final String performanceId) {
        this.performanceId = performanceId;
    }

    
    public String getFundserviceId() {
        return this.fundserviceId;
    }

    
    public void setFundserviceId(final String fundserviceId) {
        this.fundserviceId = fundserviceId;
    }

    
    public String getProdName() {
        return this.prodName;
    }

    
    public void setProdName(final String prodName) {
        this.prodName = prodName;
    }

    
    public String getProdPllName() {
        return this.prodPllName;
    }

    
    public void setProdPllName(final String prodPllName) {
        this.prodPllName = prodPllName;
    }

    
    public String getProdSllName() {
        return this.prodSllName;
    }

    
    public void setProdSllName(final String prodSllName) {
        this.prodSllName = prodSllName;
    }

    
    public String getFamilyCode() {
        return this.familyCode;
    }

    
    public void setFamilyCode(final String familyCode) {
        this.familyCode = familyCode;
    }


    
    public String getCcyProdTradeCde() {
        return this.ccyProdTradeCde;
    }

    
    public void setCcyProdTradeCde(final String ccyProdTradeCde) {
        this.ccyProdTradeCde = ccyProdTradeCde;
    }

    
    public BigDecimal getFundRtainMinAmt() {
        return this.fundRtainMinAmt;
    }

    
    public void setFundRtainMinAmt(final BigDecimal fundRtainMinAmt) {
        this.fundRtainMinAmt = fundRtainMinAmt;
    }

    
    public BigDecimal getFundSwInMinAmt() {
        return this.fundSwInMinAmt;
    }

    
    public void setFundSwInMinAmt(final BigDecimal fundSwInMinAmt) {
        this.fundSwInMinAmt = fundSwInMinAmt;
    }

    
    public String getAllowSellMipProdInd() {
        return this.allowSellMipProdInd;
    }

    
    public void setAllowSellMipProdInd(final String allowSellMipProdInd) {
        this.allowSellMipProdInd = allowSellMipProdInd;
    }

    
    public String getAllowReguCntbInd() {
        return this.AllowReguCntbInd;
    }

    
    public void setAllowReguCntbInd(final String allowReguCntbInd) {
        this.AllowReguCntbInd = allowReguCntbInd;
    }

    
    public String getCurrency() {
        return this.currency;
    }

    
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    
    public String getRiskLvlCde() {
        return this.riskLvlCde;
    }

    
    public void setRiskLvlCde(final String riskLvlCde) {
        this.riskLvlCde = riskLvlCde;
    }


    
    public BigDecimal getInvstIncrmMinAmt() {
        return this.invstIncrmMinAmt;
    }


    
    public void setInvstIncrmMinAmt(final BigDecimal invstIncrmMinAmt) {
        this.invstIncrmMinAmt = invstIncrmMinAmt;
    }


    
    public BigDecimal getDayEndNAV() {
        return this.dayEndNAV;
    }

    
    public String getClassType() {
        return this.classType;
    }


    
    public void setClassType(final String classType) {
        this.classType = classType;
    }


    
    public void setDayEndNAV(final BigDecimal dayEndNAV) {
        this.dayEndNAV = dayEndNAV;
    }

    
    public BigDecimal getChangeAmountNAV() {
        return this.changeAmountNAV;
    }

    
    public void setChangeAmountNAV(final BigDecimal changeAmountNAV) {
        this.changeAmountNAV = changeAmountNAV;
    }

    
    public BigDecimal getAssetsUnderManagement() {
        return this.assetsUnderManagement;
    }

    
    public void setAssetsUnderManagement(final BigDecimal assetsUnderManagement) {
        this.assetsUnderManagement = assetsUnderManagement;
    }

    
    public Long getTotalNetAsset() {
        return this.totalNetAsset;
    }

    
    public void setTotalNetAsset(final Long totalNetAsset) {
        this.totalNetAsset = totalNetAsset;
    }

    
    public Integer getRatingOverall() {
        return this.ratingOverall;
    }

    
    public void setRatingOverall(final Integer ratingOverall) {
        this.ratingOverall = ratingOverall;
    }

    
    public BigDecimal getMer() {
        return this.mer;
    }

    
    public void setMer(final BigDecimal mer) {
        this.mer = mer;
    }

    
    public BigDecimal getYield1Yr() {
        return this.yield1Yr;
    }

    
    public void setYield1Yr(final BigDecimal yield1Yr) {
        this.yield1Yr = yield1Yr;
    }

    
    public Date getInceptionDate() {
        return this.inceptionDate;
    }

    
    public void setInceptionDate(final Date inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    
    public BigDecimal getTurnoverRatio() {
        return this.turnoverRatio;
    }

    
    public void setTurnoverRatio(final BigDecimal turnoverRatio) {
        this.turnoverRatio = turnoverRatio;
    }

    
    public BigDecimal getStdDev3Yr() {
        return this.stdDev3Yr;
    }

    
    public void setStdDev3Yr(final BigDecimal stdDev3Yr) {
        this.stdDev3Yr = stdDev3Yr;
    }

    
    public Integer getEquityStyle() {
        return this.equityStyle;
    }

    
    public void setEquityStyle(final Integer equityStyle) {
        this.equityStyle = equityStyle;
    }

    
    public Integer getFixIncomestyle() {
        return this.fixIncomestyle;
    }

    
    public void setFixIncomestyle(final Integer fixIncomestyle) {
        this.fixIncomestyle = fixIncomestyle;
    }

    
    public Integer getTaxAdjustedRating() {
        return this.taxAdjustedRating;
    }

    
    public void setTaxAdjustedRating(final Integer taxAdjustedRating) {
        this.taxAdjustedRating = taxAdjustedRating;
    }

    
    public Integer getAverageCreditQuality() {
        return this.averageCreditQuality;
    }

    
    public void setAverageCreditQuality(final Integer averageCreditQuality) {
        this.averageCreditQuality = averageCreditQuality;
    }

    
    public BigDecimal getRank1Yr() {
        return this.rank1Yr;
    }

    
    public void setRank1Yr(final BigDecimal rank1Yr) {
        this.rank1Yr = rank1Yr;
    }

    
    public BigDecimal getReturnSinceInception() {
        return this.returnSinceInception;
    }

    
    public void setReturnSinceInception(final BigDecimal returnSinceInception) {
        this.returnSinceInception = returnSinceInception;
    }

    
    public BigDecimal getReturnYTD() {
        return this.returnYTD;
    }

    
    public void setReturnYTD(final BigDecimal returnYTD) {
        this.returnYTD = returnYTD;
    }

    
    public BigDecimal getYear1() {
        return this.year1;
    }

    
    public void setYear1(final BigDecimal year1) {
        this.year1 = year1;
    }

    
    public BigDecimal getYear2() {
        return this.year2;
    }

    
    public void setYear2(final BigDecimal year2) {
        this.year2 = year2;
    }

    
    public BigDecimal getYear3() {
        return this.year3;
    }

    
    public void setYear3(final BigDecimal year3) {
        this.year3 = year3;
    }

    
    public BigDecimal getYear4() {
        return this.year4;
    }

    
    public void setYear4(final BigDecimal year4) {
        this.year4 = year4;
    }

    
    public BigDecimal getYear5() {
        return this.year5;
    }

    
    public void setYear5(final BigDecimal year5) {
        this.year5 = year5;
    }

    
    public BigDecimal getBeta1() {
        return this.beta1;
    }

    
    public void setBeta1(final BigDecimal beta1) {
        this.beta1 = beta1;
    }

    
    public BigDecimal getStdDev1() {
        return this.stdDev1;
    }

    
    public void setStdDev1(final BigDecimal stdDev1) {
        this.stdDev1 = stdDev1;
    }

    
    public BigDecimal getAlpha1() {
        return this.alpha1;
    }

    
    public void setAlpha1(final BigDecimal alpha1) {
        this.alpha1 = alpha1;
    }

    
    public BigDecimal getSharpeRatio1() {
        return this.sharpeRatio1;
    }

    
    public void setSharpeRatio1(final BigDecimal sharpeRatio1) {
        this.sharpeRatio1 = sharpeRatio1;
    }

    
    public BigDecimal getrSquared1() {
        return this.rSquared1;
    }

    
    public void setrSquared1(final BigDecimal rSquared1) {
        this.rSquared1 = rSquared1;
    }

    
    public BigDecimal getBeta3() {
        return this.beta3;
    }

    
    public void setBeta3(final BigDecimal beta3) {
        this.beta3 = beta3;
    }

    
    public BigDecimal getAlpha3() {
        return this.alpha3;
    }

    
    public void setAlpha3(final BigDecimal alpha3) {
        this.alpha3 = alpha3;
    }

    
    public BigDecimal getSharpeRatio3() {
        return this.sharpeRatio3;
    }

    
    public void setSharpeRatio3(final BigDecimal sharpeRatio3) {
        this.sharpeRatio3 = sharpeRatio3;
    }

    
    public BigDecimal getrSquared3() {
        return this.rSquared3;
    }

    
    public void setrSquared3(final BigDecimal rSquared3) {
        this.rSquared3 = rSquared3;
    }

    
    public BigDecimal getBeta5() {
        return this.beta5;
    }

    
    public void setBeta5(final BigDecimal beta5) {
        this.beta5 = beta5;
    }

    
    public BigDecimal getStdDev5() {
        return this.stdDev5;
    }

    
    public void setStdDev5(final BigDecimal stdDev5) {
        this.stdDev5 = stdDev5;
    }

    
    public BigDecimal getAlpha5() {
        return this.alpha5;
    }

    
    public void setAlpha5(final BigDecimal alpha5) {
        this.alpha5 = alpha5;
    }

    
    public BigDecimal getSharpeRatio5() {
        return this.sharpeRatio5;
    }

    
    public void setSharpeRatio5(final BigDecimal sharpeRatio5) {
        this.sharpeRatio5 = sharpeRatio5;
    }

    
    public BigDecimal getrSquared5() {
        return this.rSquared5;
    }

    
    public void setrSquared5(final BigDecimal rSquared5) {
        this.rSquared5 = rSquared5;
    }

    
    public BigDecimal getBeta10() {
        return this.beta10;
    }

    
    public void setBeta10(final BigDecimal beta10) {
        this.beta10 = beta10;
    }

    
    public BigDecimal getStdDev10() {
        return this.stdDev10;
    }

    
    public void setStdDev10(final BigDecimal stdDev10) {
        this.stdDev10 = stdDev10;
    }

    
    public BigDecimal getAlpha10() {
        return this.alpha10;
    }

    
    public void setAlpha10(final BigDecimal alpha10) {
        this.alpha10 = alpha10;
    }

    
    public BigDecimal getSharpeRatio10() {
        return this.sharpeRatio10;
    }

    
    public void setSharpeRatio10(final BigDecimal sharpeRatio10) {
        this.sharpeRatio10 = sharpeRatio10;
    }

    
    public BigDecimal getrSquared10() {
        return this.rSquared10;
    }

    
    public void setrSquared10(final BigDecimal rSquared10) {
        this.rSquared10 = rSquared10;
    }

    
    public String getRrspEligibility() {
        return this.rrspEligibility;
    }

    
    public void setRrspEligibility(final String rrspEligibility) {
        this.rrspEligibility = rrspEligibility;
    }

    
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    
    public Timestamp getUpdatedOn() {
        return this.updatedOn;
    }

    
    public void setUpdatedOn(final Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    
    public BigDecimal getMinInitInvst() {
        return this.minInitInvst;
    }

    
    public void setMinInitInvst(final BigDecimal minInitInvst) {
        this.minInitInvst = minInitInvst;
    }

    
    public BigDecimal getMinSubqInvst() {
        return this.minSubqInvst;
    }

    
    public void setMinSubqInvst(final BigDecimal minSubqInvst) {
        this.minSubqInvst = minSubqInvst;
    }

    
    public Date getAsOfDate() {
        return this.asOfDate;
    }

    
    public void setAsOfDate(final Date asOfDate) {
        this.asOfDate = asOfDate;
    }

    
    public String getFamilyName1() {
        return this.familyName1;
    }

    
    public void setFamilyName1(final String familyName1) {
        this.familyName1 = familyName1;
    }

    
    public String getFamilyName2() {
        return this.familyName2;
    }

    
    public void setFamilyName2(final String familyName2) {
        this.familyName2 = familyName2;
    }

    
    public String getFamilyName3() {
        return this.familyName3;
    }

    
    public void setFamilyName3(final String familyName3) {
        this.familyName3 = familyName3;
    }

    
    public String getCategoryCode() {
        return this.categoryCode;
    }

    
    public void setCategoryCode(final String categoryCode) {
        this.categoryCode = categoryCode;
    }

    
    public String getCategoryName1() {
        return this.categoryName1;
    }

    
    public void setCategoryName1(final String categoryName1) {
        this.categoryName1 = categoryName1;
    }

    
    public String getCategoryName2() {
        return this.categoryName2;
    }

    
    public void setCategoryName2(final String categoryName2) {
        this.categoryName2 = categoryName2;
    }

    
    public String getCategoryName3() {
        return this.categoryName3;
    }

    
    public void setCategoryName3(final String categoryName3) {
        this.categoryName3 = categoryName3;
    }

    
    public String getCategoryLevel0Code() {
        return this.categoryLevel0Code;
    }


    
    public void setCategoryLevel0Code(final String categoryLevel0Code) {
        this.categoryLevel0Code = categoryLevel0Code;
    }


    
    public Integer getCategoryLevel0SequencenNum() {
        return this.categoryLevel0SequencenNum;
    }


    
    public void setCategoryLevel0SequencenNum(final Integer categoryLevel0SequencenNum) {
        this.categoryLevel0SequencenNum = categoryLevel0SequencenNum;
    }


    
    public String getCategoryLevel0Name1() {
        return this.categoryLevel0Name1;
    }


    
    public void setCategoryLevel0Name1(final String categoryLevel0Name1) {
        this.categoryLevel0Name1 = categoryLevel0Name1;
    }


    
    public String getCategoryLevel0Name2() {
        return this.categoryLevel0Name2;
    }


    
    public void setCategoryLevel0Name2(final String categoryLevel0Name2) {
        this.categoryLevel0Name2 = categoryLevel0Name2;
    }


    
    public String getCategoryLevel0Name3() {
        return this.categoryLevel0Name3;
    }


    
    public void setCategoryLevel0Name3(final String categoryLevel0Name3) {
        this.categoryLevel0Name3 = categoryLevel0Name3;
    }


    
    public String getCategoryLevel1Code() {
        return this.categoryLevel1Code;
    }


    
    public void setCategoryLevel1Code(final String categoryLevel1Code) {
        this.categoryLevel1Code = categoryLevel1Code;
    }


    
    public Integer getCategoryLevel1SequencenNum() {
        return this.categoryLevel1SequencenNum;
    }


    
    public void setCategoryLevel1SequencenNum(final Integer categoryLevel1SequencenNum) {
        this.categoryLevel1SequencenNum = categoryLevel1SequencenNum;
    }


    
    public String getCategoryLevel1Name1() {
        return this.categoryLevel1Name1;
    }


    
    public void setCategoryLevel1Name1(final String categoryLevel1Name1) {
        this.categoryLevel1Name1 = categoryLevel1Name1;
    }


    
    public String getCategoryLevel1Name2() {
        return this.categoryLevel1Name2;
    }


    
    public void setCategoryLevel1Name2(final String categoryLevel1Name2) {
        this.categoryLevel1Name2 = categoryLevel1Name2;
    }


    
    public String getCategoryLevel1Name3() {
        return this.categoryLevel1Name3;
    }


    
    public void setCategoryLevel1Name3(final String categoryLevel1Name3) {
        this.categoryLevel1Name3 = categoryLevel1Name3;
    }


    
    public BigDecimal getMean5Yr() {
        return this.mean5Yr;
    }

    
    public void setMean5Yr(final BigDecimal mean5Yr) {
        this.mean5Yr = mean5Yr;
    }

    
    public Float getTrackingError1Yr() {
        return this.trackingError1Yr;
    }

    
    public void setTrackingError1Yr(final Float trackingError1Yr) {
        this.trackingError1Yr = trackingError1Yr;
    }

    
    public String getRisk3Yr() {
        return this.risk3Yr;
    }

    
    public void setRisk3Yr(final String risk3Yr) {
        this.risk3Yr = risk3Yr;
    }

    
    public Float getMarketPriceChange() {
        return this.marketPriceChange;
    }

    
    public void setMarketPriceChange(final Float marketPriceChange) {
        this.marketPriceChange = marketPriceChange;
    }

    
    public String getDistributionFrequency() {
        return this.distributionFrequency;
    }

    
    public void setDistributionFrequency(final String distributionFrequency) {
        this.distributionFrequency = distributionFrequency;
    }

    
    
    

    
    public BigDecimal getRank3Yr() {
        return this.rank3Yr;
    }

    
    public void setRank3Yr(final BigDecimal rank3Yr) {
        this.rank3Yr = rank3Yr;
    }

    
    public BigDecimal getRank5Yr() {
        return this.rank5Yr;
    }

    
    public void setRank5Yr(final BigDecimal rank5Yr) {
        this.rank5Yr = rank5Yr;
    }

    
    public BigDecimal getRank10Yr() {
        return this.rank10Yr;
    }

    
    public void setRank10Yr(final BigDecimal rank10Yr) {
        this.rank10Yr = rank10Yr;
    }

    
    public Long getMinimumIRA() {
        return this.minimumIRA;
    }

    
    public void setMinimumIRA(final Long minimumIRA) {
        this.minimumIRA = minimumIRA;
    }

    
    public BigDecimal getReturn1day() {
        return this.return1day;
    }

    
    public void setReturn1day(final BigDecimal return1day) {
        this.return1day = return1day;
    }

    
    public BigDecimal getReturn1mth() {
        return this.return1mth;
    }

    
    public void setReturn1mth(final BigDecimal return1mth) {
        this.return1mth = return1mth;
    }

    
    public BigDecimal getReturn3mth() {
        return this.return3mth;
    }

    
    public void setReturn3mth(final BigDecimal return3mth) {
        this.return3mth = return3mth;
    }

    
    public BigDecimal getReturn6mth() {
        return this.return6mth;
    }

    
    public void setReturn6mth(final BigDecimal return6mth) {
        this.return6mth = return6mth;
    }

    
    public BigDecimal getReturn1yr() {
        return this.return1yr;
    }

    
    public void setReturn1yr(final BigDecimal return1yr) {
        this.return1yr = return1yr;
    }

    
    public BigDecimal getReturn3yr() {
        return this.return3yr;
    }

    
    public void setReturn3yr(final BigDecimal return3yr) {
        this.return3yr = return3yr;
    }

    
    public BigDecimal getReturn5yr() {
        return this.return5yr;
    }

    
    public void setReturn5yr(final BigDecimal return5yr) {
        this.return5yr = return5yr;
    }

    
    public BigDecimal getReturn10yr() {
        return this.return10yr;
    }

    
    public void setReturn10yr(final BigDecimal return10yr) {
        this.return10yr = return10yr;
    }


    
    public BigDecimal gethhhhMinInitInvst() {
        return this.hhhhMinInitInvst;
    }


    
    public void sethhhhMinInitInvst(final BigDecimal hhhhMinInitInvst) {
        this.hhhhMinInitInvst = hhhhMinInitInvst;
    }

    
    public BigDecimal gethhhhMinSubqInvst() {
        return this.hhhhMinSubqInvst;
    }


    
    public void sethhhhMinSubqInvst(final BigDecimal hhhhMinSubqInvst) {
        this.hhhhMinSubqInvst = hhhhMinSubqInvst;
    }


    
    public String getPurchaseCcy() {
        return MorningStarUtil.trimCurrency(this.purchaseCcy);
    }


    
    public void setPurchaseCcy(final String purchaseCcy) {
        this.purchaseCcy = purchaseCcy;
    }


    
    public String getProductShortPrimaryLanguageName() {
        return this.productShortPrimaryLanguageName;
    }


    
    public void setProductShortPrimaryLanguageName(final String productShortPrimaryLanguageName) {
        this.productShortPrimaryLanguageName = productShortPrimaryLanguageName;
    }


    
    public String getProductShortSecondLanguageName() {
        return this.productShortSecondLanguageName;
    }


    
    public void setProductShortSecondLanguageName(final String productShortSecondLanguageName) {
        this.productShortSecondLanguageName = productShortSecondLanguageName;
    }


    
    public String getProductShortName() {
        return this.productShortName;
    }


    
    public void setProductShortName(final String productShortName) {
        this.productShortName = productShortName;
    }


    
    public String getCcyAsofRep() {
        return this.ccyAsofRep;
    }


    
    public void setCcyAsofRep(final String ccyAsofRep) {
        this.ccyAsofRep = ccyAsofRep;
    }


    
    public BigDecimal getAnnualReportOngoingCharge() {
        return this.annualReportOngoingCharge;
    }


    
    public void setAnnualReportOngoingCharge(final BigDecimal annualReportOngoingCharge) {
        this.annualReportOngoingCharge = annualReportOngoingCharge;
    }


    
    public BigDecimal getActualManagementFee() {
        return this.actualManagementFee;
    }


    
    public void setActualManagementFee(final BigDecimal actualManagementFee) {
        this.actualManagementFee = actualManagementFee;
    }


    
    public String getAllowSwOutProdInd() {
        return this.allowSwOutProdInd;
    }


    
    public void setAllowSwOutProdInd(final String allowSwOutProdInd) {
        this.allowSwOutProdInd = allowSwOutProdInd;
    }


    
    public String getAllowSwInProdInd() {
        return this.allowSwInProdInd;
    }


    
    public void setAllowSwInProdInd(final String allowSwInProdInd) {
        this.allowSwInProdInd = allowSwInProdInd;
    }

    
    public String getAllowTradeProdInd() {
        return this.allowTradeProdInd;
    }


    
    public void setAllowTradeProdInd(final String allowTradeProdInd) {
        this.allowTradeProdInd = allowTradeProdInd;
    }


    
    public String getProdTaxFreeWrapActStaCde() {
        return this.prodTaxFreeWrapActStaCde;
    }


    
    public void setProdTaxFreeWrapActStaCde(final String prodTaxFreeWrapActStaCde) {
        this.prodTaxFreeWrapActStaCde = prodTaxFreeWrapActStaCde;
    }


    
    public Date getRatingDate() {
        return this.ratingDate;
    }


    
    public void setRatingDate(final Date ratingDate) {
        this.ratingDate = ratingDate;
    }


    
    public Date getMonthEndDate() {
        return this.monthEndDate;
    }


    
    public void setMonthEndDate(final Date monthEndDate) {
        this.monthEndDate = monthEndDate;
    }


    
    public Date getEndDateYearRisk() {
        return this.endDateYearRisk;
    }


    
    public void setEndDateYearRisk(final Date endDateYearRisk) {
        this.endDateYearRisk = endDateYearRisk;
    }


    
    public Date getEndDateRiskLvlCde() {
        return this.endDateRiskLvlCde;
    }


    
    public void setEndDateRiskLvlCde(final Date endDateRiskLvlCde) {
        this.endDateRiskLvlCde = endDateRiskLvlCde;
    }


    
    public Integer getFamilySequencenNum() {
        return this.familySequencenNum;
    }


    
    public void setFamilySequencenNum(final Integer familySequencenNum) {
        this.familySequencenNum = familySequencenNum;
    }


    
    public Integer getCategorySequencenNum() {
        return this.categorySequencenNum;
    }


    
    public void setCategorySequencenNum(final Integer categorySequencenNum) {
        this.categorySequencenNum = categorySequencenNum;
    }

    
    public Integer getAverageCreditQualityNum() {
        return this.averageCreditQualityNum;
    }


    
    public void setAverageCreditQualityNum(final Integer averageCreditQualityNum) {
        this.averageCreditQualityNum = averageCreditQualityNum;
    }

    
    public Integer getDistributionFrequencyNum() {
        return this.distributionFrequencyNum;
    }


    
    public void setDistributionFrequencyNum(final Integer distributionFrequencyNum) {
        this.distributionFrequencyNum = distributionFrequencyNum;
    }


    
    public BigDecimal getExpenseRatio() {
        return this.expenseRatio;
    }


    
    public void setExpenseRatio(final BigDecimal expenseRatio) {
        this.expenseRatio = expenseRatio;
    }


    
    public BigDecimal getLoanProdOdMrgnPct() {
        return this.loanProdOdMrgnPct;
    }


    
    public void setLoanProdOdMrgnPct(final BigDecimal loanProdOdMrgnPct) {
        this.loanProdOdMrgnPct = loanProdOdMrgnPct;
    }


    
    public BigDecimal getChrgInitSalesPct() {
        return this.chrgInitSalesPct;
    }


    
    public void setChrgInitSalesPct(final BigDecimal chrgInitSalesPct) {
        this.chrgInitSalesPct = chrgInitSalesPct;
    }


    
    public BigDecimal getAnnMgmtFeePct() {
        return this.annMgmtFeePct;
    }


    
    public void setAnnMgmtFeePct(final BigDecimal annMgmtFeePct) {
        this.annMgmtFeePct = annMgmtFeePct;
    }


    
    public Character getPriShareClassInd() {
        return this.priShareClassInd;
    }


    
    public void setPriShareClassInd(final Character priShareClassInd) {
        this.priShareClassInd = priShareClassInd;
    }


    
    public Character getTopPerformersInd() {
        return this.topPerformersInd;
    }


    
    public void setTopPerformersInd(final Character topPerformersInd) {
        this.topPerformersInd = topPerformersInd;
    }


    
    public String getFundId() {
        return this.fundId;
    }


    
    public void setFundId(final String fundId) {
        this.fundId = fundId;
    }


    
    public String getAverageCreditQualityName() {
        return this.averageCreditQualityName;
    }


    
    public void setAverageCreditQualityName(final String averageCreditQualityName) {
        this.averageCreditQualityName = averageCreditQualityName;
    }


    
    public String getClosingPrcCcy() {
        return this.closingPrcCcy;
    }


    
    public void setClosingPrcCcy(final String closingPrcCcy) {
        this.closingPrcCcy = closingPrcCcy;
    }

    
    public String getMstarCcyCde() {
        return this.mstarCcyCde;
    }


    
    public void setMstarCcyCde(final String mstarCcyCde) {
        this.mstarCcyCde = mstarCcyCde;
    }

    
    public Date getCreditQualityBreakdownAsOfDate() {
        return this.creditQualityBreakdownAsOfDate;
    }


    
    public void setCreditQualityBreakdownAsOfDate(final Date creditQualityBreakdownAsOfDate) {
        this.creditQualityBreakdownAsOfDate = creditQualityBreakdownAsOfDate;
    }


    
    public String getCcyInvstCde() {
        return this.ccyInvstCde;
    }

    
    public void setCcyInvstCde(final String ccyInvstCde) {
        this.ccyInvstCde = ccyInvstCde;
    }


    
    public BigDecimal getYieldToMaturity() {
        return this.yieldToMaturity;
    }


    
    public void setYieldToMaturity(final BigDecimal yieldToMaturity) {
        this.yieldToMaturity = yieldToMaturity;
    }


    
    public BigDecimal getEffectiveDuration() {
        return this.effectiveDuration;
    }


    
    public void setEffectiveDuration(final BigDecimal effectiveDuration) {
        this.effectiveDuration = effectiveDuration;
    }


    
    public BigDecimal getCurrentYield() {
        return this.currentYield;
    }


    
    public void setCurrentYield(final BigDecimal currentYield) {
        this.currentYield = currentYield;
    }


    
    public BigDecimal getReturn1mthDaily() {
        return this.return1mthDaily;
    }


    
    public void setReturn1mthDaily(final BigDecimal return1mthDaily) {
        this.return1mthDaily = return1mthDaily;
    }


    
    public BigDecimal getReturn3mthDaily() {
        return this.return3mthDaily;
    }


    
    public void setReturn3mthDaily(final BigDecimal return3mthDaily) {
        this.return3mthDaily = return3mthDaily;
    }


    
    public BigDecimal getReturn6mthDaily() {
        return this.return6mthDaily;
    }


    
    public void setReturn6mthDaily(final BigDecimal return6mthDaily) {
        this.return6mthDaily = return6mthDaily;
    }


    
    public BigDecimal getReturnytdDaily() {
        return this.returnytdDaily;
    }


    
    public void setReturnytdDaily(final BigDecimal returnytdDaily) {
        this.returnytdDaily = returnytdDaily;
    }


    
    public BigDecimal getReturn1yrDaily() {
        return this.return1yrDaily;
    }


    
    public void setReturn1yrDaily(final BigDecimal return1yrDaily) {
        this.return1yrDaily = return1yrDaily;
    }


    
    public BigDecimal getReturn3yrDaily() {
        return this.return3yrDaily;
    }


    
    public void setReturn3yrDaily(final BigDecimal return3yrDaily) {
        this.return3yrDaily = return3yrDaily;
    }


    
    public BigDecimal getReturn5yrDaily() {
        return this.return5yrDaily;
    }


    
    public void setReturn5yrDaily(final BigDecimal return5yrDaily) {
        this.return5yrDaily = return5yrDaily;
    }


    
    public BigDecimal getReturn10yrDaily() {
        return this.return10yrDaily;
    }


    
    public void setReturn10yrDaily(final BigDecimal return10yrDaily) {
        this.return10yrDaily = return10yrDaily;
    }


    
    public BigDecimal getReturnSinceInceptionDaily() {
        return this.returnSinceInceptionDaily;
    }


    
    public void setReturnSinceInceptionDaily(final BigDecimal returnSinceInceptionDaily) {
        this.returnSinceInceptionDaily = returnSinceInceptionDaily;
    }


    
    public BigDecimal getProdTopSellRankNum() {
        return this.prodTopSellRankNum;
    }


    
    public void setProdTopSellRankNum(final BigDecimal prodTopSellRankNum) {
        this.prodTopSellRankNum = prodTopSellRankNum;
    }


    
    public String getTopSellProdIndex() {
        return this.topSellProdIndex;
    }


    
    public void setTopSellProdIndex(final String topSellProdIndex) {
        this.topSellProdIndex = topSellProdIndex;
    }


    
    public Date getProdLaunchDate() {
        return this.prodLaunchDate;
    }


    
    public void setProdLaunchDate(final Date prodLaunchDate) {
        this.prodLaunchDate = prodLaunchDate;
    }


    
    public String getInvestmentRegionCode() {
        return this.investmentRegionCode;
    }


    
    public void setInvestmentRegionCode(final String investmentRegionCode) {
        this.investmentRegionCode = investmentRegionCode;
    }


    
    public String getInvestmentRegionName1() {
        return this.investmentRegionName1;
    }


    
    public void setInvestmentRegionName1(final String investmentRegionName1) {
        this.investmentRegionName1 = investmentRegionName1;
    }


    
    public String getInvestmentRegionName2() {
        return this.investmentRegionName2;
    }


    
    public void setInvestmentRegionName2(final String investmentRegionName2) {
        this.investmentRegionName2 = investmentRegionName2;
    }


    
    public String getInvestmentRegionName3() {
        return this.investmentRegionName3;
    }


    
    public void setInvestmentRegionName3(final String investmentRegionName3) {
        this.investmentRegionName3 = investmentRegionName3;
    }


    
    public Integer getInvestmentRegionSequencenNum() {
        return this.investmentRegionSequencenNum;
    }


    
    public void setInvestmentRegionSequencenNum(final Integer investmentRegionSequencenNum) {
        this.investmentRegionSequencenNum = investmentRegionSequencenNum;
    }


    
    public String getListProdCode() {
        return this.listProdCode;
    }


    
    public void setListProdCode(final String listProdCode) {
        this.listProdCode = listProdCode;
    }


    
    public String getListProdType() {
        return this.listProdType;
    }


    
    public void setListProdType(final String listProdType) {
        this.listProdType = listProdType;
    }


    
    public BigDecimal getNumberOfBondHoldings() {
        return this.numberOfBondHoldings;
    }


    
    public void setNumberOfBondHoldings(final BigDecimal numberOfBondHoldings) {
        this.numberOfBondHoldings = numberOfBondHoldings;
    }


    
    public BigDecimal getNumberOfStockHoldings() {
        return this.numberOfStockHoldings;
    }


    
    public void setNumberOfStockHoldings(final BigDecimal numberOfStockHoldings) {
        this.numberOfStockHoldings = numberOfStockHoldings;
    }


    
    public Date getHoldingAllocationPortfolioDate() {
        return this.holdingAllocationPortfolioDate;
    }


    
    public void setHoldingAllocationPortfolioDate(final Date holdingAllocationPortfolioDate) {
        this.holdingAllocationPortfolioDate = holdingAllocationPortfolioDate;
    }


    
    public BigDecimal getKiidOngoingCharge() {
        return this.kiidOngoingCharge;
    }


    
    public void setKiidOngoingCharge(final BigDecimal kiidOngoingCharge) {
        this.kiidOngoingCharge = kiidOngoingCharge;
    }


    
    public Date getKiidOngoingChargeDate() {
        return this.kiidOngoingChargeDate;
    }


    
    public void setKiidOngoingChargeDate(final Date kiidOngoingChargeDate) {
        this.kiidOngoingChargeDate = kiidOngoingChargeDate;
    }

    
    public String getFundShreClsName() {
        return this.fundShreClsName;
    }


    
    public void setFundShreClsName(final String fundShreClsName) {
        this.fundShreClsName = fundShreClsName;
    }


    
    public String getFundShreClsNamePriLang() {
        return this.fundShreClsNamePriLang;
    }


    
    public void setFundShreClsNamePriLang(final String fundShreClsNamePriLang) {
        this.fundShreClsNamePriLang = fundShreClsNamePriLang;
    }


    
    public String getFundShreClsNameSecLang() {
        return this.fundShreClsNameSecLang;
    }


    
    public void setFundShreClsNameSecLang(final String fundShreClsNameSecLang) {
        this.fundShreClsNameSecLang = fundShreClsNameSecLang;
    }


    
    public String getFundClassCde() {
        return this.fundClassCde;
    }


    
    public void setFundClassCde(final String fundClassCde) {
        this.fundClassCde = fundClassCde;
    }


    
    public String getAmcmAuthorizeIndicator() {
        return this.amcmAuthorizeIndicator;
    }


    
    public void setAmcmAuthorizeIndicator(final String amcmAuthorizeIndicator) {
        this.amcmAuthorizeIndicator = amcmAuthorizeIndicator;
    }


    
    public Date getNextDealDate() {
        return this.nextDealDate;
    }


    
    public void setNextDealDate(final Date nextDealDate) {
        this.nextDealDate = nextDealDate;
    }


    
    public String getIndexId() {
        return this.indexId;
    }


    
    public void setIndexId(final String indexId) {
        this.indexId = indexId;
    }


    
    public String getIndexName() {
        return this.indexName;
    }


    
    public void setIndexName(final String indexName) {
        this.indexName = indexName;
    }


    
    public Date getSurveyedFundNetAssetsDate() {
        return this.surveyedFundNetAssetsDate;
    }


    
    public void setSurveyedFundNetAssetsDate(final Date surveyedFundNetAssetsDate) {
        this.surveyedFundNetAssetsDate = surveyedFundNetAssetsDate;
    }


    
    public Date getWeekRangeLowDate() {
        return this.weekRangeLowDate;
    }


    
    public void setWeekRangeLowDate(final Date weekRangeLowDate) {
        this.weekRangeLowDate = weekRangeLowDate;
    }


    
    public Date getWeekRangeHighDate() {
        return this.weekRangeHighDate;
    }


    
    public void setWeekRangeHighDate(final Date weekRangeHighDate) {
        this.weekRangeHighDate = weekRangeHighDate;
    }


    
    public String getRiskFreeRateName() {
        return this.riskFreeRateName;
    }


    
    public void setRiskFreeRateName(final String riskFreeRateName) {
        this.riskFreeRateName = riskFreeRateName;
    }


    
    public String getRelativeRiskMeasuresIndexName() {
        return this.relativeRiskMeasuresIndexName;
    }


    
    public void setRelativeRiskMeasuresIndexName(final String relativeRiskMeasuresIndexName) {
        this.relativeRiskMeasuresIndexName = relativeRiskMeasuresIndexName;
    }


    
    public String getProdStatCde() {
        return this.prodStatCde;
    }


    
    public void setProdStatCde(final String prodStatCde) {
        this.prodStatCde = prodStatCde;
    }


    
    public String getAllowBuyProdInd() {
        return this.allowBuyProdInd;
    }


    
    public void setAllowBuyProdInd(final String allowBuyProdInd) {
        this.allowBuyProdInd = allowBuyProdInd;
    }


    
    public String getAllowSellProdInd() {
        return this.allowSellProdInd;
    }


    
    public void setAllowSellProdInd(final String allowSellProdInd) {
        this.allowSellProdInd = allowSellProdInd;
    }


    
    public Date getAnnualReportDate() {
        return this.annualReportDate;
    }


    
    public void setAnnualReportDate(final Date annualReportDate) {
        this.annualReportDate = annualReportDate;
    }


    
    public String getRestrOnlScribInd() {
        return this.restrOnlScribInd;
    }


    
    public String getPayCashDivInd() {
        return payCashDivInd;
    }


    
    public void setPayCashDivInd(String payCashDivInd) {
        this.payCashDivInd = payCashDivInd;
    }


    
    public void setRestrOnlScribInd(final String restrOnlScribInd) {
        this.restrOnlScribInd = restrOnlScribInd;
    }


    
    public BigDecimal getMarketPrice() {
        return this.marketPrice;
    }


    
    public void setMarketPrice(final BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }


    
    public String getPiFundInd() {
        return this.piFundInd;
    }


    
    public void setPiFundInd(final String piFundInd) {
        this.piFundInd = piFundInd;
    }


    
    public String getDeAuthFundInd() {
        return this.deAuthFundInd;
    }


    
    public void setDeAuthFundInd(final String deAuthFundInd) {
        this.deAuthFundInd = deAuthFundInd;
    }


    
    public String getmStarID() {
        return this.mStarID;
    }


    
    public void setmStarID(final String mStarID) {
        this.mStarID = mStarID;
    }


    
    public BigDecimal getBidclosingPrc() {
        return this.bidclosingPrc;
    }


    
    public void setBidclosingPrc(final BigDecimal bidclosingPrc) {
        this.bidclosingPrc = bidclosingPrc;
    }


    
    public Date getBidPriceDate() {
        return this.bidPriceDate;
    }


    
    public void setBidPriceDate(final Date bidPriceDate) {
        this.bidPriceDate = bidPriceDate;
    }


    
    public BigDecimal getAskclosingPrc() {
        return this.askclosingPrc;
    }


    
    public void setAskclosingPrc(final BigDecimal askclosingPrc) {
        this.askclosingPrc = askclosingPrc;
    }

    
    public String getUndlIndexCde() {
        return undlIndexCde;
    }

    
    public void setUndlIndexCde(String undlIndexCde) {
        this.undlIndexCde = undlIndexCde;
    }

    
    public String getUndlIndexName() {
        return undlIndexName;
    }

    
    public void setUndlIndexName(String undlIndexName) {
        this.undlIndexName = undlIndexName;
    }

    
    public String getUndlIndexPllName() {
        return undlIndexPllName;
    }

    
    public void setUndlIndexPllName(String undlIndexPllName) {
        this.undlIndexPllName = undlIndexPllName;
    }

    
    public String getUndlIndexSllName() {
        return undlIndexSllName;
    }

    
    public void setUndlIndexSllName(String undlIndexSllName) {
        this.undlIndexSllName = undlIndexSllName;
    }

    
    public Integer getPopularRankNum() {
        return popularRankNum;
    }

    
    public void setPopularRnkInd(Integer popularRankNum) {
        this.popularRankNum = popularRankNum;
    }

    
    public String getEsgInd() {
        return esgInd;
    }

    
    public void setEsgInd(String esgInd) {
        this.esgInd = esgInd;
    }

    
    public Date getCutOffTime() {
        return cutOffTime;
    }

    
    public void setCutOffTime(Date cutOffTime) {
        this.cutOffTime = cutOffTime;
    }

    
    public Date getInDealBefDate() {
        return inDealBefDate;
    }

    
    public void setInDealBefDate(Date inDealBefDate) {
        this.inDealBefDate = inDealBefDate;
    }

    
    public Date getInScribStlBefDate() {
        return inScribStlBefDate;
    }

    
    public void setInScribStlBefDate(Date inScribStlBefDate) {
        this.inScribStlBefDate = inScribStlBefDate;
    }

    
    public Date getInRedempStlBefDate() {
        return inRedempStlBefDate;
    }

    
    public void setInRedempStlBefDate(Date inRedempStlBefDate) {
        this.inRedempStlBefDate = inRedempStlBefDate;
    }

    
    public Date getInDealAftDate() {
        return inDealAftDate;
    }

    
    public void setInDealAftDate(Date inDealAftDate) {
        this.inDealAftDate = inDealAftDate;
    }

    
    public Date getInScribStlAftDate() {
        return inScribStlAftDate;
    }

    
    public void setInScribStlAftDate(Date inScribStlAftDate) {
        this.inScribStlAftDate = inScribStlAftDate;
    }

    
    public Date getInRedempStlAftDate() {
        return inRedempStlAftDate;
    }

    
    public void setInRedempStlAftDate(Date inRedempStlAftDate) {
        this.inRedempStlAftDate = inRedempStlAftDate;
    }

    
    public Date getInterfaceDate() {
        return interfaceDate;
    }

    
    public void setInterfaceDate(Date interfaceDate) {
        this.interfaceDate = interfaceDate;
    }

    
    public String getSiFundCategoryCode() {
        return siFundCategoryCode;
    }

    
    public void setSiFundCategoryCode(String siFundCategoryCode) {
        this.siFundCategoryCode = siFundCategoryCode;
    }

    
    public String getGbaAcctTrdb() {
        return gbaAcctTrdb;
    }

    
    public void setGbaAcctTrdb(String gbaAcctTrdb) {
        this.gbaAcctTrdb = gbaAcctTrdb;
    }

    
    public String getGnrAcctTrdb() {
        return gnrAcctTrdb;
    }

    
    public void setGnrAcctTrdb(String gnrAcctTrdb) {
        this.gnrAcctTrdb = gnrAcctTrdb;
    }
    
    
    public Integer getShortLstRPQLvlNum() {
        return shortLstRPQLvlNum;
    }

    
    public void setShortLstRPQLvlNum(Integer shortLstRPQLvlNum) {
        this.shortLstRPQLvlNum = shortLstRPQLvlNum;
    }

    public String getWpbProductInd() {
        return wpbProductInd;
    }

    public void setWpbProductInd(String wpbProductInd) {
        this.wpbProductInd = wpbProductInd;
    }

    public String getCmbProductInd() {
        return cmbProductInd;
    }

    public void setCmbProductInd(String cmbProductInd) {
        this.cmbProductInd = cmbProductInd;
    }

    public String getRestrCmbOnlSrchInd() {
        return restrCmbOnlSrchInd;
    }

    public void setRestrCmbOnlSrchInd(String restrCmbOnlSrchInd) {
        this.restrCmbOnlSrchInd = restrCmbOnlSrchInd;
    }

    public String getRetireInvstInd() {
        return retireInvstInd;
    }

    public void setRetireInvstInd(String retireInvstInd) {
        this.retireInvstInd = retireInvstInd;
    }
}
