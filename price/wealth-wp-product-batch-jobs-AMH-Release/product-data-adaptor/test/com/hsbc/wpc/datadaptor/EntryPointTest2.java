package com.dummy.wpc.datadaptor;

import java.io.File;

import org.apache.xmlbeans.impl.tool.XSTCTester.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class EntryPointTest2 extends TestCase {
    
    @BeforeClass
    public static void beforeClass() {
        System.setProperty("configPath", "testing/configFile");
    }
    
    @Test
    public void testMANLPRODNR() {
        
        String[] args = new String[] {
            "AE", "BBME", "MANUAL.PROD.UT.NR"
        };
        EntryPoint.main(args);
    }
    
    @Test
    public void testMANLPriceNR() {
        
        String[] args = new String[] {
            "AE", "BBME", "MANUAL.PRICE"
        };
        EntryPoint.main(args);
    }
    
    @Test
    public void testMANLPROD() {
        
        String[] args = new String[] {
            "AE", "BBME", "MANUAL.PROD.UT"
        };
        EntryPoint.main(args);
    }
    
    @Test
    public void testMANLPrice() {
        
        String[] args = new String[] {
            "AE", "BBME", "MANUAL.PRICE"
        };
        EntryPoint.main(args);
    }
    
    @Test
    public void testMSPrice() {
        
        String[] args = new String[] {
            "AE", "BBME", "MS"
        };
        
        EntryPoint.main(args);
    }
    
    @AfterClass
    public static void afterClass() {
        String outPath = "C:/WPCTest/adaptor/output/";
        String[] sites = {
            "AEBBME", "QABBME", "BHBBME", "OMBBME", "JOBBME", "LBBBME"
        };
        for (String site : sites) {
            File fl = new File(outPath + site);
            System.out.println("=== " + outPath + site + " ===");
            try {
                for (String name : fl.list()) {
                    System.out.println(name);
                }
            } catch (Exception e) {
            }
            
        }
    }
}
