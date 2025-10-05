package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.constant.Table;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.*;

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SysParamSynchronizerTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;

    @Mock
    private MongoDatabase mockMongodb;

    @Test
    public void testGetMasterRecord_notNull() {
        // Create a mock key
        Map<String, Object> keys = new HashMap<>();
        keys.put("ROWID", "123");

        // Create a mock class for SYS_PARM
        SysParamSynchronizer sysParamSynchronizer = new SysParamSynchronizer(
                Table.SYS_PARM, mockNamedParameterJdbcTemplate, mockMongodb);
        Mockito.doNothing()
                .when(sysParamSynchronizer.namedParameterJdbcTemplate)
                .query(Mockito.any(String.class), Mockito.any(SqlParameterSource.class), Mockito.any(RowCallbackHandler.class));
        SysParamSynchronizer spy = Mockito.spy(sysParamSynchronizer);
        // Call the getMasterRecord method
        Assert.assertNull(spy.getMasterRecord(keys));
    }

    @Test
    public void testSync_notNull() {
        // Create a mock keySet
        Set<Map<String, Object>> keySet = new HashSet<>();
        Map<String, Object> keys = new HashMap<>();
        keys.put("ROWID", "123");
        keySet.add(keys);

        // Create a mock master record
        Map<String, Object> master = new HashMap<>();
        master.put("_id", 1L);

        // Create a mock class
        SysParamSynchronizer sysParamSynchronizer = new SysParamSynchronizer(
                Table.SYS_PARM, mockNamedParameterJdbcTemplate, mockMongodb);
        Mockito.doNothing()
                .when(sysParamSynchronizer.namedParameterJdbcTemplate)
                .query(Mockito.any(String.class), Mockito.any(SqlParameterSource.class), Mockito.any(RowCallbackHandler.class));

        sysParamSynchronizer.collection = Mockito.mock(MongoCollection.class);
        FindIterable<Document> findIterable = Mockito.mock(FindIterable.class);
        Mockito.when(sysParamSynchronizer.collection.find(Mockito.any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(new Document(master));

        SysParamSynchronizer spy = Mockito.spy(sysParamSynchronizer);
        Mockito.doReturn(master).when(spy).getMasterRecord(Mockito.any());

        // Call the sync method
        spy.sync(keySet);

        // Verify that the replaceOne method was called with the correct parameters
        Mockito.verify(sysParamSynchronizer.collection, times(1)).replaceOne(Mockito.any(Bson.class), Mockito.any(Document.class), Mockito.any(ReplaceOptions.class));
    }

    @Test
    public void testSync_null() {
        // Create a mock keySet
        Set<Map<String, Object>> keySet = new HashSet<>();
        Map<String, Object> keys = new HashMap<>();
        keys.put("ROWID", "123");
        keySet.add(keys);

        // Create a mock class
        SysParamSynchronizer sysParamSynchronizer = new SysParamSynchronizer(
                Table.SYS_PARM, mockNamedParameterJdbcTemplate, mockMongodb);
        Mockito.doNothing()
                .when(sysParamSynchronizer.namedParameterJdbcTemplate)
                .query(Mockito.any(String.class), Mockito.any(SqlParameterSource.class), Mockito.any(RowCallbackHandler.class));

        sysParamSynchronizer.collection = Mockito.mock(MongoCollection.class);
        FindIterable<Document> findIterable = Mockito.mock(FindIterable.class);
        Mockito.when(sysParamSynchronizer.collection.find(Mockito.any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(new Document());

        SysParamSynchronizer spy = Mockito.spy(sysParamSynchronizer);
        Mockito.doReturn(null).when(spy).getMasterRecord(Mockito.any());

        // Call the sync method
        spy.sync(keySet);

        // Verify that the replaceOne method was called with the correct parameters
        Mockito.verify(sysParamSynchronizer.collection, times(0)).replaceOne(Mockito.any(Bson.class), Mockito.any(Document.class), Mockito.any(ReplaceOptions.class));
    }
    
    @Test
    public void testDelete() {
        // Create a mock class
        SysParamSynchronizer sysParamSynchronizer = new SysParamSynchronizer(
                Table.SYS_PARM, mockNamedParameterJdbcTemplate, mockMongodb);
        sysParamSynchronizer.collection = Mockito.mock(MongoCollection.class);
        DeleteResult deleteResult = DeleteResult.acknowledged(1);
        Mockito.when(sysParamSynchronizer.collection.deleteMany(Mockito.any(Bson.class))).thenReturn(deleteResult);

        SysParamSynchronizer spy = Mockito.spy(sysParamSynchronizer);
        Set<String> set = Collections.singleton("1");

        // Call the delete method
        spy.delete(set);

        // Verify that the deleteMany method was called with the correct parameters
        Mockito.verify(sysParamSynchronizer.collection).deleteMany(Mockito.any(Bson.class));
    }
}
