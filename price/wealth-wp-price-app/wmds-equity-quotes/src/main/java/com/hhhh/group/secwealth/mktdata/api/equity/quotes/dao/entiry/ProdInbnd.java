/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PROD_INBND")
public class ProdInbnd {

    /** The productInBoundId. */
    @Id
    @Column(nullable = false, name = "PROD_INBND_SEQ_NUM")
    private long productInBoundId;

    /** The prodcutText. */
    @Column(nullable = false, name = "PROD_TEXT")
    private String prodcutText;

    /** The prodcutLongText. */
    @Column(nullable = false, name = "PROD_LONG_TEXT")
    private String prodcutLongText;

    /** The externalCode. O Code */
    @Column(nullable = false, name = "PROD_INSTM_EXTNL_CDE")
    private String externalCode;

    /** The externalNumber. Symbol */
    @Column(nullable = false, name = "PROD_EXTNL_NUM")
    private String externalNumber;

    /** The exchangeCode */
    @Column(nullable = false, name = "MKT_PRD_EXCH_ALT_CDE")
    private String exchangeCode;

    /** The trading Market Code **/
    @Column(nullable = false, name = "CTRY_PROD_EXCHG_MKT_CDE")
    private String tradingMarketCode;

    /** The expired Indicator **/
    @Column(nullable = false, name = "EXPIR_PROD_IND")
    private String expiredIndicator;

    /** The product Beta **/
    @Column(nullable = true, name = "BETA_COEF_NUM")
    private BigDecimal productBeta;

    /** The market Cap **/
    @Column(nullable = true, name = "MKT_CPTL_AMT")
    private BigDecimal marketCap;

    /** The share Outstanding **/
    @Column(nullable = true, name = "SHARE_OUTSTD_CNT")
    private BigDecimal shareOutstanding;

    /** The source Flag **/
    @Column(nullable = false, name = "SRCE_SYS_CDE")
    private String sourceFlag;
}
