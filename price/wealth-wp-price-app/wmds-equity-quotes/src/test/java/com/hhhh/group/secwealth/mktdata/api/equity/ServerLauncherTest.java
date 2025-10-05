package com.hhhh.group.secwealth.mktdata.api.equity;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.scheduled.ASharesStockScheduled;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan(excludeFilters= {@ComponentScan.Filter(type= FilterType.ANNOTATION, value= {EnableScheduling.class})})
public class ServerLauncherTest {

    @MockBean
    ASharesStockScheduled aSharesStockScheduled;

    public static void main(final String[] args) {
        SpringApplication.run(ServerLauncherTest.class, args);
    }


    @Test
    public void testServerLauncherTest() {
        Assert.assertNotNull("dummy test");
    }

}