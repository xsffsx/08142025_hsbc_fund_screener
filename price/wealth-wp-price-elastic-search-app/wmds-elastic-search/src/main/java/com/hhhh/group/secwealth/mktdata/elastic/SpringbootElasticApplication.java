package com.hhhh.group.secwealth.mktdata.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringbootElasticApplication {

    public static void main(String[] args) {
        initSystemProperty();
        SpringApplication.run(SpringbootElasticApplication.class, args);
    }

    private static void initSystemProperty() {
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow", "|{}");
     
    }

}