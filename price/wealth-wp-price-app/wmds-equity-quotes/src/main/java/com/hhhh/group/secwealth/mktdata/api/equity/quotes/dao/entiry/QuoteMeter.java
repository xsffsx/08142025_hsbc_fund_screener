/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

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
@Table(name = "QUOT_MTR")
@IdClass(QuoteMeterId.class) 
public class QuoteMeter implements Serializable {

    private static final long serialVersionUID = 5885879178686301790L;
    @Id
    @Column(nullable = false, name = "CUST_SCRIB_ID")
    private String subscriberId;
    @Id
    @Column(nullable = false, name = "MKT_PRD_EXCH_ID")
    private int exchangeId;
    @Column(nullable = false, name = "QUOT_CNT")
    private Long realTimeQuota;
    @Column(nullable = false, name = "QUOT_USE_CNT")
    private Long usedRealTimeQuote;
    @Column(nullable = false, name = "UNLMT_QUOT_IND")
    private String realQuotaUnlimited;
    @Column(nullable = false, name = "QUOT_DLAY_CNT")
    private Long delayedQuota;
    @Column(nullable = false, name = "QUOT_DLAY_USE_CNT")
    private Long usedDelayedQuote;
    @Column(nullable = false, name = "UNLMT_DLAY_QUOT_IND")
    private String delayedQuotaUnlimited;
    @Column(nullable = true, name = "USER_UPDT_NUM")
    private String updatedBy;
    @Column(nullable = true, name = "REC_UPDT_DT_TM")
    private Date updatedOn;
    
    
    @Override
    public String toString() {
        return "QuoteMeter [ subscriberId=" + this.subscriberId
            + ", exchangeId=" + exchangeId
            + ", realTimeQuota=" + this.realTimeQuota + ", usedRealTimeQuote=" + this.usedRealTimeQuote + ", realQuotaUnlimited="
            + this.realQuotaUnlimited + ", delayedQuota=" + this.delayedQuota + ", usedDelayedQuote=" + this.usedDelayedQuote
            + ", delayedQuotaUnlimited=" + this.delayedQuotaUnlimited + ", updatedBy=" + this.updatedBy + ", updatedOn=" + this.updatedOn + "]";
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.hhhh.wmd.mds.mdsbe.util.CSVExportArchive#getHeader()
     */
    public String[] getHeader() {
        return new String[] {"CUST_SCRIB_ID", "MKT_PRD_EXCH_ID", "CUST_SCRIB_ID", "MKT_PRD_EXCH_ID", "QUOT_CNT", "QUOT_USE_CNT", "UNLMT_QUOT_IND",
            "QUOT_DLAY_CNT", "QUOT_DLAY_USE_CNT", "UNLMT_DLAY_QUOT_IND", "USER_UPDT_NUM", "REC_UPDT_DT_TM"};
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hhhh.wmd.mds.mdsbe.util.CSVExportArchive#getFileName()
     */
    public String getFileName() {
        return "QUOT_MTR";
    }


}
