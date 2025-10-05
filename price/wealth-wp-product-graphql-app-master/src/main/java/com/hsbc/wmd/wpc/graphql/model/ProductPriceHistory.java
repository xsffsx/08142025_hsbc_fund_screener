package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ProductPriceHistory {
    private Long prodId;
    private String pdcyPrcCde;
    private LocalDate prcEffDt;
    private LocalDate prcInpDt;
    private String ccyProdMktPrcCde;
    private Double prodBidPrcAmt;
    private Double prodOffrPrcAmt;
    private Double prodMktPrcAmt;
    private Double prodNavPrcAmt;
    private Date recCreatDtTm;
    private Date recUpdtDtTm;
}
