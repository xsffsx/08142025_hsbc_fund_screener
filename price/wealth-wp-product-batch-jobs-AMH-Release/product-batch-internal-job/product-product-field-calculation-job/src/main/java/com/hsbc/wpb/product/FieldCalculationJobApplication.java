package com.dummy.wpb.product;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.util.Properties;

@SpringBootApplication
@EnableBatchProcessing
public class FieldCalculationJobApplication {
    public static void main(String[] args) {
        Properties properties = StringUtils.splitArrayElementsIntoProperties(args, "=");
        if(properties == null){
            throw new IllegalArgumentException("Please pass in these parameter: ctryRecCde, grpMembrRecCde and calculatedField.");
        }
        String ctryRecCde = properties.getProperty("ctryRecCde");
        String grpMembrRecCde = properties.getProperty("grpMembrRecCde");
        String calculatedField = properties.getProperty("calculatedField");
        if (org.apache.commons.lang3.StringUtils.isAnyBlank(calculatedField, ctryRecCde, grpMembrRecCde)) {
            throw new IllegalArgumentException("Please pass in these parameter: ctryRecCde, grpMembrRecCde and calculatedField.");
        }
        System.exit(SpringApplication.exit(SpringApplication.run(FieldCalculationJobApplication.class, args)));
    }
}