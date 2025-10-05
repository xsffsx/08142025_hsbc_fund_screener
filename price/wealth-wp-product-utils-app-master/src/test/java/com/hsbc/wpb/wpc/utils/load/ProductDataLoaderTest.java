package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDataLoaderTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private ThreadPoolTaskExecutor mockThreadPoolTaskExecutor;
    @Mock
    private LockService mockLockService;
    @Mock
    private MongoCollection<Document> mockColProduct;
    @Mock
    private MongoCollection<Document> mockConfigColl;
    @Mock
    private MongoCollection<Document> mockMetadataColl;
    @Mock
    private SequenceService mockSequenceService;
    @Mock
    private FindIterable configFindIterable;
    @Mock
    private AggregateIterable aggregateIterable;

    private ProductDataLoader productDataLoader;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.product)).thenReturn(mockColProduct);
        when(mockMongodb.getCollection(CollectionName.configuration)).thenReturn(mockConfigColl);
        when(mockMongodb.getCollection(CollectionName.metadata)).thenReturn(mockMetadataColl);
        when(mockConfigColl.find(any(Bson.class))).thenReturn(configFindIterable);
        when(configFindIterable.first()).thenReturn(new Document());
        productDataLoader = new ProductDataLoader(
                mockNamedParameterJdbcTemplate,
                mockMongodb,
                mockThreadPoolTaskExecutor,
                false,
                mockLockService
        );

        ReflectionTestUtils.setField(productDataLoader, "sequenceService", mockSequenceService);
    }

    @Test
    public void testShowTableLoading_givenNull_returnsFalse() {
        assertThat(productDataLoader.showTableLoading()).isFalse();
    }

    @Test
    public void testSaveProducts_givenMap_returnsNull() {
        Map<Long, Map<String, Object>> prodMap = new HashMap(){{put(1L,new HashMap(){{put("a","b");}});}};
        try{
            productDataLoader.saveProducts(prodMap);
        }catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testLoad_givenNull_returnsNull() {
        when(mockColProduct.countDocuments()).thenReturn(1L);
        when(mockColProduct.aggregate(any(List.class))).thenReturn(aggregateIterable);
        when(aggregateIterable.first()).thenReturn(new Document(Field.prodId,1L));
        try{
            productDataLoader.load();
        }catch (Exception e){
            Assert.fail();
        }


    }

}
