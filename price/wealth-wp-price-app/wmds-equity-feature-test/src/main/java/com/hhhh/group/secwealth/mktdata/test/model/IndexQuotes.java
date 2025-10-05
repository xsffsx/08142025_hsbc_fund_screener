package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class IndexQuotes {

    @JsonAlias("Symbol")
    private String symbol;

    @JsonAlias("Name")
    private String name;

    @JsonAlias("Open Price")
    private String openPrice;

    @JsonAlias("Last Price")
    private String lastPrice;

    @JsonAlias("Change Amount")
    private String changeAmount;

    @JsonAlias("Change Percent")
    private String changePercent;

}
