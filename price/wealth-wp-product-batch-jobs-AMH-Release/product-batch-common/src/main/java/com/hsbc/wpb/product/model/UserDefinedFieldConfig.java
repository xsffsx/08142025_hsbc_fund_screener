package com.dummy.wpb.product.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDefinedFieldConfig {
    private String jsonPath;
    private String fieldCde;
    private FieldDataType fieldDataTypeText;
    private String fieldTypeCde;

    public enum FieldDataType {
        @JsonProperty("Char")
        CHAR("Char"),
        @JsonProperty("String")
        STRING("String"),
        @JsonProperty("Integer")
        INTEGER("Integer"),
        @JsonProperty("Decimal")
        DECIMAL("Decimal"),
        @JsonProperty("Date")
        DATE("Date"),
        @JsonProperty("Timestamp")
        TIMESTAMP("Timestamp");
        private String name;

        FieldDataType(String name) {
            this.name = name;
        }

    }
}
