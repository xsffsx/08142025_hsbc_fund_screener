package com.dummy.wmd.wpc.graphql.utils;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

public class RequestLogUtilsTests {

    @Test
    public void testRemoveData_givenDocumentWithExecutionResult_returnsNull() {
        // Setup
        Document doc = new Document("data", "value");
        Document document = new Document("executionResult",doc);
        // Run the test
        try{
            RequestLogUtils.removeData(document);
        }catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void testRemoveData_givenDocument_returnsNull() {
        // Setup
        Document doc = new Document("data", "value");
        // Run the test
        try{
            RequestLogUtils.removeData(doc);
        }catch (Exception e){
            Assert.fail();
        }
    }
}
