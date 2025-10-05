package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.exception.productBatchException;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CommonUtilsTest {
    
    @Test
    @SneakyThrows
    public void readResourceFileExists() {
        String result = CommonUtils.readResource("reference_data.yml");
        assertEquals("ctryRecCde: HK", result);
    }

    @Test
    @SneakyThrows
    public void readResourceFileNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> CommonUtils.readResource("non-existing-file.txt"));
        assertEquals("Resource not found: non-existing-file.txt", exception.getMessage());
    }

    @Test
    @SneakyThrows
    public void readResourceIOException() {
        ClassPathResource resource = Mockito.mock(ClassPathResource.class);
        Mockito.when(resource.getInputStream()).thenThrow(new IOException("IO error"));

        try (MockedConstruction<ClassPathResource> mockedConstruction = Mockito.mockConstruction(ClassPathResource.class,
            (mock, context) -> Mockito.when(mock.getInputStream()).thenThrow(new IOException("IO error")))) {
            productBatchException exception = assertThrows(productBatchException.class, () -> CommonUtils.readResource("file-with-io-error.txt"));
            assertEquals("Error reading resource: file-with-io-error.txt", exception.getMessage());
        }
    }

    @Test
    public void test_isEmptyList() {
        Assert.assertTrue(CommonUtils.isEmptyList(null));
        Assert.assertTrue(CommonUtils.isEmptyList(Collections.emptyList()));
        Assert.assertTrue(CommonUtils.isEmptyList(Collections.singletonList(null)));
        Assert.assertFalse(CommonUtils.isEmptyList(Collections.singletonList("123")));
    }
}