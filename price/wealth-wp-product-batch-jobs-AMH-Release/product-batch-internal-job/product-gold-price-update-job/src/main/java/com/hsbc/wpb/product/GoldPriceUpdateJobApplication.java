package com.dummy.wpb.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GoldPriceUpdateJobApplication {

    public static final String JOB_NAME = "Generate Gold Price XML Job";

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(GoldPriceUpdateJobApplication.class, args)));
    }
}
