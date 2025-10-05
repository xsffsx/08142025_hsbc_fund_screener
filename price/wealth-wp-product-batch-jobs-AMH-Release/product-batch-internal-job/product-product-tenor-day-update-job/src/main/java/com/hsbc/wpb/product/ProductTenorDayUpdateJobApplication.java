package com.dummy.wpb.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Product tenor-in day update job
 */
@SpringBootApplication
public class ProductTenorDayUpdateJobApplication {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ProductTenorDayUpdateJobApplication.class, args)));
    }
}