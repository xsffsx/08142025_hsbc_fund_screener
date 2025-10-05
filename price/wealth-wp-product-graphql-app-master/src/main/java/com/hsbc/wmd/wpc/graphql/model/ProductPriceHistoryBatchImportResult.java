package com.dummy.wmd.wpc.graphql.model;


import lombok.Data;

import java.util.List;

@Data
public class ProductPriceHistoryBatchImportResult {

    List<ProductPriceHistory> importedPriceHistories;


    List<ProductPriceHistoryValidationResult> invalidPriceHistories;
}
