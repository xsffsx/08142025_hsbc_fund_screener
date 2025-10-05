package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.model.BatchJobExecution;
import com.dummy.wmd.wpc.graphql.model.Pagination;
import graphql.schema.DataFetchingEnvironment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class BatchExecutionFetcherTest {

    @InjectMocks
    private BatchExecutionFetcher batchExecutionFetcher;
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Mock
    private DataFetchingEnvironment environment;

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsPagination1() throws ParseException {
        prepareMockData(true);
        Pagination<BatchJobExecution> batchJobExecutionPagination = batchExecutionFetcher.get(environment);
        Assert.assertNotNull(batchJobExecutionPagination);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsPagination2() throws ParseException {
        prepareMockData(false);
        Pagination<BatchJobExecution> batchJobExecutionPagination = batchExecutionFetcher.get(environment);
        Assert.assertNotNull(batchJobExecutionPagination);
    }
   @Test
    public void testGet_givenDataFetchingEnvironment_returnsPagination3() throws ParseException {
       Map<String, Object> filterMap = prepareMockData(true);
       filterMap.remove("dateFrom");
       Pagination<BatchJobExecution> batchJobExecutionPagination = batchExecutionFetcher.get(environment);
        Assert.assertNotNull(batchJobExecutionPagination);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsPagination4() throws ParseException {
        Map<String, Object> filterMap = prepareMockData(true);
        filterMap.remove("dateTo");
        Pagination<BatchJobExecution> batchJobExecutionPagination = batchExecutionFetcher.get(environment);
        Assert.assertNotNull(batchJobExecutionPagination);
    }

    private  Map<String, Object> prepareMockData(Boolean isContain) {
        Map<String, Object> filterMap = new HashMap<>();
        if(isContain) {
            filterMap.put("status", "FAILED");
            filterMap.put("jobName", "jobName");
            filterMap.put("systemCde", "MSUT");
            filterMap.put("dateFrom", "2024-05-13T16:00:00.000Z");
            filterMap.put("dateTo", "2024-05-15T16:03:00.000Z");
            filterMap.put("jobExecutionId", 1);
        } else{
            filterMap.put("A", "b");
        }
        Map<String, Object> sortMap = new HashMap<>();
        sortMap.put("A", 1);
        Mockito.when(environment.getArgument(anyString())).thenReturn(filterMap).
                thenReturn(sortMap).thenReturn(1).thenReturn(null);
        Mockito.when(namedParameterJdbcTemplate.query(anyString(),any(MapSqlParameterSource.class), any(RowMapper.class))).thenReturn(new ArrayList<>());
        Mockito.when(namedParameterJdbcTemplate.queryForObject(anyString(),any(MapSqlParameterSource.class), any(Class.class))).thenReturn(1L);
        return filterMap;
    }
}
