package com.dummy.wpb.product.writer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.ProductPriceStreamItem;
import com.dummy.wpb.product.model.graphql.*;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.OperationService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dummy.wpb.product.ImportProductPriceXmlJobApplication.JOB_NAME;

@Slf4j
@Service
@StepScope
public class ProductPriceImportWriter implements ItemWriter<ProductPriceStreamItem> {

    private final GraphQLService graphQLService;

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Autowired
    OperationService operationService;

    public ProductPriceImportWriter(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @Override
    public void write(List<? extends ProductPriceStreamItem> streamItemList) throws Exception {
        List<ProductBatchUpdateByIdInput> productParams = new ArrayList<>();
        List<Map<String, Object>> priceHistoryList = new ArrayList<>();

        Map<String, Object> variables = new HashMap<>();
        variables.put("updateParams", productParams);
        variables.put("priceHistory", priceHistoryList);

        for (ProductPriceStreamItem streamItem : streamItemList) {
            List<Operation> operations = operationService.calcOperations(streamItem.getOriginalProduct(), streamItem.getUpdatedProduct());
            if (CollectionUtils.isNotEmpty(operations)) {
                ProductBatchUpdateByIdInput updateInput = new ProductBatchUpdateByIdInput();
                updateInput.setProdId(streamItem.getUpdatedProduct().get(Field.prodId, Number.class).longValue());
                operations.add(new Operation("put", Field.lastUpdatedBy, JOB_NAME + " -- " + systemCde));
                updateInput.setOperations(operations);
                productParams.add(updateInput);
            }

            if (CollectionUtils.isNotEmpty(streamItem.getUpdatedPriceHistory())) {
                for (ProductPriceHistory updatedHistory : streamItem.getUpdatedPriceHistory()) {
                    priceHistoryList.add(JsonUtil.convertType(updatedHistory, Document.class));
                }
            }
        }

        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/product-price-batch-import.gql"));
        graphQLRequest.setVariables(variables);
        graphQLRequest.setDataPath("data");
        graphQLRequest.setOperationName("productPriceBatchUpdate");

        Map<String, Object> response = graphQLService.executeRequest(graphQLRequest, new TypeReference<Map<String, Object>>() {
        });

        ProductBatchUpdateResult productUpdateResult = JsonUtil.convertType(response.get("productBatchUpdateById"), ProductBatchUpdateResult.class);
        ProductPriceHistoryBatchImportResult historyImportResult = JsonUtil.convertType(response.get("productPriceHistoryBatchImport"), ProductPriceHistoryBatchImportResult.class);
        productUpdateResult.logUpdateResult(log);
        historyImportResult.logImportResult(log);
    }
}
