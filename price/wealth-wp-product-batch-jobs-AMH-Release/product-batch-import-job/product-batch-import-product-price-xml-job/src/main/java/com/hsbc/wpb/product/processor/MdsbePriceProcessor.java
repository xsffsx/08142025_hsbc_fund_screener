package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductPriceStreamItem;
import com.dummy.wpb.product.utils.JsonPathUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;

import static com.dummy.wpb.product.constant.BatchConstants.DOT;

@Order(3)
public class MdsbePriceProcessor implements ItemProcessor<ProductPriceStreamItem, ProductPriceStreamItem> {

    private static final Map<String, String> PATH_MAPPING = new HashMap<>();

    static {
        PATH_MAPPING.put(Field.debtInstm + DOT + Field.prodCloseBidPrcAmt, Field.prodBidPrcAmt);
        PATH_MAPPING.put(Field.debtInstm + DOT + Field.prodCloseOffrPrcAmt, Field.prodOffrPrcAmt);
        PATH_MAPPING.put(Field.debtInstm + DOT + Field.bondCloseDt, Field.prcEffDt);
    }

    @Override
    public ProductPriceStreamItem process(ProductPriceStreamItem productPriceStreamItem) {
        Document importedProd = productPriceStreamItem.getImportProduct();
        Document updatedProd = productPriceStreamItem.getUpdatedProduct();
        // Special logic for MDS
        PATH_MAPPING.forEach((source, target) -> JsonPathUtils.setValue(updatedProd, source, JsonPathUtils.readValue(importedProd, target)));
        return productPriceStreamItem;
    }
}
