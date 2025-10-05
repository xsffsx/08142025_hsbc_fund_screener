package com.dummy.wpb.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * termsheet PDF file upload job
 */
@Slf4j
@SpringBootApplication
public class ImportTermsheetJobApplication {

    public static final String JOB_NAME = "Import SID Termsheet PDF Job";

    /**
     * termsheet PDF file upload job
     * @param args ctryRecCde grpMembrRecCde prodTypeCde finDocPath isPostProduct
     *             <br/>(e.g. GB HBEU SID "/appvol/product/data/incoming/findoc/GBHBEU/SID" Y)
     */
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ImportTermsheetJobApplication.class, args)));
    }
}
