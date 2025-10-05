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
public class PredictiveSearchQuery {

    private String market;

    @JsonAlias("Key word")
    private String keyword;

    @JsonAlias("Asset classes")
    private List<String> assetClasses;

    @JsonAlias("Top num")
    private String topNum;

    @JsonAlias("Criteria key")
    private String criteriaKey;

    @JsonAlias("Criteria value")
    private String criteriaValue;

    @JsonAlias("Operator")
    private String operator;
}
