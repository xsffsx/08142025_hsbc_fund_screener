package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class HousekeepingServiceTests {

    @InjectMocks
    private HousekeepingService housekeepingService;
    @Mock
    private MongoDatabase mongodb;
    @Mock
    private MongoCollection<Document> coll;
    @Mock
    private FindIterable<Document> findIterable;
    @Mock
    private DeleteResult result;

    @Test
    public void testCleanByBatch_givenCollectionNameAndSortFieldAndkeepingDays_returnsLong() {
        Document doc = CommonUtils.readResourceAsDocument("/files/fin_doc-doc.json");
        Mockito.when(mongodb.getCollection(anyString())).thenReturn(coll);
        Mockito.when(coll.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.sort(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.skip(anyInt())).thenReturn(findIterable);
        Mockito.when(findIterable.limit(anyInt())).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(doc).thenReturn(null);
        Mockito.when(coll.deleteMany(any(Bson.class))).thenReturn(result);
        Mockito.when(result.getDeletedCount()).thenReturn(0L);
        long count = housekeepingService.cleanByBatch("fin_doc","docEffDtTm", 7);
        Assert.assertTrue(count == 0);
    }
}
