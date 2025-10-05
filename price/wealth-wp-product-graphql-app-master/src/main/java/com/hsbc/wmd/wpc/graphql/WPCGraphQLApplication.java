package com.dummy.wmd.wpc.graphql;


import com.dummy.wmd.wpc.graphql.task.HousekeepingTask;
import com.dummy.wmd.wpc.graphql.task.RequestLogTask;
import com.dummy.wmd.wpc.graphql.utils.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Slf4j
@SpringBootApplication
@EnableCaching
@EnableRetry
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class WPCGraphQLApplication {
    @Autowired
    private HousekeepingTask housekeepingTask;
    @Autowired
    private RequestLogTask requestLogTask;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    public static void main(String[] args) {
        // decrypt encrypted properties in the system properties, like -Dmy.password="{cipher}98fe61d81c3c595928e79eebc8ef4d7339d4a5ee4125ae927ef2e72c03fb2536"
        CryptoUtil.decrypt(System.getProperties());
        SpringApplication.run(WPCGraphQLApplication.class, args);
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("Task Thread ");
        return threadPoolTaskScheduler;
    }

    @PostConstruct
    private void startLogAndLaunchThread(){
        taskScheduler.scheduleAtFixedRate(housekeepingTask, Duration.ofDays(1));		// housekeeping every 24 hours
        log.info("Housekeeping Task is scheduled at 24 hours interval");

        new Thread(requestLogTask, "request log").start();
        log.info("RequestLog Task Thread is up");
    }
}

