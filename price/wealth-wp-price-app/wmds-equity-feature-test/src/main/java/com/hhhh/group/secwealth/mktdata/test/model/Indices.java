package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class Indices {
    @JsonAlias("symbol")
    private String symbol;

    @JsonAlias("name")
    private String name;

    @JsonAlias("lastPrice")
    private BigDecimal lastPrice;

    @JsonAlias("changeAmount")
    private BigDecimal changeAmount;

    @JsonAlias("changePercent")
    private BigDecimal changePercent;

    @JsonAlias("openPrice")
    private BigDecimal openPrice;

    @JsonAlias("previousClosePrice")
    private BigDecimal previousClosePrice;

    @JsonAlias("dayRangeHigh")
    private BigDecimal dayRangeHigh;

    @JsonAlias("dayRangeLow")
    private BigDecimal dayRangeLow;

    @JsonAlias("changePercent1M")
    private BigDecimal changePercent1M;

    @JsonAlias("changePercent2M")
    private BigDecimal changePercent2M;

    @JsonAlias("changePercent1Y")
    private BigDecimal changePercent1Y;

    @JsonAlias("oneMonthLowPrice")
    private BigDecimal oneMonthLowPrice;

    @JsonAlias("twoMonthLowPrice")
    private BigDecimal twoMonthLowPrice;

    @JsonAlias("threeMonthLowPrice")
    private BigDecimal threeMonthLowPrice;

    @JsonAlias("oneMonthHighPrice")
    private BigDecimal oneMonthHighPrice;

    @JsonAlias("twoMonthHighPrice")
    private BigDecimal twoMonthHighPrice;

    @JsonAlias("threeMonthHighPrice")
    private BigDecimal threeMonthHighPrice;

    @JsonAlias("yearHighPrice")
    private BigDecimal yearHighPrice;

    @JsonAlias("yearLowPrice")
    private BigDecimal yearLowPrice;

    @JsonAlias("asOfDateTime")
    private String asOfDateTime;
}
