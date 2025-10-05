package com.dummy.wmd.wpc.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiffType {
    private String path;
    private Object left;
    private Object right;
}
