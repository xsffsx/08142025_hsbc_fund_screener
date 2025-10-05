package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.model.RowSnapshot;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExtFieldServiceTests {
    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoCollection<Document> configuration;
    @Mock
    private FindIterable findIterable;

    private ExtFieldService extFieldService;

    @Before
    public void setUp() throws Exception {
        Document document = Document.parse(CommonUtils.readResource("/data/configuration.json"));
        when(mockMongoDatabase.getCollection(CollectionName.configuration)).thenReturn(configuration);
        when(configuration.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(document);
        extFieldService = new ExtFieldService(mockMongoDatabase, mockNamedParameterJdbcTemplate);

    }

    @Test
    public void testRemodel_givenString_returnsString() {
        // Run the test
        String result = extFieldService.remodel("ext.beforeRemodel");
        // Verify the results
        assertThat(result).isEqualTo("afterRemodel");
    }

    @Test
    public void testReadValue_givenMaps_returnsValue() {
        // Setup
        //Document rowData = Document.parse(CommonUtils.readResource("/data/product-data.json"));
        Document rowData = new Document("prodId", "prodId");
        rowData.put("fieldCde", "fieldCde");
        rowData.put(Field.fieldDataTypeText, "String");
        rowData.put("fieldStrngValueText", "test");
        Map<String, List<Object>> listValueMap = new HashMap<>();
        listValueMap.put("key", new ArrayList<>());
        // Run the test
        Object result = extFieldService.readValue(rowData, listValueMap);
        // Verify the results
        assertThat(result).isEqualTo("test");
        listValueMap.put("fieldCde@prodId", Arrays.asList("value"));
        Object result2 = extFieldService.readValue(rowData, listValueMap);
        assertThat(result2).isEqualTo(Arrays.asList("value"));
    }

    @Test
    public void testGetExtListFieldProds_givenMaps_returnsSetLong() {
        // Setup
        Map<String, Map<String, Object>> rowDataMap = new HashMap<>();
        Document rowData = new Document("prodId", 0L);
        rowData.put("fieldCde", "jurisCde");
        rowDataMap.put("key", rowData);

        Map<String, RowSnapshot> snapshotMap = new HashMap<>();
        RowSnapshot snapshot = new RowSnapshot();
        snapshot.setData(rowData);
        snapshotMap.put("key", snapshot);
        // Run the test
        Set<Long> result = extFieldService.getExtListFieldProds(rowDataMap, snapshotMap);
        // Verify the results
        assertNotNull(result);
    }


    @Test
    public void testReadExtListValueMap_givenEmptySet_returnsRowMap() {
        Map<String, List<Object>> result = extFieldService.readExtListValueMap("tableName",
                new HashSet<>());
        assertThat(result).isEqualTo(new LinkedHashMap<>());
    }

    @Test
    public void testReadExtListValueMap_givenSet_returnsRowMap() {
        Map<String, List<Object>> result = extFieldService.readExtListValueMap("tableName",
                new HashSet<>(Arrays.asList(0L, 1L, 2L)));
        assertNotNull(result);

    }

}
