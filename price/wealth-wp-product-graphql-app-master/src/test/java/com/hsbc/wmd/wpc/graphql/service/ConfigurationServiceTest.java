package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {

    @InjectMocks
    ConfigurationService configurationService;
    @Mock
    private MongoDatabase mongodb;
    @Mock
    private MongoCollection<Document> colConfiguration;
    @Mock
    private FindIterable<Document> findIterable;
    private ArrayList<Document> cfList = new ArrayList<>();

    @Before
    public void setUp() {
        configurationService = new ConfigurationService(mongodb);
        ReflectionTestUtils.setField(configurationService, "colConfiguration", colConfiguration);
        Document doc = CommonUtils.readResourceAsDocument("/files/table-columns-mapping.json");
        cfList.add(doc);
        Mockito.when(colConfiguration.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.into(new ArrayList<>())).thenReturn(cfList);
    }

    @Test
    public void testGetConfigurationsByFilter_givenBsonFilter_returnsConifgList() {
        Bson filter = eq(com.dummy.wmd.wpc.graphql.constant.Field._id, "ALL/table-columns-mapping");
        List conifgList = configurationService.getConfigurationsByFilter(filter);
        assertEquals(cfList, conifgList);
    }

    @Test
    public void testGetInterestedGroups_givenStringField_returnsStringList() {
        String field = "asetAllocWghtPct";
        List<String> list = configurationService.getInterestedGroups(field);
        assertEquals("mdsProduct", list.get(0));
    }

    @Test
    public void testGetInterestedGroupFullMap_omitsArgs_returnsMap() {
        Map<String, List<String>> map = configurationService.getInterestedGroupFullMap();
        Assert.assertNotNull(map);
    }

    @Test
    public void testGetLegacyConfigByFilter() {
        // Configure the behavior of the mock objects
        when(colConfiguration.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(new Document());
        Map<String, Object> result = configurationService.getLegacyConfigByFilter();
        // Assert the expected result
        assertEquals(0, result.size());
    }
}
