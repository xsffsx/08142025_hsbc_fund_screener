package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SequenceServiceTest {

    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> colSequence;
    @InjectMocks
    private SequenceService sequenceService;

    @Before
    public void setUp() throws Exception {
        sequenceService = new SequenceService(mockMongodb);
        ReflectionTestUtils.setField(sequenceService, "colSequence", colSequence);
    }

    @Test
    public void testNextId_givenSequenceNameWithDocument_returnsId() {
        // Setup
        Document document = new Document(Field.max, 1L);
        Mockito.when(colSequence.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class))).thenReturn(document);
        // Run the test
        Long result = sequenceService.nextId("sequenceName");
        // Verify the results
        assertEquals(Long.valueOf(1L), result);
    }

    @Test
    public void testNextId_givenSequenceNameWithNoDocument_returnsId() {
        // Run the test
        Long result = sequenceService.nextId("sequenceName");
        // Verify the results
        assertEquals(Long.valueOf(1L), result);
    }
}
