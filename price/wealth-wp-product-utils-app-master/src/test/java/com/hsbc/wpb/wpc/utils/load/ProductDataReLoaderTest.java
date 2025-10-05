package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class ProductDataReLoaderTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> mockColProduct;
    @Mock
    private MongoCollection<Document> mockConfigColl;
    @Mock
    private MongoCollection<Document> mockMetadataColl;
    @Mock
    private FindIterable configFindIterable;
    @Mock
    private AggregateIterable aggregateIterable;
    @Mock
    private SequenceService mockSequenceService;
    private ProductDataReLoader productDataReLoader;


    @Before
    public void setUp() throws Exception {
        List<Long> prodIdList = Arrays.asList(0L,1L);
        when(mockMongodb.getCollection(CollectionName.product)).thenReturn(mockColProduct);
        when(mockMongodb.getCollection(CollectionName.configuration)).thenReturn(mockConfigColl);
        when(mockMongodb.getCollection(CollectionName.metadata)).thenReturn(mockMetadataColl);
        when(mockConfigColl.find(any(Bson.class))).thenReturn(configFindIterable);
        when(configFindIterable.first()).thenReturn(new Document());
        productDataReLoader = new ProductDataReLoader(prodIdList,mockNamedParameterJdbcTemplate,mockMongodb);
        ReflectionTestUtils.setField(productDataReLoader, "prodSequenceService", mockSequenceService);
    }

    @Test
    public void testShowTableLoading() {
        assertThat(productDataReLoader.showTableLoading()).isFalse();
    }

    @Test
    public void testSaveProducts_givenDocument_returnsNull() {
        Map<Long, Map<String, Object>> prodMap = new HashMap<>();
        prodMap.put(0L,new Document());
        prodMap.put(1L,new Document());
        try{
            productDataReLoader.saveProducts(prodMap);
        }catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testLoad_givenNull_returnsNull(){
        when(mockConfigColl.find(any(Bson.class))).thenReturn(configFindIterable);
        Document document = new Document("ext",Arrays.asList(new Document("fieldCde","code")));
        Document config = new Document("config",document);
        config.put("name","user-defined-field-mapping");
        when(configFindIterable.first()).thenReturn(config);
        when(mockColProduct.aggregate(any(List.class))).thenReturn(aggregateIterable);
        when(aggregateIterable.first()).thenReturn(new Document(Field.prodId,1L));
        try{
            productDataReLoader.load();
        }catch (Exception e){
            Assert.fail();
        }


    }

}
