package com.dummy.wpb.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@Slf4j
@SpringBootApplication
public class SanctionUpdateJobApplication {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(SanctionUpdateJobApplication.class, args)));
    }
}
