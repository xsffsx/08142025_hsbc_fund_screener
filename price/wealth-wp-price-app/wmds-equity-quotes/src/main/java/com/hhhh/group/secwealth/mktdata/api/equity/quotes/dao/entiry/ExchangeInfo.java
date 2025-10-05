package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MKT_PRD_EXCH_INFO")
public class ExchangeInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2652318698801061528L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("--[id:").append(getId()).append("]").append("[exchangeCode:").append(getExchangeCode())
            .append("]").append("[exchangeDesc:").append(getExchangeDesc()).append("]").append("[updatedBy:")
            .append(getUpdatedBy()).append("]").append("[updatedTime:").append(getUpdatedTime()).append("]");

        return sb.toString();
    }
    @Id
    @Column(name = "MKT_PRD_EXCH_ID", unique = true, nullable = false)
    private int id;

    @Column(name="MKT_PRD_EXCH_CDE")
    private String exchangeCode;

    @Column(name="MKT_PRD_EXCH_DESC")
    private String exchangeDesc;

    @Column(name="USER_UPDT_NUM")
    private String updatedBy;
    
    @Column(name="REC_UPDT_DT_TM")
    private Timestamp updatedTime;

}
