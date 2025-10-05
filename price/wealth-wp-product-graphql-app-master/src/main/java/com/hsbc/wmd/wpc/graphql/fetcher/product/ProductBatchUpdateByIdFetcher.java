package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.model.ProductBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import com.dummy.wmd.wpc.graphql.utils.ObjectMapperUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductBatchUpdateByIdFetcher implements DataFetcher<ProductBatchUpdateResult> {

    private ProductService productService;

    public ProductBatchUpdateByIdFetcher(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductBatchUpdateResult get(DataFetchingEnvironment environment) throws Exception {
        List<Map<String, Object>> updateParams = environment.getArgument("updateParams");
        boolean allowPartial = environment.getArgument("allowPartial");

        Map<Long, List<OperationInput>> prodIdOperationMap = new LinkedHashMap<>();
        for (Map<String, Object> updateParam : updateParams) {
            Long prodId = (Long) updateParam.get("prodId");
            List<OperationInput> operations = ObjectMapperUtils.convertValue(updateParam.get("operations"), new TypeReference<List<OperationInput>>() {
            });
            prodIdOperationMap.put(prodId, operations);
        }

        return productService.batchUpdateById(prodIdOperationMap, allowPartial);
    }
}
