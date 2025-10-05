package com.dummy.wmd.wpc.graphql.model;

import com.dummy.wmd.wpc.graphql.validator.Error;
import lombok.Data;

import java.util.List;

@Data
public class ProductPriceHistoryValidationResult {
    private ProductPriceHistory priceHistory;
    // Errors in validation
    private List<Error> errors;
}
