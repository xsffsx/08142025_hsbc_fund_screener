package com.dummy.wpb.wpc.utils.controller;

import com.dummy.wpb.wpc.utils.service.HealthCheckService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
public class HealthCheckController {

    @Autowired
    private HealthCheckService healthCheckService;

    @GetMapping(value = "/healthCheckScopes")
    public Document healthCheckScopes() {
        return healthCheckService.healthCheckScopes();
    }

    @PostMapping(value = "/healthCheck/{service}")
    public ResponseEntity<Document> healthCheck(@PathVariable("service") String service,
                                                @RequestBody(required = false) Map<String, String> parameters) {
        return ResponseEntity.ok().body(healthCheckService.healthCheck(service, parameters, false));
    }

}
