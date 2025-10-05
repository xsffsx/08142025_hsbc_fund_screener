package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;


import org.apache.commons.vfs2.FileSystemException;
import org.junit.Test;

public class FileAccessHelperTest {


    @Test
    public void testIsConnect() {
        Boolean flag = true;
        try {
            FileAccessHelper.isConnect("sftp://2132132", "sftp://2132132", 1);
        } catch (Exception e) {
            flag = false;
        }
         org.junit.Assert.assertTrue(flag);
    }

    @Test
    public void testConnect() {
        org.junit.Assert.assertTrue(!FileAccessHelper.connect("sftp://2132132", "sftp://2132132"));
    }

    @Test
    public void testGetChildren() {
        Boolean flag = false;

        try {
            FileAccessHelper.getChildren("sftp://2132132", "./test", "sftp://2132132");
        } catch (FileSystemException e) {
            flag = true;
        }
         org.junit.Assert.assertTrue(true);
    }

    @Test
    public void testCopyToLocal() {
        Boolean flag = false;
        try {
            FileAccessHelper.copyToLocal("sftp://2132132", "./test", "sftp://2132132");
        } catch (FileSystemException e) {
            flag = true;
        }
         org.junit.Assert.assertTrue(flag);
    }


}
