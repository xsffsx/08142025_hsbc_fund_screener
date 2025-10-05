package com.dummy.wpb.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class ImportProductPriceXmlJobApplication {

    public static final String JOB_NAME = "Import Product Price XML Job";

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ImportProductPriceXmlJobApplication.class, args)));
    }
}