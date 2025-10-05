package com.hhhh.group.secwealth.mktdata.test.config;

import org.junit.Before;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
    classes = {CommonConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class}
)
public class ContextInitializationStepDefinition {
    public ContextInitializationStepDefinition() {
    }

    @Before
    public void initializeSpringContext() {
    }
}
