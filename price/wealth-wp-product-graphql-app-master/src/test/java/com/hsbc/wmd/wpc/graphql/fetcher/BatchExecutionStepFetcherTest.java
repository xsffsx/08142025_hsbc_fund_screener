package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.model.BatchJobExecutionStep;
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class BatchExecutionStepFetcherTest {

    @InjectMocks
    private BatchExecutionStepFetcher batchExecutionStepFetcher;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsPagination() {
        Map<String, Object> filterMap = new HashMap<>();
         Map<String, Object> sortMap = new HashMap<>();
        sortMap.put("A", 1);
        Mockito.when(environment.getArgument(anyString())).thenReturn(filterMap). thenReturn(sortMap).thenReturn(1).thenReturn(null);
        Mockito.when(environment.getArgument("jobExecutionId")).thenReturn(1L);
        Mockito.when(namedParameterJdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(new ArrayList<>());
        Pagination<BatchJobExecutionStep> batchJobExecutionStepPagination = batchExecutionStepFetcher.get(environment);
        Assert.assertNotNull(batchJobExecutionStepPagination);
    }
}
