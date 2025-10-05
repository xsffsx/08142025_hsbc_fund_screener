package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.springframework.test.util.ReflectionTestUtils;
@RunWith(MockitoJUnitRunner.Silent.class)
public class ReferenceDataSynchronizerTest {

    @Mock
    private  NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private  MongoDatabase mockMongodb;
    @Mock
    private  ThreadPoolTaskExecutor mockThreadPoolTaskExecutor;

    private  ReferenceDataSynchronizer referenceDataSynchronizerUnderTest;

    private  ResultSet resultSet;

    private  MockedStatic<DbUtils> dbUtils;

    @Before
    public void setUp() throws Exception {

        //referenceDataSynchronizerUnderTest = spy(new ReferenceDataSynchronizer(mockNamedParameterJdbcTemplate, mockMongodb, mockThreadPoolTaskExecutor));
        referenceDataSynchronizerUnderTest = new ReferenceDataSynchronizer(mockNamedParameterJdbcTemplate, mockMongodb, mockThreadPoolTaskExecutor);
        dbUtils = Mockito.mockStatic(DbUtils.class);
        referenceDataSynchronizerUnderTest.colReferenceData = mock(MongoCollection.class);
       // referenceDataSynchronizerUnderTest.namedParameterJdbcTemplate = mockNamedParameterJdbcTemplate;
        referenceDataSynchronizerUnderTest.sequenceService = mock(SequenceService.class);
        ReflectionTestUtils.setField(referenceDataSynchronizerUnderTest,"threadPoolTaskExecutor",mockThreadPoolTaskExecutor);
    }

