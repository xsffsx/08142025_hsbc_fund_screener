package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.model.DataChangeEvent;
import com.dummy.wpb.wpc.utils.model.MongoIndexInfo;
import com.dummy.wpb.wpc.utils.model.ProductEventGroup;
import com.dummy.wpb.wpc.utils.model.SyncLog;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SyncServiceTests {

    @Mock
    private MongoTemplate mockMongoTemplate;
    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoCollection<Document> collection;

    @InjectMocks
    private SyncService syncService;

    @Before
    public void setUp() throws Exception {
        syncService = new SyncService(mockMongoTemplate);
        ReflectionTestUtils.setField(syncService, "namedParameterJdbcTemplate",
                mockNamedParameterJdbcTemplate);
    }

    @Test
    public void testRetrieveProductChangeEvents_givenDate_returnsResult() {
        when(mockNamedParameterJdbcTemplate.queryForObject(
                eq("select count(1) as eventCount from DATA_CHANGE_EVENT t where TS between :countStartTime AND :processStartTime AND OP_Table in (:productTables)"),
                any(SqlParameterSource.class), eq(Integer.class))).thenReturn(0);
        when(mockNamedParameterJdbcTemplate.queryForObject(
                eq("select min(ts) as eventStartTime, max(ts) as eventEndTime, count(1) as eventCount from DATA_CHANGE_EVENT t where TS < :processStartTime AND OP_Table in (:productTables)"),
                any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(new ProductEventGroup());
        when(mockNamedParameterJdbcTemplate.queryForList(
                eq("select distinct PROD_ID from DATA_CHANGE_EVENT t where TS < :processStartTime AND OP_Table in (:productTables)"),
                any(SqlParameterSource.class), eq(Long.class))).thenReturn(Arrays.asList(0L, 1L));
        ProductEventGroup result = syncService.retrieveProductChangeEvents(new Date());
        assertThat(result).isNotNull();
    }

    @Test
    public void testRetrieveProductChangeEvents_givenDate_returnsNull() {
        when(mockNamedParameterJdbcTemplate.queryForObject(
                eq("select count(1) as eventCount from DATA_CHANGE_EVENT t where TS between :countStartTime AND :processStartTime AND OP_Table in (:productTables)"),
                any(SqlParameterSource.class), eq(Integer.class))).thenReturn(6001);
        when(mockNamedParameterJdbcTemplate.queryForObject(
                eq("select min(ts) as eventStartTime from DATA_CHANGE_EVENT t where TS < :processStartTime AND OP_Table in (:productTables)"),
                any(SqlParameterSource.class), eq(Date.class))).thenReturn(new Date());
        ProductEventGroup result = syncService.retrieveProductChangeEvents(new Date());
        assertThat(result).isNull();
    }


    @Test
    public void testDeleteProductChangeEvents() {
        // Setup
        ProductEventGroup group = new ProductEventGroup();
        when(mockNamedParameterJdbcTemplate.update(
                eq("delete from DATA_CHANGE_EVENT t where TS < :processStartTime AND OP_Table in (:productTables)"),
                any(SqlParameterSource.class))).thenReturn(100);
        // Run the test
        group.setEventCount(100);
        try {
            syncService.deleteProductChangeEvents(group);
        } catch (Exception e) {
            Assert.fail();
        }
        group.setEventCount(99);
        try {
            syncService.deleteProductChangeEvents(group);
        } catch (Exception e) {
            Assert.fail();
        }
    }


    @Test
    public void testRetrieveNonProductChangeEvents_givenDate_returnsDataChangeEventList() {
        // Setup
        DataChangeEvent dataChangeEvent = new DataChangeEvent();
        final List<DataChangeEvent> dataChangeEvents = Arrays.asList(dataChangeEvent);
        when(mockNamedParameterJdbcTemplate.query(
                eq("select ROWID as ID, t.OP_TABLE as TABLE_NAME, t.OP, t.ROW_ID, t.TS, t.PROD_ID from DATA_CHANGE_EVENT t where TS < :processStartTime AND ROWNUM <= 1000 AND OP_TABLE not in (:productTables) ORDER BY TS"),
                any(SqlParameterSource.class), any(BeanPropertyRowMapper.class))).thenReturn(dataChangeEvents);
        // Run the test
        List<DataChangeEvent> result = syncService.retrieveNonProductChangeEvents(new Date());
        // Verify the results
        assertThat(result).isNotNull();
    }

    @Test
    public void testDeleteDataChangeEvents_givenDataChangeEvents_returnsNull() {
        // Setup
        DataChangeEvent dataChangeEvent = new DataChangeEvent();
        List<DataChangeEvent> events = Arrays.asList(dataChangeEvent);
        // Run the test
        syncService.deleteDataChangeEvents(events);
        // Verify the results
        verify(mockNamedParameterJdbcTemplate).update(eq("delete from DATA_CHANGE_EVENT where ROWID in (:rowids)"),
                any(SqlParameterSource.class));
    }

    @Test
    public void testWriteDeltaSyncLog_givenSyncLog_returnsNull() {
        syncService.writeDeltaSyncLog(new SyncLog());
        verify(mockMongoTemplate).insert(new SyncLog());
    }

    @Test
    public void testRemoveEventsByTableNames_givenTableNames_returnsNull() {
        try {
            syncService.removeEventsByTableNames(Arrays.asList("value"));
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
