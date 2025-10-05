package com.dummy.wmd.wpc.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationPair {
    private String path;
    private OperationInput left;
    private OperationInput right;
}
