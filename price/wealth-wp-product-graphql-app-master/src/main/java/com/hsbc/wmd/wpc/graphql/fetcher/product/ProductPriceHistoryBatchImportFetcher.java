package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistory;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistoryBatchImportResult;
import com.dummy.wmd.wpc.graphql.service.PriceHistoryService;
import com.dummy.wmd.wpc.graphql.utils.ObjectMapperUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductPriceHistoryBatchImportFetcher implements DataFetcher<ProductPriceHistoryBatchImportResult> {

    private final PriceHistoryService priceHistoryService;

    public ProductPriceHistoryBatchImportFetcher(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @Override
    public ProductPriceHistoryBatchImportResult get(DataFetchingEnvironment environment) throws Exception {
        List<ProductPriceHistory> priceHistory = ObjectMapperUtils.convertValue(environment.getArgument("priceHistory"), new TypeReference<List<ProductPriceHistory>>() {
        });
        Boolean allowPartial = environment.getArgument("allowPartial");
        return priceHistoryService.batchImport(priceHistory, allowPartial);
    }
}
