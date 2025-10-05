package com.dummy.wpb.product.framework;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.*;

import static org.apache.commons.lang.StringUtils.EMPTY;

public class ExcludeMongoAutoConfigEnvPostProcessor implements EnvironmentPostProcessor {
    private static final String PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE = "spring.autoconfigure.exclude";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String excludes = environment.getProperty(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, EMPTY);
        Set<String> excludeSet = new TreeSet<>(Arrays.asList(StringUtils.split(excludes,",")));
        excludeSet.add("org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration");
        excludeSet.add("org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration");

        Map<String, Object> map = Collections.singletonMap(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, StringUtils.join(excludeSet.iterator(), ","));
        MapPropertySource mapPropertySource = new MapPropertySource("excludeMongoAutoConfig", map);
        environment.getPropertySources().addFirst(mapPropertySource);
    }
}
