package com.hhhh.group.secwealth.mktdata.api.equity.chart.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "wealth/api/v1/market-data")
public class GreetingController {
	
	@RequestMapping(value = "/greeting", method = RequestMethod.GET)
	public String hello(Map<String, Object> map) {
		return "Hello, this is greeting !";
	}
	
}
