package com.dummy.wpb.product.jobs.registration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.dummy.wpb.product")
@EnableBatchProcessing
public class ProductRegistrationApplication {

	public static final String JOB_NAME = "Internal Product Registration In Smart Global Job";

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(ProductRegistrationApplication.class, args)));
	}
}
