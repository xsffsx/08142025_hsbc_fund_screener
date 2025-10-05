package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

@Data
public class CalculatedBy {
    private String rootObjectPath;
    private String expression;
    private String className;
}
