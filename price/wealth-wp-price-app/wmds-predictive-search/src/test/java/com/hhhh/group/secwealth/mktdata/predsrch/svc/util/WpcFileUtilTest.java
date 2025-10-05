package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;

import org.junit.Test;

import java.io.File;

public class WpcFileUtilTest {


    @Test
    public void testConverOutFileNameToTimestamp() {
        Boolean flag = true;
        try {
            WpcFileUtil.converOutFileNameToTimestamp("test.log");
            WpcFileUtil.converOutFileNameToTimestamp("test_test_test_test_test_test_test_test_test_test_test_test.log");
        } catch (Exception e) {
            flag = false;
        }
         org.junit.Assert.assertTrue(flag);
    }


    @Test
    public void testOutFileFilter() {
        Boolean flag = true;
        try {
            WpcFileUtil.OutFileFilter outFileFilter = new WpcFileUtil.OutFileFilter("test");
            outFileFilter.accept(new File("./test"), "test");
            WpcFileUtil.OutFileFilter fileFilter = new WpcFileUtil.OutFileFilter();
            outFileFilter.accept(new File("./test"), "test");
        } catch (Exception e) {
            flag = false;
        }
         org.junit.Assert.assertTrue(flag);
    }

    @Test
    public void testWpcOutFileNameComprator() {
        Boolean flag = false;
        try {
            WpcFileUtil.WpcOutFileNameComprator wpcOutFileNameComprator = new WpcFileUtil.WpcOutFileNameComprator();
            wpcOutFileNameComprator.compare("test.log","test_test_test_test_test_test.log");
        } finally {
            flag = true;
        }
         org.junit.Assert.assertTrue(flag);
    }


    @Test
    public void testWpcOutFileComprator() {
        Boolean flag = true;
        try {
            WpcFileUtil.WpcOutFileComprator wpcOutFileComprator = new WpcFileUtil.WpcOutFileComprator();
            wpcOutFileComprator.compare(new File("test.log"),new File("test_test_test_test_test_test.log"));
        } catch (Exception e) {
            flag = false;
        }
         org.junit.Assert.assertTrue(flag);
    }

    @Test
    public void testGetTime() {
        Boolean flag = true;
        try {
            WpcFileUtil.getTime("test_test_test_test_test_test.log");
        } catch (Exception e) {
            flag=false;
        }
         org.junit.Assert.assertTrue(flag);
    }



    @Test
    public void testWpcFilenameFilter() {
        Boolean flag = true;
        try {
            WpcFileUtil.WpcFilenameFilter wpcOutFileComprator = new WpcFileUtil.WpcFilenameFilter("./test");
            wpcOutFileComprator.accept(new File("./test"),"test");
        } catch (Exception e) {
            flag=false;
        }
         org.junit.Assert.assertTrue(flag);
    }

}
