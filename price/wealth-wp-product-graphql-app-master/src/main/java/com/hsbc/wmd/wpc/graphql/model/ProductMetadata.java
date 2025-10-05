package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S116")
@Data
public class ProductMetadata {
    private String _id;
    private String attrName;
    private String jsonPath;
    private String parent;
    private String businessName;
    private String businessDefinition;
    private String graphQLType;
    private CalculatedBy calculatedBy;
    private List<ProductMetadata> fields = new ArrayList<>();
}
