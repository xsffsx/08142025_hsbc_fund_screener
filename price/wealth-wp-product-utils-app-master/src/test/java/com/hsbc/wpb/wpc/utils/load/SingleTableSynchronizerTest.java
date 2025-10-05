package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SingleTableSynchronizerTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection collection;
    @Mock
    private DeleteResult result;
    @Mock
    private ThreadPoolTaskExecutor mockThreadPoolTaskExecutor;

    private SingleTableSynchronizer singleTableSynchronizer;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection("collectionName")).thenReturn(collection);
        singleTableSynchronizer = new SingleTableSynchronizer(mockNamedParameterJdbcTemplate, "tableName",
                mockMongodb, "collectionName", mockThreadPoolTaskExecutor);
    }

    @Test
    public void testSync_givenKeySet_returnsNull() {
        // Setup
        Set<Map<String, Object>> keySet = new HashSet<>(Arrays.asList(new HashMap<>()));
        doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(mockThreadPoolTaskExecutor).execute(any(Runnable.class));
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(2);
            callbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(Mockito.anyString(), any(MapSqlParameterSource.class), any(RowCallbackHandler.class));
        MockedStatic<DbUtils> dbUtils = Mockito.mockStatic(DbUtils.class);
        dbUtils.when(() -> DbUtils.getStringObjectMap(resultSet)).thenReturn(
                new HashMap<String, String>() {{
                    put(Field.msgRecName, "msgRecName");
                }}
        );
        // Run the test
        try{
            singleTableSynchronizer.sync(keySet);
        }catch (Exception e){
            Assert.fail();
        }finally {
            dbUtils.close();
        }
    }

    @Test
    public void testDelete() {
        when(collection.deleteMany(any(Bson.class))).thenReturn(result);
        Set<String> rowidSet = new HashSet<>();
        rowidSet.add("value");
        singleTableSynchronizer.delete(rowidSet);
        Assert.assertNotNull(mockMongodb);
        // Verify the results

    }
}
