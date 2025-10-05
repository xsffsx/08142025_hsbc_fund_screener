package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistory;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistoryBatchImportResult;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistoryValidationResult;
import com.dummy.wmd.wpc.graphql.service.PriceHistoryService;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.utils.ObjectMapperUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOneModel;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductPriceHistoryBatchImportTest {

    MongoDatabase mongoDatabase = Mockito.mock(MongoDatabase.class);

    MongoCollection<Document> collPrcHist = Mockito.mock(MongoCollection.class);

    ProductPriceHistoryBatchImportFetcher fetcher = null;

    PriceHistoryService priceHistoryService = null;


    @Before
   public void beforeEach() {
        Mockito.when(mongoDatabase.getCollection(CollectionName.prod_prc_hist.toString())).thenReturn(collPrcHist);
        priceHistoryService = new PriceHistoryService(mongoDatabase);
        fetcher = new ProductPriceHistoryBatchImportFetcher(priceHistoryService);
    }

    @Test
    public void testProductPriceHistoryBatchImport() throws Exception {
        String jsonContent = CommonUtils.readResource("/files/product-price-history-batch-import.json");
        List<ProductPriceHistory> priceHistoryList = ObjectMapperUtils.readValue(jsonContent, new TypeReference<List<ProductPriceHistory>>() {
        });

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("priceHistory", priceHistoryList);
        arguments.put("allowPartial", true);
        DataFetchingEnvironment environment = new DataFetchingEnvironmentImpl.Builder()
                .arguments(arguments)
                .build();

        ProductPriceHistoryBatchImportResult importResult = fetcher.get(environment);

        List<ProductPriceHistoryValidationResult> invalids = importResult.getInvalidPriceHistories();

        List<Error> allErrors = invalids.stream()
                .flatMap(item -> item.getErrors().stream())
                .collect(Collectors.toList());

        Assert.assertTrue(allErrors.stream().anyMatch(err -> err.getCode().equals("prcEffDt@Required")));
        Assert.assertTrue(allErrors.stream().anyMatch(err -> err.getCode().equals("prodId@Required")));
        Assert.assertTrue(allErrors.stream().anyMatch(err -> err.getCode().equals("prcEffDt@Illegal")));
        Assert.assertTrue(allErrors.stream().anyMatch(err -> err.getCode().equals("ccyProdMktPrcCde@Required")));
        Assert.assertTrue(allErrors.stream().anyMatch(err -> err.getCode().equals("priceAmount@Required")));

        ArgumentCaptor<List<UpdateOneModel<Document>>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(collPrcHist, Mockito.atLeast(1)).bulkWrite(captor.capture());
        Assert.assertFalse(captor.getValue().isEmpty());
    }
}
