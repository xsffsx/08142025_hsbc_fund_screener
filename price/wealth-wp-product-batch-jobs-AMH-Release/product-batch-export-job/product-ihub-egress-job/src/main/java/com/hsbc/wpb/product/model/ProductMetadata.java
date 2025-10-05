package com.dummy.wpb.product.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("metadata")
@NoArgsConstructor
public class ProductMetadata {

    private String attrName;

    public ProductMetadata(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    private String table;

    @Field("graphQLType")
    private String type;

    private String parent;
}
