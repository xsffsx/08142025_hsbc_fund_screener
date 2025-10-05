package com.dummy.wmd.wpc.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationInput {
    private Operation op;
    private String path;
    private Object value;
}
