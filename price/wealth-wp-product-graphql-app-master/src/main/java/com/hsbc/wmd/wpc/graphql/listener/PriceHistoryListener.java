package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistory;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistoryBatchImportResult;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistoryValidationResult;
import com.dummy.wmd.wpc.graphql.service.PriceHistoryService;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PriceHistoryListener extends BaseChangeListener {
    private PriceHistoryService priceHistoryService;

    public PriceHistoryListener(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @Override
    public void afterAllUpdate(List<Document> products) {
        updatePriceHistory(products);
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return Arrays.asList(Field.prcEffDt, Field.ccyProdMktPrcCde, Field.prodBidPrcAmt, Field.prodOffrPrcAmt, Field.prodMktPrcAmt, Field.prodNavPrcAmt);
    }

    @Override
    public void afterInsert(Document product) {
        updatePriceHistory(Collections.singletonList(product));
    }

    private void updatePriceHistory(List<Document> products) {
        /*
        "prcEffDt" : ISODate("2020-07-03T00:00:00.000Z"),
        "ccyProdMktPrcCde" : "GBP",
        "prodBidPrcAmt" : null,
        "prodOffrPrcAmt" : null,
        "prodMktPrcAmt" : null,
        "prodNavPrcAmt" : 2.0601,
        "prodPrcUpdtDtTm" : ISODate("2020-08-07T02:15:42.142Z"),
         */
        List<ProductPriceHistory> productPriceHistoryList = products.stream().map(prod -> {
            ProductPriceHistory productPriceHistory = new ProductPriceHistory();
            productPriceHistory.setProdId(prod.getLong(Field.prodId));
            productPriceHistory.setPrcEffDt(DocumentUtils.getLocalDate(prod, Field.prcEffDt));
            productPriceHistory.setPrcInpDt(DocumentUtils.getLocalDate(prod, Field.prcInpDt));
            productPriceHistory.setCcyProdMktPrcCde(prod.getString(Field.ccyProdMktPrcCde));
            productPriceHistory.setProdBidPrcAmt(prod.getDouble(Field.prodBidPrcAmt));
            productPriceHistory.setProdOffrPrcAmt(prod.getDouble(Field.prodOffrPrcAmt));
            productPriceHistory.setProdMktPrcAmt(prod.getDouble(Field.prodMktPrcAmt));
            productPriceHistory.setProdNavPrcAmt(prod.getDouble(Field.prodNavPrcAmt));
            return productPriceHistory;
        }).collect(Collectors.toList());

        ProductPriceHistoryBatchImportResult importResult = priceHistoryService.batchImport(productPriceHistoryList, false);
        List<ProductPriceHistoryValidationResult> invalidPriceHistories = importResult.getInvalidPriceHistories();
        if (CollectionUtils.isNotEmpty(invalidPriceHistories)) {
            for (Error error : invalidPriceHistories.get(0).getErrors()) {
                log.warn(error.getMessage());
            }
        }

    }
}
