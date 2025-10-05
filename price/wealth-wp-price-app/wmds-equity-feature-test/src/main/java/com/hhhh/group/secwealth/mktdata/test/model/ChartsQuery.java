package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChartsQuery {

    @JsonAlias("Product type")
    private String productType;

    @JsonAlias("Int cnt")
    private String intCnt;

    @JsonAlias("Int type")
    private String intType;

    @JsonAlias("Period")
    private String period;

    @JsonAlias("Symbol")
    private List<String> symbol;

    @JsonAlias("Filters")
    private List<String> filters;

    private String market;


}
