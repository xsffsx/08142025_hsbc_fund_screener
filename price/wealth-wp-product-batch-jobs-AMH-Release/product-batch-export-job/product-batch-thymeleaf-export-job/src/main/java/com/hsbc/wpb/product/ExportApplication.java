package com.dummy.wpb.product;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class ExportApplication {
    public static void main(String[] args) {
        if (args.length < 1){
            throw new IllegalArgumentException("Please just provide the config path to the export request file.");
        }
        System.exit(SpringApplication.exit(SpringApplication.run(ExportApplication.class, args)));
    }
}
