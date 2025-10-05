package com.hhhh.group.secwealth.mktdata.test.hook;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ScenarioHooks {

    @Before(order = 0)
    public void printScenarioName(Scenario scenario) {
        String featureName = scenario.getSourceTagNames().toString();
        String scenarioName = scenario.getName();
        log.info("Running Test Feature: {}, Scenario: [{}]", featureName, scenarioName);
    }

}
