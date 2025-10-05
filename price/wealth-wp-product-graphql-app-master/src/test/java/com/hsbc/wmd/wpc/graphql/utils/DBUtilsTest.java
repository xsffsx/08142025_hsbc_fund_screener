package com.dummy.wmd.wpc.graphql.utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static graphql.Assert.assertNotNull;
import static graphql.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class DBUtilsTest {
    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    private MongoCollection<Document> collection;

    @InjectMocks
    private DBUtils dbUtils;
    @Before
    public void setUp() {
        dbUtils = new DBUtils(mongoDatabase);
    }
    @Test
    public void testGetMongoDatabase_givenNull_returnsMongoDatabase() {
        // Run the test
        MongoDatabase result = dbUtils.getMongoDatabase();
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testGetCollection_givenCollectionName_returnsMongoCollection() {
        // Setup
        Mockito.when(mongoDatabase.getCollection(any(String.class))).thenReturn(collection);
        // Run the test
        MongoCollection<Document> result = DBUtils.getCollection("collectionName");
        // Verify the results
        assertNotNull(result);
    }
}
