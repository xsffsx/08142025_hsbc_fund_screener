package com.dummy.wpb.wpc.utils.load;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductCompareLoaderTests {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> colProduct;
    @Mock
    private MongoCollection<Document> configColl;
    @Mock
    private MongoCollection<Document> metadataColl;
    @Mock
    private FindIterable findIterableCongfig;
    @Mock
    private FindIterable findIterableMetaData;
    @Mock
    private FindIterable findIterable;

    private ProductCompareLoader productCompareLoader;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.product)).thenReturn(colProduct);
        when(mockMongodb.getCollection(CollectionName.configuration)).thenReturn(configColl);
        when(mockMongodb.getCollection(CollectionName.metadata)).thenReturn(metadataColl);
        when(configColl.find(any(Bson.class))).thenReturn(findIterableCongfig);
        Document document = new Document("ext", Arrays.asList(new Document("fieldCde", "code")));
        Document config = new Document("config", document);
        config.put("name", "product-remodel");
        when(findIterableCongfig.first()).thenReturn(config);
        when(metadataColl.find(any(Bson.class))).thenReturn(findIterableMetaData);
        when(findIterableMetaData.into(new ArrayList<>())).thenReturn(new ArrayList<>());
        productCompareLoader = new ProductCompareLoader(mockNamedParameterJdbcTemplate, mockMongodb);
        ReflectionTestUtils.setField(productCompareLoader, "objectMapper", new ObjectMapper());
    }

    @Test
    public void testShowTableLoading() {
        assertThat(productCompareLoader.showTableLoading()).isFalse();
    }

    @Test
    public void testSaveProducts_givenMap_returnsNull() {
        try {
            productCompareLoader.saveProducts(new HashMap<>());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testRetrieveProdIds_givenIndex_returnsIdList() throws Exception {
        when(colProduct.find()).thenReturn(findIterable);
        when(findIterable.sort(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.skip(1)).thenReturn(findIterable);
        when(findIterable.limit(100)).thenReturn(findIterable);
        when(findIterable.projection(Projections.include(Field.prodId))).thenReturn(findIterable);
        List<Document> list = new LinkedList<>();
        list.add(new Document(Field.prodId, 0L));
        when(findIterable.into(new LinkedList<>())).thenReturn(list);
        List<Long> prodIds = productCompareLoader.retrieveProdIds(1);
        assertNotNull(prodIds);
    }

    @Test
    public void testGetProductDocument_givenId_returnsDocument() {
        try {
            productCompareLoader.getProductDocument(1L);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void testCompare_givenIdList_returnsMap() {
        try {
            productCompareLoader.compare(Arrays.asList(0L));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCompare_givenIds_returnsMap() {
        when(colProduct.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(new Document(Field._id, 1L));
        try {
            productCompareLoader.compare(0L, 1L);
        } catch (Exception e) {
            Assert.fail();
        }
    }

}