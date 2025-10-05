package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBServiceTest {
    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private MongoCollection<Document> mongoCollection;
    @Mock
    private FindIterable<Document> findIterable;
    @InjectMocks
    private MongoDBService metadataService;

    @Before
    public void setUp() {
        metadataService = new MongoDBService(mongoDatabase);
        Mockito.when(mongoDatabase.getCollection(any(String.class))).thenReturn(mongoCollection);
    }

    @Test
    public void testQueryForMapList_givenFiter_returnsListResult() {
        // Setup
        Bson filter = Filters.eq("key", "value");
        Mockito.when(mongoCollection.find(filter)).thenReturn(findIterable);
        Mockito.when(findIterable.into(new LinkedList<>())).thenReturn(new LinkedList<>());
        // Run the test
        List<Map<String, Object>> result = metadataService.queryForMapList(CollectionName.amendment, filter);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testCountDocuments_givenFilter_returnsCount() {
        // Setup
        Bson filter = Filters.eq("key", "value");
        Mockito.when(mongoCollection.countDocuments(filter)).thenReturn(0L);
        // Run the test
        long result = MongoDBService.countDocuments(CollectionName.amendment, filter);
        // Verify the results
        assertEquals(0L, result);
    }
}
