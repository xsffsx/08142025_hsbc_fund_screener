package com.dummy.wpb.wpc.utils.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@SuppressWarnings("java:S116")
public class Metadata {
    private String _id;
    private String code;
    private String table;
    private String fieldName;
    private Integer fieldScale;
    private Integer fieldPrecision;
    private Integer fieldType;
    private String fieldTypeName;
    private Integer fieldNullable;
    private String attrName;
    private String jsonPath;
    private String parent;
    private String instrument;
    private String classification;
    private String mandatory;
    private String dataUsage;
    private String logicalEntityName;
    private String sampleData;
    private String businessName;
    private String businessDefinition;
    private String graphQLType;
    private CalculatedBy calculatedBy;
    private List<String> prodType;
    private List<String> entity;
    private List<Map<String, Object>> validations;
}
