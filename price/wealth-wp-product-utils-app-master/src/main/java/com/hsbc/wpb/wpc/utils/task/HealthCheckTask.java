package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.service.HealthCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HealthCheckTask implements Runnable {

    @Autowired
    private HealthCheckService healthCheckService;

    @Override
    public void run() {
        healthCheckService.healthCheckAll();
    }
}
