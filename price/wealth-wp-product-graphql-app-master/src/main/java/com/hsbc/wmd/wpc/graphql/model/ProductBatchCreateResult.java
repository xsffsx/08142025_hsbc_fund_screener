package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductBatchCreateResult {
    private List<Map<String, Object>> createdProducts;
    private List<ProductValidationResult> invalidProducts;
}
