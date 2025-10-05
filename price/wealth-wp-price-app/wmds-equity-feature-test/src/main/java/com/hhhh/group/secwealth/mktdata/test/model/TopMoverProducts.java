package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopMoverProducts {
    @JsonAlias("ric")
    private String ric;

    @JsonAlias("symbol")
    private String symbol;

    @JsonAlias("market")
    private String market;

    @JsonAlias("name")
    private String name;

    @JsonAlias("price")
    private BigDecimal price;

    @JsonAlias("delay")
    private Boolean delay;

    @JsonAlias("changeAmount")
    private BigDecimal changeAmount;

    @JsonAlias("changePercent")
    private BigDecimal changePercent;

    @JsonAlias("volume")
    private BigDecimal volume;

    @JsonAlias("openPrice")
    private BigDecimal openPrice;

    @JsonAlias("previousClosePrice")
    private BigDecimal previousClosePrice;

    @JsonAlias("turnover")
    private BigDecimal turnover;

    @JsonAlias("score")
    private BigDecimal score;

    @JsonAlias("productType")
    private String productType;

    @JsonAlias("currency")
    private String currency;

    @JsonAlias("As of date date and time")
    private String asOfDateTime;

    @JsonAlias("As of date date")
    private String asOfDate;

    @JsonAlias("As of date time")
    private String asOfTime;

    @JsonAlias("isQuotable")
    private Boolean isQuotable;

}
