package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.model.ProductBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.service.ProductService;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ProductBatchUpdateFetcher implements DataFetcher<ProductBatchUpdateResult> {
    private ProductService productService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ProductBatchUpdateFetcher(ProductService productService){
        this.productService = productService;
    }

    /**
     * Update logic
     *   Step 1: Retrieve the latest document A
     *   Step 2: Create a document B to wrap the document A and put in collection document_revision.
     *   Step 3: Apply update to document A with Json Patch, the result would be document C.
     *   Step 4: Increase revision in document C by 1
     *   Step 5: Replace the document A with the new document C.
     *
     * @param environment
     * @return
     * @throws Exception
     */
    @Override
    public ProductBatchUpdateResult get(DataFetchingEnvironment environment) throws Exception {
        Map<String, Object> filterMap = environment.getArgument("filter");
        BsonDocument revisedFilterBson = new FilterUtils(CollectionName.product).toBsonDocument(filterMap);
        List<OperationInput> operations  = objectMapper.convertValue(environment.getArgument("operations"), new TypeReference<List<OperationInput>>(){});
        boolean allowPartial = environment.getArgument("allowPartial");

        return productService.batchUpdate(revisedFilterBson, operations, allowPartial);
    }
}