package com.dummy.wpb.product.model;

import lombok.Data;

import java.util.Map;

@Data
public class TableInfo {
    private String tableName;

    private String parent;

    private Map<String,String> fieldMapping;
}
