/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <b> Market Agreement entity for table MKT_AGREE. </b>
 * </p>
 */

@Setter
@Getter
@IdClass(MarketAgreementPK.class)
@Entity
@Table(name = "MKT_AGREE")
public class MarketAgreement {

    @Id
    @Column(name = "CUST_SCRIB_ID")
    private String subscriberId;

    @Id
    @Column(name = "DOC_ID")
    private long documentId;

    @Type(type = "yes_no")
    @Column(name = "AGREE_STAT_CDE")
    private boolean status;

    @Column(name = "AGREE_EXPIR_DT_TM")
    private Date expiryDate;

    @Column(name = "AGREE_REGIS_DT")
    private Date registrationDate;

    @Column(name = "REC_UPDT_DT_TM")
    private Date updateDate;

    @Column(name = "USER_UPDT_NUM")
    private String updatedBy;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[subscriberId:").append(this.subscriberId).append(", documentId:").append(String.valueOf(this.documentId))
            .append(", status:").append(this.status).append(", agreementExpiryDate:").append(this.expiryDate).append("]");
        return sb.toString();
    }
}
