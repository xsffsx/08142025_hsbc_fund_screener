package com.dummy.wpb.product;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class WimEgressJobApplication {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(WimEgressJobApplication.class, args)));
    }
}