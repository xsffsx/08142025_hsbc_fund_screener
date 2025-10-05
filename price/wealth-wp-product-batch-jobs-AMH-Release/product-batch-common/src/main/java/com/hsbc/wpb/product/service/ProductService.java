package com.dummy.wpb.product.service;

import com.dummy.wpb.product.model.graphql.ProductBatchCreateResult;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateByIdInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import org.bson.Document;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Document> productByFilters(Map<String, Object> filter);

    List<Document> productByFilters(Map<String, Object> filter, Collection<String> ignoreFields);

    ProductBatchCreateResult batchCreateProduct(List<Map<String, ?>> createdProducts);

    ProductBatchUpdateResult batchUpdateProduct(ProductBatchUpdateInput productBatchUpdateInput);

    ProductBatchUpdateResult batchUpdateProductById(List<ProductBatchUpdateByIdInput> updateParams);
}
