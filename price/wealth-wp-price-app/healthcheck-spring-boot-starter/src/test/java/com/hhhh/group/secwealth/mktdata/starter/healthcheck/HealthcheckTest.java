/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.hhhh.group.secwealth.mktdata.starter.healthcheck.configuration.HealthcheckAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HealthcheckAutoConfiguration.class)
@EnableWebSecurity
@EnableWebMvc
public class HealthcheckTest {

    @Test
    public void testLoad() {}

}
