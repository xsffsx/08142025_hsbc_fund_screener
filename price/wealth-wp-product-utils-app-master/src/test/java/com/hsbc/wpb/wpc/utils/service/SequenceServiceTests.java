package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SequenceServiceTests {

    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> colSequence;
    private SequenceService sequenceService;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.sequence)).thenReturn(colSequence);
        sequenceService = new SequenceService(mockMongodb);
    }

    @Test
    public void testInitSequenceId_givenSequenceName_returnsNull() {
        try {
            sequenceService.initSequenceId("sequenceName", 0L);
        }catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testNextId_givenSequenceNameMockNull_returnsLong() {
        // Setup
        when(colSequence.findOneAndUpdate(any(Bson.class),any(Bson.class),any(FindOneAndUpdateOptions.class)))
                .thenReturn(null);
        // Run the test
        Long result = sequenceService.nextId("sequenceName");
        // Verify the results
        assertThat(result).isEqualTo(1L);
    }

    @Test
    public void testNextId_givenSequenceName_returnsLong(){
        Document document = new Document(Field.max,1L);
        // Setup
        when(colSequence.findOneAndUpdate(any(Bson.class),any(Bson.class),any(FindOneAndUpdateOptions.class)))
                .thenReturn(document);
        // Run the test
        Long result = sequenceService.nextId("sequenceName");
        // Verify the results
        assertThat(result).isEqualTo(1L);
    }
}
