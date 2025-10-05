package com.dummy.wpb.wpc.utils.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Enumeration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminLogServiceTests {

    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> collection;
    @Mock
    private Enumeration<String> enumeration;
    @Mock
    private MockHttpServletRequest request;

    @InjectMocks
    private AdminLogService adminLogServiceUnderTest;

    @Test
    public void testLog_givenRequest_returnsNull() {
        //set up
        when(request.getHeaderNames()).thenReturn(enumeration);
        when(enumeration.hasMoreElements()).thenReturn(true).thenReturn(false);
        when(enumeration.nextElement()).thenReturn("header");
        when(request.getHeaders(anyString())).thenReturn(enumeration);
        when(mockMongodb.getCollection("admin_log")).thenReturn(collection);
        // Run the test
        try{
            adminLogServiceUnderTest.log(request);
        }catch (Exception e){
            Assert.fail();
        }
    }
}
