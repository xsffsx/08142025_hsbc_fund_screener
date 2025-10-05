package com.dummy.wpb.product.model.graphql;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class InvalidPriceHistory {
    private Document priceHistory;
    // Errors in validation
    private List<InvalidPriceHistoryError> errors;
}
