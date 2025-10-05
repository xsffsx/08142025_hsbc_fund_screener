package com.dummy.wpb.wpc.utils.load;

import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.SyncService;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDataSynchronizerTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private ThreadPoolTaskExecutor mockThreadPoolTaskExecutor;
    @Mock
    private LockService mockLockService;
    @Mock
    private SyncService mockSyncService;
    @Mock
    private MongoCollection mockColProduct;
    @Mock
    private MongoCollection<Document> mockConfigColl;
    @Mock
    private MongoCollection<Document> mockMetadataColl;
    @Mock
    private FindIterable findIterableCongfig;
    @Mock
    private FindIterable findIterable;
    @Mock
    private MongoCursor mongoCursor;
    @Mock
    private DeleteResult result;
    private ProductDataSynchronizer productDataSynchronizer;


    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.product)).thenReturn(mockColProduct);
        when(mockMongodb.getCollection(CollectionName.configuration)).thenReturn(mockConfigColl);
        when(mockMongodb.getCollection(CollectionName.metadata)).thenReturn(mockMetadataColl);
        when(mockConfigColl.find(any(Bson.class))).thenReturn(findIterableCongfig);
        when(findIterableCongfig.first()).thenReturn(null);
        productDataSynchronizer = new ProductDataSynchronizer(mockNamedParameterJdbcTemplate, mockMongodb,
                mockThreadPoolTaskExecutor, false, mockLockService);

    }

    @Test
    public void testShowTableLoading() {
        assertThat(productDataSynchronizer.showTableLoading()).isFalse();
    }

    @Test
    public void testSaveProducts_givenMap_returnsNull() {
        // Setup
        Map<Long, Map<String, Object>> prodMap = ImmutableMap.of(999L,ImmutableMap.of("a","b"));
        when(mockColProduct.find(any(Bson.class))).thenReturn(findIterable);
        // Run the test
        try{
            productDataSynchronizer.saveProducts(prodMap);
        }catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testSync_givenKeySet_returnsNull() {
        // Setup
        Map<String, Object> prod = new HashMap<>();
        prod.put(Field.PROD_ID,0L);
        Set<Map<String, Object>> keySet = new HashSet<>(Arrays.asList(prod));
        when(mockConfigColl.find(any(Bson.class))).thenReturn(findIterable);
        Document document = new Document("ext",Arrays.asList(new Document("fieldCde","code")));
        Document config = new Document("config",document);
        config.put("name", "user-defined-field-mapping");
        when(findIterable.first()).thenReturn(config);
        when(mockColProduct.find(any(Bson.class))).thenReturn(findIterable);
        // Run the test
        try {
            productDataSynchronizer.sync(keySet);
        }catch (Exception e){
            Assert.fail();
        }


    }



    @Test
    public void testDelete() {
        // Run the test
        when(mockColProduct.deleteMany(any(Bson.class))).thenReturn(result);
        productDataSynchronizer.delete(new HashSet<>(Arrays.asList("value")));
        // Verify the results
        Assert.assertNotNull(mockColProduct);
    }
}