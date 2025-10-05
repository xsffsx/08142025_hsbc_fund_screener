package com.dummy.wpb.product.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YamlUtilsTest {
    @Test
    void testReadResource_ReturnsObject_WhenValidResource() {
        String classpath = "/reference_data.yml";
        ReferenceData expectedObject = new ReferenceData();
        expectedObject.setCtryRecCde("HK");
        ReferenceData result = YamlUtils.readResource(classpath, ReferenceData.class);
        assertEquals(expectedObject, result);
    }

    @Test
    void testReadResource_ThrowsIllegalArgumentException_WhenResourceNotFound() {
        String classpath = "/reference_data1.yml";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            YamlUtils.readResource(classpath, ReferenceData.class);
        });
        assertEquals("Resource not found: " + classpath, exception.getMessage());
    }

    @Test
    void testReadResource_ThrowsproductBatchException_WhenIOExceptionOccurs() {
        String classpath = "/reference_data.json";
        productBatchException exception = assertThrows(productBatchException.class, () -> {
            YamlUtils.readResource(classpath, ReferenceData.class);
        });
        assertTrue(exception.getMessage().contains("Error reading yaml resource: " + classpath));
    }
    
    @Test
    void testReadResource_ReturnsObject_WhenValidResourceWithTypeReference() {
        String classpath = "/reference_data.yml";
        TypeReference<ReferenceData> typeReference = new TypeReference<ReferenceData>() {};
        ReferenceData expectedObject = new ReferenceData();
        expectedObject.setCtryRecCde("HK");
        ReferenceData result = YamlUtils.readResource(classpath, typeReference);
        assertEquals(expectedObject, result);
    }
    
    @Test
    void testReadResource_ThrowsIllegalArgumentException_WhenResourceNotFoundWithTypeReference() {
        String classpath = "/nonExistentResource.yaml";
        TypeReference<ReferenceData> typeReference = new TypeReference<ReferenceData>() {};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            YamlUtils.readResource(classpath, typeReference);
        });
        assertEquals("Resource not found: " + classpath, exception.getMessage());
    }
    
    @Test
    void testReadResource_ThrowsproductBatchException_WhenIOExceptionOccursWithTypeReference() {
        String classpath = "/reference_data.json";
        TypeReference<ReferenceData> typeReference = new TypeReference<ReferenceData>() {};
        productBatchException exception = assertThrows(productBatchException.class, () -> {
            YamlUtils.readResource(classpath, typeReference);
        });
        assertTrue(exception.getMessage().contains("Error reading yaml resource: " + classpath));
    }
}