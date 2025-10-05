package com.dummy.wpb.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImportInvestorCharacterIndApplication {

    public static final String JOB_NAME = "Import GSOPS IC Daily Delta Batch Job";

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ImportInvestorCharacterIndApplication.class, args)));
    }
}