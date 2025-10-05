package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.dummy.wmd.wpc.graphql.model.ProductBatchCreateResult;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ProductBatchCreateFetcher implements DataFetcher<ProductBatchCreateResult> {
    private ProductService productService;

    public ProductBatchCreateFetcher(ProductService productService){
        this.productService = productService;
    }

    @Override
    public ProductBatchCreateResult get(DataFetchingEnvironment environment) {
        List<Map<String, Object>> prodList = environment.getArgument("products");
        boolean allowPartial = environment.getArgument("allowPartial");

        return productService.batchCreate(prodList, allowPartial);
    }
}