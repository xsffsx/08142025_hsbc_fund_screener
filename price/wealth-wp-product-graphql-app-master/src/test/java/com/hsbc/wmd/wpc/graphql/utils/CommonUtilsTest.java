package com.dummy.wmd.wpc.graphql.utils;

import org.bson.Document;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CommonUtilsTest {
    @Test
    public void testNormalResource(){
        String text = CommonUtils.readResource("/files/hello.txt");
        assertEquals("hello world", text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotExistsResource(){
        CommonUtils.readResource("/files/hello1.txt");
    }

    @Test
    public void testToCanonicalPath_givenPath_returnsCanonicalPath() throws URISyntaxException {
        URL url = CommonUtils.class.getResource("/files/hello.txt");
        Path path =CommonUtils.toCanonicalPath(Paths.get(url.toURI()));
        assertNotNull(path);
    }
    @Test
    public void testCanonicalPath_givenStringPath_returnsCanonicalPathString() throws URISyntaxException {
        URL url = CommonUtils.class.getResource("/files/hello.txt");
        String path = url.toURI().getPath();
        String canonicalPath = CommonUtils.canonicalPath(path);
        assertNotNull(canonicalPath);
    }
    @Test
    public void testReadResourceAsDocument_givenClasspath_returnsDocument(){
        Document document = CommonUtils.readResourceAsDocument("/files/prod1.json");
        assertNotNull(document);
    }
}
