package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListedIPO {

    @JsonAlias("symbol")
    private String symbol;

    @JsonAlias("name")
    private String name;

    @JsonAlias("listDate")
    private String listDate;

    @JsonAlias("lastPrice")
    private Integer lastPrice;

    @JsonAlias("listPrice")
    private Integer listPrice;

    @JsonAlias("accumulatePerChange")
    private Integer accumulatePerChange;

    @JsonAlias("changeAmount")
    private Integer changeAmount;

    @JsonAlias("changePercent")
    private Integer changePercent;

    @JsonAlias("currency")
    private String currency;

    @JsonAlias("overSubscriptionRate")
    private String overSubscriptionRate;

    @JsonAlias("ipoSponsor")
    private String ipoSponsor;

    @JsonAlias("peRatio")
    private Integer peRatio;

    @JsonAlias("firstDayPerformance")
    private Integer firstDayPerformance;

    @JsonAlias("ipoStatus")
    private Integer ipoStatus;

    @JsonAlias("asOfDateTime")
    private String asOfDateTime;
}
