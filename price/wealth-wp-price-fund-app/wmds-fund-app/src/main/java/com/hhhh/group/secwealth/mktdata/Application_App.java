package com.hhhh.group.secwealth.mktdata;

import com.hhhh.wealth.feature.annotation.FeatureTogglesScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@FeatureTogglesScan(basePackages = "com.hhhh" )
@SpringBootApplication
@ImportResource(locations={"classpath:spring/applicationContext.xml"})
@ServletComponentScan("com.hhhh.group.secwealth.mktdata.common.filter")
//@ComponentScan(value= {"com.hhhh.group.secwealth.mktdata"})
//@EnableTransactionManagement
//@EnableScheduling
@EnableEurekaClient
@Profile("!test")
public class Application_App {
    
    public static void main(String[] args) {
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
        System.setProperty("org.apache.xml.security.ignoreLineBreaks", "true");
        SpringApplication.run(Application_App.class, args);
    }
}

