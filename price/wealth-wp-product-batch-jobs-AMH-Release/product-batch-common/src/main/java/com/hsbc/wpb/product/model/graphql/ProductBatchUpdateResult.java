package com.dummy.wpb.product.model.graphql;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.error.ErrorCode;
import com.dummy.wpb.product.error.ErrorLogger;
import lombok.Data;
import org.bson.Document;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductBatchUpdateResult {

    private long matchCount;

    private List<Document> matchProducts;

    private List<Document> updatedProducts;

    private List<InvalidProduct> invalidProducts;

    public void logUpdateResult(Logger log) {
        for (Document product : updatedProducts) {
            String keyInfo = buildProdKeyInfo(product);
            log.info(String.format("\nProduct has been updated %s", keyInfo));
        }

        for (InvalidProduct invalidProduct : invalidProducts) {
            Document product = invalidProduct.getProduct();
            String keyInfo = buildProdKeyInfo(product);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("\nProduct updated failed %s", keyInfo));
            invalidProduct.getErrors().forEach(err -> {
                stringBuilder.append(String.format("\n[%s] %s", err.getJsonPath(), err.getMessage()));
            });
            log.info(stringBuilder.toString());
        }
    }

    private String buildProdKeyInfo(Document prod) {
        String ctryRecCde = prod.getString(Field.ctryRecCde);
        String grpMembrRecCde = prod.getString(Field.grpMembrRecCde);
        String prodTypeCde = prod.getString(Field.prodTypeCde);
        String prodAltPrimNum = prod.getString(Field.prodAltPrimNum);

        return String.format("(ctryRecCde: %s, grpMembrRecCde: %s, prodTypeCde: %s, prodAltPrimNum: %s)", ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum);
    }
}
