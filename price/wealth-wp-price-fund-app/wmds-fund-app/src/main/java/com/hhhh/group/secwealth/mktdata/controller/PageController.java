package com.hhhh.group.secwealth.mktdata.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {
    
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String helloE2e(Map<String, Object> map) {
        return "index";
    }
    
}
