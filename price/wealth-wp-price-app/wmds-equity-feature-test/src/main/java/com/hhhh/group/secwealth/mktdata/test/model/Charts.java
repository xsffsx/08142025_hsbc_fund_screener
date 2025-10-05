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
public class Charts {

    private String market;

    @JsonAlias("Product type")
    private String productType;

    @JsonAlias("Symbol")
    private String[] symbol;

    @JsonAlias("Display name")
    private String displayName;

    @JsonAlias("Data")
    private Object[] data;

    @JsonAlias("Fields")
    private Object[] fields;

}
