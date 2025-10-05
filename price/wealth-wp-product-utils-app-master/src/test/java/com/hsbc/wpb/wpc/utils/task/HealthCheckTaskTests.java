package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.service.HealthCheckService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckTaskTests {

    @InjectMocks
    private HealthCheckTask healthCheckTask;
    @Mock
    private HealthCheckService healthCheckService;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(healthCheckTask, "healthCheckService", healthCheckService);
    }

    @Test
    public void testRun() {
        try {
            healthCheckTask.run();
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
