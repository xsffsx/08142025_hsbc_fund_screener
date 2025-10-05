package com.dummy.wpb.wpc.utils.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductTableInfo {
    private String table;
    private String importMethod;
    private String prodIdField;
    private String toAttribute;
    private String toCollection;
    private String valueField;
    private String businessName;
    private String businessDefinition;
    private List<String> keyFields;

    public String getParent() {
        if(null == toAttribute) return null;
        if(importMethod.endsWith("List")){
            return toAttribute + "[*]";
        } else {
            return toAttribute;
        }
    }
}
