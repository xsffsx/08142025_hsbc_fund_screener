package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.model.BatchJobExecutionParams;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class BatchExecutionParamsFetcherTest {

    @InjectMocks
    private BatchExecutionParamsFetcher batchExecutionParamsFetcher;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsBatchJobExecutionParamsList() {
        Mockito.when(environment.getArgument("jobExecutionId")).thenReturn(1L);
        Mockito.when(namedParameterJdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(new ArrayList<>());
        List<BatchJobExecutionParams> batchJobExecutionParams = batchExecutionParamsFetcher.get(environment);
        Assert.assertNotNull(batchJobExecutionParams);
    }
}
