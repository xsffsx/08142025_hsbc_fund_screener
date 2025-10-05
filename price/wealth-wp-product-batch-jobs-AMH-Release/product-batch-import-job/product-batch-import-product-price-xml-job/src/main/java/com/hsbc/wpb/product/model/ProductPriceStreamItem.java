package com.dummy.wpb.product.model;

import com.dummy.wpb.product.model.graphql.ProductPriceHistory;
import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class ProductPriceStreamItem extends ProductStreamItem {

    private Long prodId;

    /**
     * This type is document because it contains stock price
     * */
    private List<Document> importPriceHistory;
    private List<ProductPriceHistory> originalPriceHistory;
    private List<ProductPriceHistory> updatedPriceHistory;
}
