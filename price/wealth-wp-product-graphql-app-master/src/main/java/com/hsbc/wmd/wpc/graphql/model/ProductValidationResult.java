package com.dummy.wmd.wpc.graphql.model;

import com.dummy.wmd.wpc.graphql.validator.Error;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ProductValidationResult {
    // The product data which is going to be created or updated
    private Map<String, Object> product;

    // Errors in the product validation
    private List<Error> errors;
}
