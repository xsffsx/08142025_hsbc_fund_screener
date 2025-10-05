package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.List;

@Data
public class UnderlyingConfig {
    private String prodTypeCde;
    private String path;
    private List<String> allowTypes;
}
