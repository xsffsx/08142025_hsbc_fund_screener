/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */

@Getter
@Setter
@Entity
@Table(name = "QUOT_HIST")
public class QuoteHistory implements Serializable {

    /**
     * <p>
     * <b> TODO : Insert description of the field. </b>
     * </p>
     */
    private static final long serialVersionUID = 1L;

    // @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
    // "sequenceGenerator")
    // @SequenceGenerator(name = "sequenceGenerator", sequenceName =
    // "QUOT_HIST_SEQ")
    // @Column(nullable = false, name = "QUOT_HIST_ID")
    // private Long quoteHistId;

    @Id
    @GeneratedValue(generator = "generateUUID")
    @GenericGenerator(name = "generateUUID", strategy = "uuid")
    @Column(nullable = false, name = "QUOT_HIST_ID")
    private String quoteHistId;

    @Column(nullable = false, name = "QUOT_HIST_BATCH_ID")
    private Long quotHistBatId;

    @Column(nullable = false, name = "CUST_SCRIB_ID")
    private String subscriberId;

    @Column(nullable = true, name = "REC_UPDT_DT_TM")
    private Date updatedOn;

    @Column(nullable = false, name = "PROD_ALT_NUM")
    private String symbol;

    @Column(nullable = false, name = "RQST_DT_TM")
    private Date requestTime;

    @Column(nullable = true, name = "MKT_PRD_EXCH_CDE")
    private String exchangeCode;

    @Column(nullable = true, name = "TMZN_RQST_CDE")
    private String requestTimeZone;

    @Column(nullable = true, name = "RQST_STAT_CDE")
    private String requestStatus;

    @Column(nullable = true, name = "RESP_TEXT")
    private String responseText;

    @Column(nullable = true, name = "RQST_TYP")
    private String requestType;

    @Column(nullable = true, name = "TRADE_DATETIME")
    private String tradeDatetime;

    @Column(nullable = true, name = "TRADE_DATETIME_TZ")
    private String tradeDatetimeZone;

    @Column(nullable = true, name = "MKT_CDE")
    private String marketCode;

    @Column(nullable = true, name = "PE_CODE")
    private String peCode;

    @Column(nullable = true, name = "CTRY_CDE")
    private String countryCode;

    @Column(nullable = true, name = "GROUP_MEMBER")
    private String groupMember;

    @Column(nullable = true, name = "CHNL_ID")
    private String channelId;

    @Column(nullable = true, name = "APP_CDE")
    private String appCode;
       
    @Column(nullable = true, name = "SYMBOL")
    private String ProdSymbol;
    
    @Column(nullable = true, name = "RIC")
    private String ricCode;
    
    @Column(nullable = true, name = "STAFF_ID")
    private String staffid;
    
    @Column(nullable = true, name = "STOCK_NAME")
    private String requestForStock;
    
    @Column(nullable = true, name = "MKT_PRD_EXCH_ID")
    private int exchangeId;
    
    @Column(nullable = true, name = "USAGE_QNT")
    private int usageCount;
    
    @Column(nullable = true, name = "USER_UPDT_NUM")
    private String updatedBy;
}
