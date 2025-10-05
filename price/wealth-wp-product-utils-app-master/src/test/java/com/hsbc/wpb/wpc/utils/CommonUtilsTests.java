package com.dummy.wpb.wpc.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class CommonUtilsTests {

    @Test
    public void testReadResource_givenString_returnsString() {
        String resource = CommonUtils.readResource("/data/UT-40028320.json");
        Assert.assertNotNull(resource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadResource_givenString_throwsIllegalArgumentException() {
        CommonUtils.readResource("/data/UT-40028320.jso");
    }

    @Test(expected = RuntimeException.class)
    public void testReadResource_givenString_throwsRuntimeException() {
        CommonUtils.readResource("/foo.json");
    }

    @Test
    public void testExceptionInfo_givenException_returnsString() {
        String exceptionInfo = CommonUtils.exceptionInfo(new RuntimeException());
        Assert.assertNotNull(exceptionInfo);
    }

    @Test
    public void testGetZipEntryMap_givenZipInputStream_returnsMap() throws Exception {
        ZipInputStream zipInputStream = new ZipInputStream(
                Objects.requireNonNull(CommonUtils.class.getResourceAsStream("/data/hello.zip"))
        );
        Map<String, String> zipEntryMap = CommonUtils.getZipEntryMap(zipInputStream);
        Assert.assertNotNull(zipEntryMap);
    }

    @Test
    public void testGetHostname_given() {
        String hostname = CommonUtils.getHostname();
        Assert.assertNotNull(hostname);
    }
}