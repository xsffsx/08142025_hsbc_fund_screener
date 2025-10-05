package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SysParamLoaderTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private MongoCollection<Document> mockCollection;

    private SysParamLoader sysParamLoader;

    private MockedStatic<DbUtils> mockedDbUtils;


    @Before
    public void setUp() {
        when(mockMongoDatabase.getCollection(CollectionName.sys_parm)).thenReturn(mockCollection);
        sysParamLoader = new SysParamLoader(mockNamedParameterJdbcTemplate, mockMongoDatabase);
        mockedDbUtils = mockStatic(DbUtils.class);
    }

    @After
    public void tearDown() {
        mockedDbUtils.close();
    }

    @Test
    public void testLoad_whenCollectionNotEmpty_dropsCollectionAndLoadsData() {
        when(mockCollection.countDocuments()).thenReturn(10L);

        doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(1);
            ResultSet mockResultSet = mock(ResultSet.class);
            callbackHandler.processRow(mockResultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(any(String.class), any(RowCallbackHandler.class));

        Map<String, Object> mockRow = new HashMap<>();
        mockRow.put("rowid", "1");
        mockRow.put("key", "value");
        mockedDbUtils.when(() -> DbUtils.getStringObjectMap(any())).thenReturn(mockRow);

        sysParamLoader.load();

        verify(mockCollection).drop();
        verify(mockCollection, atLeastOnce()).insertMany(anyList());
    }

    @Test
    public void testLoad_whenCollectionEmpty_loadsData() {
        when(mockCollection.countDocuments()).thenReturn(0L);

        doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(1);
            ResultSet mockResultSet = mock(ResultSet.class);
            callbackHandler.processRow(mockResultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(any(String.class), any(RowCallbackHandler.class));

        Map<String, Object> mockRow = new HashMap<>();
        mockRow.put("rowid", "1");
        mockRow.put("key", "value");
        mockedDbUtils.when(() -> DbUtils.getStringObjectMap(any())).thenReturn(mockRow);

        sysParamLoader.load();

        verify(mockCollection, never()).drop();
        verify(mockCollection, atLeastOnce()).insertMany(anyList());
    }
}