package com.hhhh.group.secwealth.mktdata.test.stepdefinitions;

import com.hhhh.group.secwealth.mktdata.test.ApplicationContext;
import com.hhhh.group.secwealth.mktdata.test.service.DistributedCacheService;
import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(classes = ApplicationContext.class)
public class CommonStepDefinition {

    @Autowired
    private DistributedCacheService distributedCacheService;

    @Given("^Customer has logged in$")
    public void customerLogin() {
        HttpStatus status = distributedCacheService.login();
        assertThat(204, is(status.value()));
    }

}
