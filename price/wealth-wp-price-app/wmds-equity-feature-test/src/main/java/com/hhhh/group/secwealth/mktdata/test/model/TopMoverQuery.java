package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class TopMoverQuery {

    @JsonAlias("ProductType")
    private String productType;

    @JsonAlias("ExchangeCode")
    private String exchangeCode;

    @JsonAlias("MoverType")
    private String moverType;

    @JsonAlias("BoardType")
    private String boardType;

    @JsonAlias("Delay")
    private String delay;

    @JsonAlias("TopNum")
    private String topNum;

    private String market;

}
