/*
 */
package com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispatcherConfigurationBean {

    private List<String> requestParameters = new ArrayList<>();

    private LinkedHashMap<String, List<String>> rules = new LinkedHashMap<>();

    private Map<String, String> endPoint = new HashMap<>();
}