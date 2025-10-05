package com.dummy.wpb.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImportBondCharacterIndJonApplication {

    public static final String JOB_NAME = "Import GSOPS BOND CECS Interface Job";

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ImportBondCharacterIndJonApplication.class, args)));
    }
}