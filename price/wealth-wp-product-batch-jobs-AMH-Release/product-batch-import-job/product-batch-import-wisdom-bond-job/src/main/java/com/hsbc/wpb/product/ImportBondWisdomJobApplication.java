package com.dummy.wpb.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImportBondWisdomJobApplication {

    public static final String JOB_NAME = "Import BOND Wisdom XML Job";

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ImportBondWisdomJobApplication.class, args)));
    }
}
