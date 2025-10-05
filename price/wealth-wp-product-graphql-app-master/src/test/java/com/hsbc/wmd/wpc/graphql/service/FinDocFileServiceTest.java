package com.dummy.wmd.wpc.graphql.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;

@RunWith(MockitoJUnitRunner.class)
public class FinDocFileServiceTest {

    @InjectMocks
    private FinDocFileService finDocFileService;

    @Test
    public void testGetPath_givenFileName_returnPath() {
        Path path = finDocFileService.getPath("hello.pdf");
        Assert.assertNotNull(path);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPath_givenFileName_throwException() {
        finDocFileService.getPath("/hello.pdf");
    }
}
