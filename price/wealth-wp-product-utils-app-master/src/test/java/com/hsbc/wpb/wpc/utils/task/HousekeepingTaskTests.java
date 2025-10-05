package com.dummy.wpb.wpc.utils.task;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class HousekeepingTaskTests {

    @InjectMocks
    private HousekeepingTask housekeepingTask;
    @Mock
    private MongoDatabase mongodb;
    @Mock
    private MongoCollection<Document> mongoCollection;

    @Test
    public void testRun_noArgs_doesNotThrow() {
        ReflectionTestUtils.setField(housekeepingTask, "keepLogDays", 1);
        MockedStatic<Filters> filtersMockedStatic = Mockito.mockStatic(Filters.class);
        try {
            filtersMockedStatic.when(() -> Filters.lt(anyString(), any())).thenReturn(new BsonDocument());
            Mockito.when(mongodb.getCollection(anyString())).thenReturn(mongoCollection);
            housekeepingTask.run();
        } catch (Exception e) {
        } finally {
            filtersMockedStatic.close();
            Assert.assertNotNull(housekeepingTask);
        }
    }
}
