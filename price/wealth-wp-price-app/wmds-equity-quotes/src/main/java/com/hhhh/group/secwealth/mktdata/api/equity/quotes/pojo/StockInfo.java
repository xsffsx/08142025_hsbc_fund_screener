package com.hhhh.group.secwealth.mktdata.api.equity.quotes.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StockInfo {

    private String nbRic;

    private String symbol;

    private String name;

    private Boolean isADR;

    private Boolean isETF;

    private BigDecimal PERatio;

    private BigDecimal expectedPERatio;

    private BigDecimal marketCap;

    private BigDecimal divYield;

    private BigDecimal expectedDividendYield;

    private String ccy;

    private String industry;

    private String exDivDate;

    private BigDecimal ls;

    private BigDecimal nc;

    private BigDecimal pc;

    private BigDecimal prevClose;

    private BigDecimal volume;

    private BigDecimal turnover;

    private BigDecimal adrRatio;

    private BigDecimal adrPrice;

    private String adrCcy;

    private String symbolHK;

    private String nameHK;

    private String fundType;

    private String fundTypeSubClass;

    private String fundRegion;

    private BigDecimal beta6M;

    private BigDecimal beta1y;

    private BigDecimal beta3y;

    private BigDecimal beta5y;

    private BigDecimal beta10y;

    private BigDecimal amount;

    private String amountCcy;

    private String amountDate;

    private BigDecimal expenseRatio;
}
