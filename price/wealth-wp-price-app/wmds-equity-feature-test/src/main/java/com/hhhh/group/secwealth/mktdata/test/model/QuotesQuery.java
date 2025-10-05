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
public class QuotesQuery {

    private List<ProductKeys> productKeys;

    private String market;

    @JsonAlias("Delay")
    private String delay;

    @JsonAlias("RequestType")
    private String requestType;
}
