package com.hhhh.group.secwealth.mktdata.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@ImportResource(locations={"classpath:spring/applicationContext.xml"})
@ServletComponentScan("com.hhhh.group.secwealth.mktdata.common.filter")
//@ComponentScan(value= {"com.hhhh.group.secwealth.mktdata"})
//@EnableTransactionManagement
//@EnableScheduling
//@EnableEurekaClient
@Profile("test")
public class ApplicationTest {
    
    public static void main(String[] args) {
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
        System.setProperty("org.apache.xml.security.ignoreLineBreaks", "true");
        SpringApplication.run(ApplicationTest.class, args);
    }
}

