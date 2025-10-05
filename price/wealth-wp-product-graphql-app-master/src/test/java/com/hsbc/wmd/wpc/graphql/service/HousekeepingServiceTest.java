package com.dummy.wmd.wpc.graphql.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HousekeepingServiceTest {

    @InjectMocks
    HousekeepingService housekeepingService;
    @Mock
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Before
    public void setUp() {
        ReflectionTestUtils.setField(housekeepingService, "namedParameterJdbcTemplate", namedParameterJdbcTemplate);
    }


    @Test
    public void testCleanBatchJobLog() throws Exception {
        when(namedParameterJdbcTemplate.update(anyString(),anyMap())).thenReturn(1);
        long result = housekeepingService.cleanBatchJobLog(30);
        Assert.assertEquals(1L, result);
    }
}

