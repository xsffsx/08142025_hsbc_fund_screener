package com.hhhh.group.secwealth.mktdata.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController  {
    
    @GetMapping(value="/greeting")
    public String greeting() {
        return "Hello, This is wmds-fund-app!";
    }

    @GetMapping(value="/actuator/health")
    public String greet1ing() {
        return "Hello, This is wmds-fund-app!";
    }
    @GetMapping(value="/")
    public String greet3ing() {
        return "Hello, This is wmds-fund-app!";
    }
    
}
