package com.dummy.wmd.wpc.graphql.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dummy.wmd.wpc.graphql.model.CalculatedBy;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import graphql.GraphQLContext;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl.Builder;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class CalculatedFieldDataLoaderTest {

    CalculatedFieldDataLoader dataLoader;

    MongoDatabase mongoDatabase = Mockito.mock(MongoDatabase.class);
    Document product = Document.parse(CommonUtils.readResource("/files/calculated_field_product.json"));

    @Before
    public void setUp() throws JsonProcessingException {
        MongoCollection mongoCollection = Mockito.mock(MongoCollection.class);
        FindIterable findIterable = Mockito.mock(FindIterable.class);

        CalculatedBy ctryRecCdeCalculatedBy = new CalculatedBy();
        ctryRecCdeCalculatedBy.setRootObjectPath("../../..");
        ctryRecCdeCalculatedBy.setExpression("ctryRecCde");
        Document ctryRecCdeMetadata = new Document();
        ctryRecCdeMetadata.put("attrName", "ctryRecCde");
        ctryRecCdeMetadata.put("calculatedBy", ctryRecCdeCalculatedBy);

        CalculatedBy allowTradProdIndCalculatedBy = new CalculatedBy();
        allowTradProdIndCalculatedBy.setRootObjectPath("..");
        allowTradProdIndCalculatedBy.setExpression("allowBuyProdInd=='Y' or allowSellProdInd=='Y' or allowSwInProdInd=='Y' or allowSwOutProdInd=='Y' or allowSellMipProdInd=='Y'? 'Y' : 'N'");
        Document allowTradProdIndMetadata = new Document();
        allowTradProdIndMetadata.put("attrName", "allowTradProdInd");
        allowTradProdIndMetadata.put("calculatedBy", allowTradProdIndCalculatedBy);

        Mockito.when(mongoDatabase.getCollection(anyString())).thenReturn(mongoCollection);
        Mockito.when(mongoCollection.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.iterator()).thenReturn(new TestMongoCursor(Lists.newArrayList(ctryRecCdeMetadata, allowTradProdIndMetadata).iterator()));
        Mockito.doCallRealMethod().when(findIterable).forEach(any(Consumer.class));
        dataLoader = new CalculatedFieldDataLoader(mongoDatabase);
    }


    @Test
    public void testLoad_givenDataFetchingEnvironmentAndProductList_returnAltIdCtryRecCde() throws ExecutionException, InterruptedException {
        ExecutionStepInfo stepInfo = Mockito.mock(ExecutionStepInfo.class);
        ResultPath resultPath = ResultPath.parse("/productByFilter[0]/altId[0]/ctryRecCde");
        Mockito.when(stepInfo.getPath()).thenReturn(resultPath);
        GraphQLContext graphQLContext = new GraphQLContext.Builder().of("productByFilter", Lists.newArrayList(product)).build();
        DataFetchingEnvironment environment = new Builder()
                .context(graphQLContext)
                .executionStepInfo(stepInfo).build();
        CompletionStage<List<Object>> completionStage = dataLoader.load(Lists.newArrayList(environment));
        List<Object> objects = completionStage.toCompletableFuture().get();
        Assert.assertEquals(product.get("ctryRecCde"), objects.get(0));
    }

    @Test
    public void testLoad_givenDataFetchingEnvironmentAndProduct_returnAltIdCtryRecCde() throws ExecutionException, InterruptedException {
        ExecutionStepInfo stepInfo = Mockito.mock(ExecutionStepInfo.class);
        ResultPath resultPath = ResultPath.parse("/productByFilter/altId[0]/ctryRecCde");
        Mockito.when(stepInfo.getPath()).thenReturn(resultPath);
        GraphQLContext graphQLContext = new GraphQLContext.Builder().of("productByFilter", product).build();
        DataFetchingEnvironment environment = new Builder()
                .context(graphQLContext)
                .executionStepInfo(stepInfo).build();
        CompletionStage<List<Object>> completionStage = dataLoader.load(Lists.newArrayList(environment));
        List<Object> objects = completionStage.toCompletableFuture().get();
        Assert.assertEquals(product.get("ctryRecCde"), objects.get(0));
    }


    @Test
    public void testLoad_givenDataFetchingEnvironmentAndProduct_returnAllowTradProdInd() throws ExecutionException, InterruptedException {
        ExecutionStepInfo stepInfo = Mockito.mock(ExecutionStepInfo.class);
        ResultPath resultPath = ResultPath.parse("/productByFilter[0]/allowTradProdInd");
        Mockito.when(stepInfo.getPath()).thenReturn(resultPath);
        GraphQLContext graphQLContext = new GraphQLContext.Builder().of("productByFilter", Lists.newArrayList(product)).build();
        DataFetchingEnvironment environment = new Builder()
                .context(graphQLContext)
                .executionStepInfo(stepInfo).build();
        CompletionStage<List<Object>> completionStage = dataLoader.load(Lists.newArrayList(environment));
        List<Object> objects = completionStage.toCompletableFuture().get();
        Assert.assertEquals("Y", objects.get(0));
    }

    private static class TestMongoCursor implements MongoCursor<Document> {
        Iterator<Document> iterator;

        public TestMongoCursor(Iterator<Document> iterator) {
            this.iterator = iterator;
        }

        @Override
        public void close() {
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Document next() {
            return iterator.next();
        }

        @Override
        public int available() {
            return 0;
        }

        @Override
        public Document tryNext() {
            return iterator.next();
        }

        @Override
        public ServerCursor getServerCursor() {
            return null;
        }

        @Override
        public ServerAddress getServerAddress() {
            return null;
        }
    }
}
