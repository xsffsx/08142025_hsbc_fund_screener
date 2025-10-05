package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProdUndlInstmBatchLoaderTest {

    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    private MongoCollection<Document> collection;

    @InjectMocks
    private ProdUndlInstmBatchLoader batchLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mongoDatabase.getCollection(CollectionName.product.toString())).thenReturn(collection);
    }

    @Test
    void testLoad() throws ExecutionException, InterruptedException {
        Document doc1 = new Document("prodId", 1L);
        Document doc2 = new Document("prodId", 2L);
        FindIterable<Document> documents = Mockito.mock(FindIterable.class);
        when(documents.into(any())).thenReturn(Arrays.asList(doc1, doc2));
        when(collection.find(Mockito.any(Bson.class))).thenReturn(documents);

        DataFetchingEnvironment env1 = Mockito.mock(DataFetchingEnvironment.class);
        DataFetchingEnvironment env2 = Mockito.mock(DataFetchingEnvironment.class);
        Document undlStock1 = new Document("prodIdUndlInstm", 1L);
        Document undlStock2 = new Document("prodIdUndlInstm", 2L);
        when(env1.getSource()).thenReturn(undlStock1);
        when(env2.getSource()).thenReturn(undlStock2);

        List<DataFetchingEnvironment> envs = Arrays.asList(env1, env2);
        CompletableFuture<List<Document>> future = batchLoader.load(envs).toCompletableFuture();
        List<Document> result = future.get();

        assertEquals(2, result.size());
        assertEquals(doc1, result.get(0));
        assertEquals(doc2, result.get(1));
    }
}