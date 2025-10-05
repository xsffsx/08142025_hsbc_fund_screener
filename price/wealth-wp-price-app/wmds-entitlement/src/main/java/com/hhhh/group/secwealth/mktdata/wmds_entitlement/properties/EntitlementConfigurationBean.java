/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_entitlement.properties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntitlementConfigurationBean {

    private List<String> checklist = new ArrayList<>();

    private LinkedHashMap<String, List<String>> whiteListRules = new LinkedHashMap<>();
}