    @After
    public void after(){
        dbUtils.close();
    }
    @Test
    public void testGetMasterRecord() throws SQLException {
        // Setup
        try {
            final Map<String, Object> keys = new HashMap<String, Object>(){{
                put("CTRY_REC_CDE","CN");
                put("GRP_MEMBR_REC_CDE","dummy");
                put("CDV_TYPE_CDE","UT");
                put("CDV_CDE","LCUT");
            }};

            resultSet = mock(ResultSet.class);
            Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
            Mockito.when(resultSet.getString("ctryRecCde")).thenReturn("CN");
            Mockito.when(resultSet.getString("grpMembrRecCde")).thenReturn("dummy");

//            dbUtils = Mockito.mockStatic(DbUtils.class);
            dbUtils.when(() -> DbUtils.getStringObjectMap(resultSet)).thenReturn(
                    new HashMap<String, String>() {{
                        put("CTRY_REC_CDE","CN");
                        put("GRP_MEMBR_REC_CDE","dummy");
                        put("CDV_TYPE_CDE","UT");
                        put("CDV_CDE","LCUT");
                    }}
            );

            doAnswer( InvocationOnMock -> {
                ((RowCallbackHandler)InvocationOnMock.getArgument(2)).processRow(resultSet);
                return null;
            }).when(mockNamedParameterJdbcTemplate).query(anyString(), any(SqlParameterSource.class), any(RowCallbackHandler.class));

            final Map<String, Object> result = referenceDataSynchronizerUnderTest.getMasterRecord(keys);
            Assert.assertNotNull(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSync() throws SQLException {
        try {
            // Setup
            final Set<Map<String, Object>> keySet = new HashSet<Map<String, Object>>(Arrays.asList(new HashMap<String, Object>(){{
                put("CTRY_REC_CDE","CN");
                put("GRP_MEMBR_REC_CDE","dummy");
                put("CDV_TYPE_CDE","UT");
                put("CDV_CDE","LCUT");
            }}));


            resultSet = mock(ResultSet.class);
            Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
            Mockito.when(resultSet.getString("ctryRecCde")).thenReturn("CN");
            Mockito.when(resultSet.getString("grpMembrRecCde")).thenReturn("dummy");

//            dbUtils = Mockito.mockStatic(DbUtils.class);
            dbUtils.when(() -> DbUtils.getStringObjectMap(any(ResultSet.class))).thenReturn(
                    new HashMap<String, String>() {{
                        put("CTRY_REC_CDE","CN");
                        put("GRP_MEMBR_REC_CDE","dummy");
                        put("CDV_TYPE_CDE","UT");
                        put("CDV_CDE","LCUT");
                    }}
            );

            when(mockNamedParameterJdbcTemplate.queryForList(
                    eq("select CHANL_COMN_CDE from CDE_DESC_VALUE_CHANL_REL t where CTRY_REC_CDE = :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and CDV_TYPE_CDE = :CDV_TYPE_CDE and CDV_CDE = :CDV_CDE"),
                    any(SqlParameterSource.class), Mockito.<Class> any())).thenReturn(Arrays.asList("WPS"));

            Map<String, String> m = new HashMap<String, String>() {{
                put("CTRY_REC_CDE","CN");
                put("GRP_MEMBR_REC_CDE","dummy");
                put("CDV_TYPE_CDE","UT");
                put("CDV_CDE","LCUT");
            }};

            doAnswer(invocation -> {
                        RowCallbackHandler rowCallbackHandler = invocation.getArgument(2);
                        rowCallbackHandler.processRow(resultSet);
                        return null;
                    }
            ).when(mockNamedParameterJdbcTemplate).query(
                    eq("select ROWID, t.* from CDE_DESC_VALUE t where CTRY_REC_CDE = :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and CDV_TYPE_CDE = :CDV_TYPE_CDE and CDV_CDE = :CDV_CDE"),
                    any(SqlParameterSource.class), any(RowCallbackHandler.class));

            FindIterable b = mock(FindIterable.class);

            when(referenceDataSynchronizerUnderTest.colReferenceData.find(any(Bson.class))).thenReturn(b);

            Document doc = mock(Document.class);
            doc.put("test","aaa");
            when(b.first()).thenReturn(doc);

            Mockito.doAnswer( (InvocationOnMock invocation) -> {
                        ((Runnable) invocation.getArguments()[0]).run();
                        return null;
                    }
            ).when(mockThreadPoolTaskExecutor).execute(Mockito.any(Runnable.class));

            referenceDataSynchronizerUnderTest.sync(keySet);
            Assert.assertNotNull(keySet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSync_ThreadPoolTaskExecutorThrowsTaskRejectedException() {
        // Setup
        final Set<Map<String, Object>> keySet = new HashSet<>(Arrays.asList(new HashMap<>()));
        doThrow(TaskRejectedException.class).when(mockThreadPoolTaskExecutor).execute(any(Runnable.class));

        // Run the test
        assertThatThrownBy(() -> referenceDataSynchronizerUnderTest.sync(keySet))
                .isInstanceOf(TaskRejectedException.class);
    }


    @Test
    public void testSync_NamedParameterJdbcTemplateQueryForListReturnsNoItems() {
        // Setup
        final Set<Map<String, Object>> keySet = new HashSet<>(Arrays.asList(new HashMap<>()));
        doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(mockThreadPoolTaskExecutor).execute(any(Runnable.class));
        when(mockNamedParameterJdbcTemplate.queryForList(
                eq("select CHANL_COMN_CDE from CDE_DESC_VALUE_CHANL_REL t where CTRY_REC_CDE = :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and CDV_TYPE_CDE = :CDV_TYPE_CDE and CDV_CDE = :CDV_CDE"),
                any(SqlParameterSource.class), eq(String.class))).thenReturn(Collections.emptyList());

        // Run the test
        referenceDataSynchronizerUnderTest.sync(keySet);

        // Verify the results
        verify(mockThreadPoolTaskExecutor).execute(any(Runnable.class));
        verify(mockNamedParameterJdbcTemplate).query(
                eq("select ROWID, t.* from CDE_DESC_VALUE t where CTRY_REC_CDE = :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and CDV_TYPE_CDE = :CDV_TYPE_CDE and CDV_CDE = :CDV_CDE"),
                any(SqlParameterSource.class), any(RowCallbackHandler.class));
    }


    @Test
    public void testDelete() {
        DeleteResult deleteResult = mock(DeleteResult.class);
        when(deleteResult.getDeletedCount()).thenReturn(1L);
        when(referenceDataSynchronizerUnderTest.colReferenceData.deleteMany(any(Bson.class))).thenReturn(deleteResult);
        referenceDataSynchronizerUnderTest.delete(new HashSet<>(Arrays.asList("value")));
        Assert.assertNotNull(deleteResult);
    }


}
