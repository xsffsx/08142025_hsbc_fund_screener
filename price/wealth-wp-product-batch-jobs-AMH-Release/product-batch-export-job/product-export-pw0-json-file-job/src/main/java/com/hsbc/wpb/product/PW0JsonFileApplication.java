package com.dummy.wpb.product;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class PW0JsonFileApplication {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(PW0JsonFileApplication.class, args)));
    }
}