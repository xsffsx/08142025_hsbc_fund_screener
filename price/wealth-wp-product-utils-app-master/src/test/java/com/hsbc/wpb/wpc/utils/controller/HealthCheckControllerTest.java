package com.dummy.wpb.wpc.utils.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.wpc.utils.MockUtils;
import com.dummy.wpb.wpc.utils.load.ProductCompareLoader;
import com.dummy.wpb.wpc.utils.load.ProductDataReLoader;
import com.dummy.wpb.wpc.utils.service.AdminLogService;
import com.dummy.wpb.wpc.utils.service.HealthCheckService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckControllerTest {


    @InjectMocks
    private HealthCheckController  healthCheckController;

    @Mock
    private HealthCheckService healthCheckService;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        ReflectionTestUtils.setField(healthCheckController, "healthCheckService", healthCheckService);
    }

    @Test
    public void testHealthCheckScopes() {
        try {
            healthCheckController.healthCheckScopes();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testHealthCheck() {
        try {
            healthCheckController.healthCheck("RestAdapter", new HashMap<>());
        } catch (Exception e) {
            fail();
        }
    }

}
