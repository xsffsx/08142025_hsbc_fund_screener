package com.dummy.wmd.wpc.graphql.service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class TestUserServiceTest {

    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> collection;
    @Mock
    private FindIterable<Document> iterable;
    @InjectMocks
    private TestUserService testUserService;

    @Before
    public void setUp() {
        testUserService = new TestUserService(mockMongodb);
        ReflectionTestUtils.setField(testUserService, "collection", collection);
    }

    @Test
    public void testGetRoles_givenStaffId_returnsList() {
        // Setup
        List<String> list = Arrays.asList("value");
        Document document = new Document("roles", list);
        Mockito.when(collection.find(any(Bson.class))).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(document);
        // Run the test
        List<String> result = testUserService.getRoles("staffId");
        // Verify the results
        assertEquals(Arrays.asList("value"), result);
    } 
    
    @Test
    public void testGetRoles_givenStaffId_returnsNull() {
        // Setup
        Mockito.when(collection.find(any(Bson.class))).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(null);
        // Run the test
        List<String> result = testUserService.getRoles("staffId");
        // Verify the results
        assertNull(result);
    }

}
