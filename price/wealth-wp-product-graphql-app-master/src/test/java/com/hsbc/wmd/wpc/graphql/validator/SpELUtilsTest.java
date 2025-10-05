package com.dummy.wmd.wpc.graphql.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.MongoDBService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static graphql.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("java:S5845")
@RunWith(MockitoJUnitRunner.class)
public class SpELUtilsTest {
    @Test
    public void testAddDouble_givenMapListAndString_returnsDouble() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URL url = SpELUtilsTest.class.getResource("/files/test-sum-data.json");
        List data = mapper.readValue(url, List.class);
        double result = SpELUtils.sum(data, "asetAllocWghtPct");
        assertNotNull(result);
    }
    @Test
    public void testProductById_givenId_returnsEmptyMap(){
        Long prodId = 1L;
        MockedStatic<MongoDBService> mongoDBService = Mockito.mockStatic(MongoDBService.class);
        mongoDBService.when(() -> MongoDBService.queryForMapList(CollectionName.product, eq(Field.prodId, prodId))).thenReturn(new ArrayList<>());
        Map<String, Object> map = SpELUtils.productById(prodId);
        mongoDBService.close();
        assertNull(map);
    }
    @Test
    public void testProductById_givenId_returnsMap(){
        Long prodId = 1L;
        MockedStatic<MongoDBService> mongoDBService = Mockito.mockStatic(MongoDBService.class);
        Map<String, Object> prod = new HashMap<>();
        prod.put("key","value");
        mongoDBService.when(() -> MongoDBService.queryForMapList(CollectionName.product, eq(Field.prodId, prodId))).thenReturn(Arrays.asList(prod));
        Map<String, Object> map = SpELUtils.productById(prodId);
        mongoDBService.close();
        assertNotNull(map);
    }
    @Test
    public void testLocalDate_givenLocalDate_returnsLocalDate(){
        LocalDate localDate =   SpELUtils.localDate(LocalDate.now());
        assertNotNull(localDate);
    }
    @Test
    public void testLocalDate_givenDate_returnsLocalDate(){
        LocalDate localDate =   SpELUtils.localDate(new Date());
        assertNotNull(localDate);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testLocalDate_givenString_throwsException(){
        SpELUtils.localDate("value");
    }
}
