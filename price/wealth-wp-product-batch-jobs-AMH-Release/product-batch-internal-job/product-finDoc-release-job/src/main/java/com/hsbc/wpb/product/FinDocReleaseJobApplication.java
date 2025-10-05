package com.dummy.wpb.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@EnableBatchProcessing
public class FinDocReleaseJobApplication {
    public static final String JOB_NAME = "Financial Document Release Job";

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(FinDocReleaseJobApplication.class, args)));
    }
}