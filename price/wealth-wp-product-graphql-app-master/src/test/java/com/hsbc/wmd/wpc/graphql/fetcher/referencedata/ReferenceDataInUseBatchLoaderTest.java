package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.dummy.wmd.wpc.graphql.constant.Field;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.concurrent.CompletionStage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReferenceDataInUseBatchLoaderTest {

    private MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);

    private ReferenceDataInUseBatchLoader batchLoader= new ReferenceDataInUseBatchLoader();

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(batchLoader, "mongoTemplate", mongoTemplate);
    }

    @Test
    public void testLoad() {
        List<DataFetchingEnvironment> envs = new ArrayList<>();
        Map<String, Object> args = new HashMap<>();
        args.put("path", "cioAsetClass");
        args.put("filter", new Document(Field.cdvCde, "CIOAC").append(Field.ctryRecCde, "HK").append(Field.grpMembrRecCde, "HBAP"));
        args.put("productFilter", new Document(Field.prodTypeCde, "BOND").append(Field.ctryRecCde, "HK").append(Field.grpMembrRecCde, "HBAP").append(Field.prodStatCde, "A"));

        envs.add(DataFetchingEnvironmentImpl.newDataFetchingEnvironment()
                .arguments(args)
                .source(new Document(Field.ctryRecCde, "HK").append(Field.grpMembrRecCde, "HBAP").append(Field.cdvCde, "CIOAC1"))
                .build());
        envs.add(DataFetchingEnvironmentImpl.newDataFetchingEnvironment()
                .arguments(args)
                .source(new Document(Field.ctryRecCde, "HK").append(Field.grpMembrRecCde, "HBAP").append(Field.cdvCde, "CIOAC10"))
                .build());

        List<Document> mappedResults = new ArrayList<>();
        Document resultDoc = new Document("_id",
                new Document(Field.ctryRecCde, "HK").append(Field.grpMembrRecCde, "HBAP")).append("cioAsetClass", Collections.singletonList("CIOAC10"));
        mappedResults.add(resultDoc);

        AggregationResults<Document> aggregationResults = new AggregationResults<>(mappedResults, new Document());
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("product"), eq(Document.class))).thenReturn(aggregationResults);

        CompletionStage<List<Boolean>> resultStage = batchLoader.load(envs);
        List<Boolean> result = resultStage.toCompletableFuture().join();

        assertEquals(Arrays.asList(false, true), result);
    }
}