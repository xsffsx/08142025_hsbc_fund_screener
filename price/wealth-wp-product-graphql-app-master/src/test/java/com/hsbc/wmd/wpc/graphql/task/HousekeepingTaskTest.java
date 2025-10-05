package com.dummy.wmd.wpc.graphql.task;

import com.dummy.wmd.wpc.graphql.service.HousekeepingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HousekeepingTaskTest {

    @Mock
    private HousekeepingService housekeepingService;
    @InjectMocks
    private HousekeepingTask housekeepingTask;
    private int documentRevisionKeepDays = 7;
    private int requestLogKeepDays = 7;

    private int msgProcessLogKeepDays = 7;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(housekeepingTask, "housekeepingService", housekeepingService);
        ReflectionTestUtils.setField(housekeepingTask, "documentRevisionKeepDays", documentRevisionKeepDays);
        ReflectionTestUtils.setField(housekeepingTask, "requestLogKeepDays", requestLogKeepDays);
        ReflectionTestUtils.setField(housekeepingTask, "msgProcessLogKeepDays", msgProcessLogKeepDays);
    }

    @Test
    public void testRun() {
        // Setup
        when(housekeepingService.cleanByBatch(any(String.class), any(String.class), any(int.class))).thenReturn(0L);
        when(housekeepingService.cleanBatchJobLog(any(int.class))).thenReturn(0L);
        // Run the test
        try {
            housekeepingTask.run();
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
