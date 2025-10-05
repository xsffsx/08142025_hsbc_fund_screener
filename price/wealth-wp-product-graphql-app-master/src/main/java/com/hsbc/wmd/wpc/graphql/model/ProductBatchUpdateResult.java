package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class ProductBatchUpdateResult {
    private long matchCount;
    private List<Document> matchProducts;
    private List<Document> updatedProducts;
    private List<ProductValidationResult> invalidProducts;
}
