package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MetadataServiceTest {

    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> collMetadata;
    @Mock
    private FindIterable<Document> findIterable;
    @Mock
    private FindIterable<Document> projectionIterable;
    @InjectMocks
    private MetadataService metadataService;

    @Before
    public void setUp() {
        metadataService = new MetadataService(mockMongodb);
        ReflectionTestUtils.setField(metadataService, "collMetadata", collMetadata);
    }

    @Test
    public void testGetMetadataList_givenFilter_returnListDocument() {
        // Setup
        Bson filter = Filters.eq("key","value");
        ArrayList<Document> documentArrayList = new ArrayList<>();
        documentArrayList.add(new Document("key", "value"));
        Mockito.when(collMetadata.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.into(new ArrayList<>())).thenReturn(documentArrayList);
        // Run the test
        List<Document> result = metadataService.getMetadataList(filter);
        // Verify the results
        assertEquals(documentArrayList, result);
    }

    @Test
    public void testGetBusinessNameMapping_returnResultMap() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/metadata.json");
        List<Document> metadata = doc.getList("metadata",Document.class);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(metadata);
        Mockito.when(collMetadata.find()).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any(Bson.class))).thenReturn(projectionIterable);
        Mockito.when(projectionIterable.into(new ArrayList<>())).thenReturn(arrayList);
        // Run the test
        Map<String, String> result = metadataService.getBusinessNameMapping();
        // Verify the results
        assertNotNull(result);
    }
}
