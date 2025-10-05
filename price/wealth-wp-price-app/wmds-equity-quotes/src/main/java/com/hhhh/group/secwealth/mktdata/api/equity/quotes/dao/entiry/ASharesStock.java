package com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MKT_DATA_A_SHARES_STOCK")
public class ASharesStock {

    @Column(name = "EXCHANGE")
    private String exchange;

    @Id
    @Column(name = "A_SYMBOL")
    private String aSymbol;

    @Column(name = "A_NAMEENG")
    private String aNameeng;

    @Column(name = "A_NAMETC")
    private String aNametc;

    @Column(name = "A_NAMESC")
    private String aNamesc;

    @Column(name = "STOCK_CONNECT_STATUS")
    private String stockConnectStatus;

    @Column(name = "ELIGIBILITY")
    private String eligibility;

    @Column(name = "H_SYMBOL")
    private String hSymbol;

    @Column(name = "AS_OF_DATE_TIME")
    private Timestamp asOfDateTime;

}
