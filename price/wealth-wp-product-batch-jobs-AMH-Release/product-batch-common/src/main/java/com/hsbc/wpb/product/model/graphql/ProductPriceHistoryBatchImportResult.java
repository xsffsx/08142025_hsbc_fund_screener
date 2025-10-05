package com.dummy.wpb.product.model.graphql;


import com.dummy.wpb.product.constant.Field;
import lombok.Data;
import org.bson.Document;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class ProductPriceHistoryBatchImportResult {
    List<Document> importedPriceHistories;
    List<InvalidPriceHistory> invalidPriceHistories;

    public void logImportResult(Logger log) {
        if (!CollectionUtils.isEmpty(importedPriceHistories)) {
            for (Document priceHistory : importedPriceHistories) {
                String lineSeparator = System.lineSeparator();
                log.info("{} Product Price History has been imported (prodId: {}, pdcyPrcCde: {}, prcEffDt: {})",
                        lineSeparator,
                        priceHistory.get(Field.prodId),
                        priceHistory.get(Field.pdcyPrcCde),
                        priceHistory.get(Field.prcEffDt));
            }
        }

        if (!CollectionUtils.isEmpty(invalidPriceHistories)) {
            for (InvalidPriceHistory invalidPriceHistory : invalidPriceHistories) {
                Document priceHistory = invalidPriceHistory.getPriceHistory();
                String historyInfo = String.format("%n Product Price History imported failed (prodId: %s, pdcyPrcCde: %s, prcEffDt: %s)",
                        priceHistory.get(Field.prodId),
                        priceHistory.get(Field.pdcyPrcCde),
                        priceHistory.get(Field.prcEffDt));
                StringBuilder stringBuilder = new StringBuilder(historyInfo);
                invalidPriceHistory.getErrors().forEach(err -> stringBuilder.append(String.format("%n [%s] %s", err.getJsonPath(), err.getMessage())));
                log.info("{}", stringBuilder);
            }
        }
    }
}
