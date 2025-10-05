package com.dummy.wpb.wpc.utils.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.wpc.utils.MockUtils;
import com.dummy.wpb.wpc.utils.load.ProductCompareLoader;
import com.dummy.wpb.wpc.utils.load.ProductDataReLoader;
import com.dummy.wpb.wpc.utils.service.AdminLogService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDataControllerTest {

    @MockBean
    private ObjectMapper mockObjectMapper;
    @MockBean
    private ProductCompareLoader mockProductCompareLoader;

    @InjectMocks
    private ProductDataController  productDataController;

    HttpServletRequest request;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        mockProductCompareLoader = mock(ProductCompareLoader.class);
        request = mock(HttpServletRequest.class);
        MockUtils.mockPrivate(productDataController, "adminLogService", mock(AdminLogService.class));
        MockitoAnnotations.initMocks(this);
        Assert.assertNotNull(request);
    }

    @Test
    public void testProductReload_givenString_returnsResponseEntity1() {

        try (MockedConstruction<ProductDataReLoader> productDataReLoaderMockedConstruction = Mockito.mockConstruction(ProductDataReLoader.class, (mock, context) -> {
            ProductDataReLoader spyProductDataReLoader = spy(mock);
        })) {
            ResponseEntity<Object> objectResponseEntity = productDataController.productReload("1,2,3");
            Assert.assertNotNull(objectResponseEntity);
        }

    }

    @Test
    public void testProductReload_givenString_returnsResponseEntity2() {
        ResponseEntity<Object> objectResponseEntity = productDataController.productReload("L,2,3");
        Assert.assertNotNull(objectResponseEntity);
    }

    @Test
    public void testProductCompare_givenString_returnsResponseEntity1() {
        when(mockProductCompareLoader.compare(any())).thenReturn(new HashMap<Long, List<Map<String, Object>>>(){{
            put(1L,new ArrayList(Arrays.asList("a","b","c")));
        }});
        ResponseEntity<Object> resp = productDataController.productCompare("1,2,3");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testProductCompare_givenString_returnsResponseEntity2() {
        ResponseEntity<Object> resp = productDataController.productCompare("l,2,3");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testProductCompare_givenLongAndLong_returnsResponseEntity1() {
        ResponseEntity<Object> objectResponseEntity = productDataController.productCompare(1L, 1L);
        Assert.assertNotNull(objectResponseEntity);
    }

    @Test
    public void testProductCompare_givenLongAndLong_returnsResponseEntity2() {
        Mockito.when(mockProductCompareLoader.compare(any(), any())).thenThrow(RuntimeException.class);
        ResponseEntity<Object> objectResponseEntity = productDataController.productCompare(1L, 1L);
        Assert.assertNotNull(objectResponseEntity);
    }


    @Test
    public void testAutoCompare_givenHttpServletRequestAndInteger_returnsResponseEntity1() {
        ResponseEntity<Object> objectResponseEntity = productDataController.autoCompare(request, 1);
        Assert.assertNotNull(objectResponseEntity);
    }

    @Test
    public void testAutoCompare_givenHttpServletRequestAndInteger_returnsResponseEntity2() throws Exception {
        Mockito.when(mockProductCompareLoader.retrieveProdIds(any())).thenThrow(RuntimeException.class);
        ResponseEntity<Object> objectResponseEntity = productDataController.autoCompare(request, 1);
        Assert.assertNotNull(objectResponseEntity);
    }

    @Test
    public void testProductCompareJson_givenMap_returnsResponseEntity() throws Exception {
        mockObjectMapper  = mock(ObjectMapper.class);
        MockUtils.mockPrivate(productDataController,"objectMapper",mockObjectMapper);

        when(mockObjectMapper.readValue(anyString(),eq(Map.class))).thenReturn(new HashMap(){{put("a","a");}})
                .thenReturn(new HashMap(){{put("a","b");}});

        ResponseEntity<Object> objectResponseEntity = productDataController.productCompareJson(new HashMap<String, String>() {{
            put("jsonStr1", "a");
            put("jsonStr2", "b");
        }});
        Assert.assertNotNull(objectResponseEntity);
    }

    @Test
    public void testProductCompareJson_givenMap_returnsResponseEntity2() throws Exception {
        mockObjectMapper  = mock(ObjectMapper.class);
        ResponseEntity<Object> objectResponseEntity = productDataController.productCompareJson(new HashMap<>());
        Assert.assertNotNull(objectResponseEntity);
    }

}