package com.dummy.wpb.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Product status & indicator update job
 */
@SpringBootApplication
public class ProductStatusUpdateJobApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ProductStatusUpdateJobApplication.class, args)));
    }
}
