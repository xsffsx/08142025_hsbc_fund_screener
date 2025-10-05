package com.dummy.wpb.product.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.graphql.*;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Retryable(backoff = @Backoff(delay = 30000))
public class DefaultProductService implements ProductService {
    private final GraphQLService graphQLService;
    private static final Set<String> productInputGraphqlFields = new HashSet<>();

    private static final String ALLOW_PARTIAL = "allowPartial";

    public DefaultProductService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @PostConstruct
    public void init() {
        initProductInputFields();
    }

    public List<Document> productByFilters(Map<String, Object> filter) {
        return productByFilters(filter, Collections.emptySet());
    }

    public List<Document> productByFilters(Map<String, Object> filter, Collection<String> ignoreFields) {
        Set<String> fields = new HashSet<>(productInputGraphqlFields);

        if (!CollectionUtils.isEmpty(ignoreFields)) {
            Collection<String> ignoreGraphqlFields = ignoreFields.stream()
                    .map(field -> new GraphqlField(field).toString().replace("}", ""))
                    .collect(Collectors.toSet());
            fields.removeIf(field -> ignoreGraphqlFields.stream().anyMatch(field::contains));
        }

        GraphQLRequest graphQLRequest = new GraphQLRequest();
        String query = CommonUtils.readResource("/gql/product-query.gql").replace("${productFields}", String.join(" ", fields));
        graphQLRequest.setQuery(query);
        graphQLRequest.setVariables(Collections.singletonMap("filter", filter));
        graphQLRequest.setDataPath("data.productByFilter");
        graphQLRequest.setOperationName("productByFilter");
        return graphQLService.executeRequest(graphQLRequest, new TypeReference<List<Document>>() {
        });
    }

    private void initProductInputFields() {
        try {
            GraphQLRequest graphQLRequest = new GraphQLRequest();
            graphQLRequest.setQuery(CommonUtils.readResource("/gql/product-schema.gql"));
            graphQLRequest.setVariables(Collections.singletonMap("graphQLType", "ProductInput"));
            graphQLRequest.setDataPath("data.graphQLTypeSchema");
            List<Map<String, String>> types = graphQLService.executeRequest(graphQLRequest, new TypeReference<List<Map<String, String>>>() {
            });
            for (Map<String, String> type : types) {
                String path = type.get("name");
                productInputGraphqlFields.add(new GraphqlField(path.replace("[*]", "")).toString());
            }
        } catch (Exception e) {
            log.error("Failed to init product input schema", e);
        }
    }

    public ProductBatchCreateResult batchCreateProduct(List<Map<String, ?>> createdProducts) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/product-batch-create.gql"));
        graphQLRequest.setDataPath("data.productBatchCreate");
        graphQLRequest.setOperationName("productBatchCreate");

        Map<String, Object> variablesObj = new LinkedHashMap<>();
        variablesObj.put("products", createdProducts);
        variablesObj.put(ALLOW_PARTIAL, true);
        graphQLRequest.setVariables(variablesObj);

        return graphQLService.executeRequest(graphQLRequest, ProductBatchCreateResult.class);
    }

    public ProductBatchUpdateResult batchUpdateProduct(ProductBatchUpdateInput productBatchUpdateInput) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/product-batch-update.gql"));
        graphQLRequest.setDataPath("data.productBatchUpdate");
        graphQLRequest.setOperationName("productBatchUpdate");

        Map<String, Object> variablesObj = new LinkedHashMap<>();
        variablesObj.put("filter", productBatchUpdateInput.getFilter());
        variablesObj.put("operations", productBatchUpdateInput.getOperations());
        variablesObj.put(ALLOW_PARTIAL, true);
        graphQLRequest.setVariables(variablesObj);

        return graphQLService.executeRequest(graphQLRequest, ProductBatchUpdateResult.class);
    }

    public ProductBatchUpdateResult batchUpdateProductById(List<ProductBatchUpdateByIdInput> updateParams) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/product-batch-update-by-Id.gql"));
        graphQLRequest.setDataPath("data.productBatchUpdateById");
        graphQLRequest.setOperationName("productBatchUpdateById");

        Map<String, Object> variablesObj = new LinkedHashMap<>();
        variablesObj.put("updateParams", updateParams);
        variablesObj.put(ALLOW_PARTIAL, true);
        graphQLRequest.setVariables(variablesObj);

        return graphQLService.executeRequest(graphQLRequest, ProductBatchUpdateResult.class);
    }
}
