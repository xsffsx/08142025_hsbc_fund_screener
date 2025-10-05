package com.dummy.wmd.wpc.graphql;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@SuppressWarnings({"java:S2696", "java:S2209"})
@Configuration
public class ApplicationContextConfig implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static String getConfig(String key) {
        Environment env = applicationContext.getEnvironment();
        return env.getProperty(key);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
