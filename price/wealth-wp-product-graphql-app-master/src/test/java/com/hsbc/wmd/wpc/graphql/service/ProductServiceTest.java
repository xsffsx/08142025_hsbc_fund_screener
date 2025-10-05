package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.GraphQLProvider;
import com.dummy.wmd.wpc.graphql.calc.CalculationManager;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.fetcher.amendment.AmendmentUtils;
import com.dummy.wmd.wpc.graphql.listener.NotificationManager;
import com.dummy.wmd.wpc.graphql.model.GroupItem;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.model.ProductBatchCreateResult;
import com.dummy.wmd.wpc.graphql.model.ProductBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.utils.ProductInputUtils;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ProductValidator;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
    @Mock
    private MongoCollection<Document> colProduct;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private DocumentRevisionService documentRevisionService;
    @Mock
    private CalculationManager calculationManager;
    @Mock
    private NotificationManager notificationManager;
    @Mock
    private ProductValidator productValidator;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    MongoDatabase mongodb;
    @Mock
    private FindIterable findIterable;
    @Mock
    AggregateIterable aggregateIterable;

    @Mock
    LockService lockService;

    @InjectMocks
    private ProductService productService;
    private String countryCode = "HK";
    private String groupMember = "HASE";
    private String reportCode = "WRTS";
    private String reportDate = "2023-08-22";

    @Before
    public void setUp() throws Exception {
        productService = new ProductService();
        ReflectionTestUtils.setField(productService, "mongoTemplate", mongoTemplate);
        Mockito.when(mongoTemplate.getCollection(CollectionName.product.name())).thenReturn(colProduct);
        ReflectionTestUtils.setField(productService, "sequenceService", sequenceService);
        ReflectionTestUtils.setField(productService, "documentRevisionService", documentRevisionService);
        ReflectionTestUtils.setField(productService, "calculationManager", calculationManager);
        ReflectionTestUtils.setField(productService, "notificationManager", notificationManager);
        ReflectionTestUtils.setField(productService, "productValidator", productValidator);
        ReflectionTestUtils.setField(productService, "lockService", lockService);

    }

    @Test
    public void testCreateProduct() {

        Mockito.when(sequenceService.nextId(anyString())).thenReturn(1L);

        MockedStatic<ProductInputUtils> productInputUtils = Mockito.mockStatic(ProductInputUtils.class);
        try {
            productInputUtils.when(() -> ProductInputUtils.supplementMissingFields(anyMap())).thenReturn(new HashMap<>());
            Map<String, Object> prod = new HashMap<>();
            prod.put(Field._id, 1L);
            prod.put(Field.prodId, 1L);
            prod.put(Field.prodCde, "1");
            prod.put(Field.revision, 1L);
            Document product = productService.createProduct(prod);
            Assertions.assertNotNull(product);
        } finally {
            productInputUtils.close();
        }
    }

    @Test
    public void testBatchCreate() {
        MockedStatic<ProductInputUtils> productInputUtils = Mockito.mockStatic(ProductInputUtils.class);
        try {
            productInputUtils.when(() -> ProductInputUtils.supplementMissingFields(anyMap())).thenReturn(new HashMap<>());
            List<Map<String, Object>> prodList = new ArrayList<>();
            Map<String, Object> prod = new HashMap<>();
            prod.put(Field._id, 1L);
            prod.put(Field.prodId, 1L);
            prod.put(Field.prodCde, "1");
            prod.put(Field.revision, 1L);
            prodList.add(prod);

            boolean allowPartial = true;
            ProductBatchCreateResult productBatchCreateResult = productService.batchCreate(prodList, allowPartial);
            Assertions.assertNotNull(productBatchCreateResult);
        } finally {
            productInputUtils.close();
        }
    }


    @Test
    public void testBatchCreate_giveNotAllowPartial_thenRollback() {
        MockedStatic<ProductInputUtils> productInputUtils = Mockito.mockStatic(ProductInputUtils.class);
        MockedStatic<TransactionAspectSupport> transactionAspectSupport = Mockito.mockStatic(TransactionAspectSupport.class);
        try {
            productInputUtils.when(() -> ProductInputUtils.supplementMissingFields(anyMap())).thenReturn(new HashMap<>());
            List<Map<String, Object>> prodList = new ArrayList<>();
            Map<String, Object> prod = new HashMap<>();
            prod.put(Field._id, 1L);
            prod.put(Field.prodId, 1L);
            prod.put(Field.prodCde, "1");
            prod.put(Field.revision, 1L);
            prodList.add(prod);
            prodList.add(new Document(prod));

            TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
            Mockito.when(TransactionAspectSupport.currentTransactionStatus()).thenReturn(transactionStatus);
            boolean allowPartial = false;

            Mockito.when(productValidator.validate(any()))
                    .thenAnswer((invocation) -> Collections.emptyList())
                    .thenAnswer((invocation) -> Collections.singletonList(new Error("$","prodAltPrimNum@duplication","Active product with the same primary code exists")));
            ProductBatchCreateResult productBatchCreateResult = productService.batchCreate(prodList, allowPartial);
            Assertions.assertNotNull(productBatchCreateResult);
            Mockito.verify(transactionStatus,times(1)).setRollbackOnly();
        } finally {
            productInputUtils.close();
            transactionAspectSupport.close();
        }
    }


    @Test
    public void testUpdateProduct() {
        MockedStatic<ProductInputUtils> productInputUtils = Mockito.mockStatic(ProductInputUtils.class);
        try {
            Document doc = new Document();
            doc.put(Field._id, 1L);
            Mockito.when(findIterable.first()).thenReturn(new Document());
            Mockito.when(colProduct.find(any(Bson.class))).thenReturn(findIterable);
            productInputUtils.when(() -> ProductInputUtils.supplementMissingFields(anyMap())).thenReturn(new HashMap<>());
            List<OperationInput> list = new ArrayList<>();
            Document product = productService.updateProduct(doc, list);
            Assertions.assertNotNull(product);
        } finally {
            productInputUtils.close();
        }
    }

    @Test
    public void testBatchUpdate() {
        MockedStatic<ProductInputUtils> productInputUtils = Mockito.mockStatic(ProductInputUtils.class);
        MockedStatic<AmendmentUtils> amendmentUtils = Mockito.mockStatic(AmendmentUtils.class);
        MockedStatic<GraphQLProvider> graphQLProvider = Mockito.mockStatic(GraphQLProvider.class);
        MockedStatic<AopContext> aopContext = Mockito.mockStatic(AopContext.class);
        try {
            List<Document> list = new ArrayList<>();
            Document map = new Document();
            map.put(Field.prodId, 1L);
            map.put(Field._id, 1L);
            list.add(map);
            Mockito.when(findIterable.into(any(List.class))).thenReturn(list);
            Mockito.when(colProduct.find(any(Bson.class))).thenReturn(findIterable);
            Mockito.when(findIterable.first()).thenReturn(new Document());
            aopContext.when(() -> AopContext.currentProxy()).thenReturn(productService);

            BsonDocument filter = new BsonDocument();
            List<OperationInput> operations = new ArrayList<>();
            boolean allowPartial = true;
            ProductBatchUpdateResult productBatchUpdateResult = productService.batchUpdate(filter, operations, allowPartial);
            Assertions.assertNotNull(productBatchUpdateResult);
        } finally {
            graphQLProvider.close();
            amendmentUtils.close();
            productInputUtils.close();
            aopContext.close();
        }
    }


    @Test
    public void testBatchUpdateById() {
        MockedStatic<AmendmentUtils> amendmentUtils = Mockito.mockStatic(AmendmentUtils.class);
        MockedStatic<RevisionUtils> revisionUtils = Mockito.mockStatic(RevisionUtils.class);
        MockedStatic<ProductInputUtils> productInputUtils = Mockito.mockStatic(ProductInputUtils.class);
        MockedStatic<AopContext> aopContext = Mockito.mockStatic(AopContext.class);
        try {
            boolean allowPartial = true;
            Map<Long, List<OperationInput>> prodIdOperationMap = new HashMap<>();
            prodIdOperationMap.put(1L, new ArrayList<>());

            aopContext.when(() -> AopContext.currentProxy()).thenReturn(productService);
            List<Document> list = new ArrayList<>();
            Document map = new Document();
            map.put(Field.prodId, 1L);
            map.put(Field._id, 1L);
            list.add(map);
            Mockito.when(findIterable.into(any(List.class))).thenReturn(list);

            Mockito.when(colProduct.find(any(Bson.class))).thenReturn(findIterable);
            ProductBatchUpdateResult result = productService.batchUpdateById(prodIdOperationMap, allowPartial);
            Assertions.assertNotNull(result);
        } finally {
            amendmentUtils.close();
            revisionUtils.close();
            productInputUtils.close();
            aopContext.close();
        }
    }

    @Test
    public void testGroupByProductType() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put(Field._id, 1L);

        List<Map<String, Object>> list = new ArrayList<>();
        Document doc = new Document();
        doc.put(Field.prodId, "1");
        doc.put(Field._id, "1");
        doc.put("count", 1);
        list.add(doc);
        Mockito.when(aggregateIterable.into(any(List.class))).thenReturn(list);
        Mockito.when(colProduct.aggregate(any(List.class))).thenReturn(aggregateIterable);
        List<GroupItem> groupItems = productService.groupByProductType(filterMap);
        Assertions.assertEquals(1, groupItems.size());
    }

    @Test
    public void testGroupByProductStatus() {
        String prodTypeCode = "BOND";


        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put(Field._id, 1L);

        List<Map<String, Object>> list = new ArrayList<>();
        Document doc = new Document();
        doc.put(Field.prodId, "1");
        doc.put(Field._id, "1");
        doc.put("count", 1);
        list.add(doc);
        Mockito.when(aggregateIterable.into(any(List.class))).thenReturn(list);
        Mockito.when(colProduct.aggregate(any(List.class))).thenReturn(aggregateIterable);
        List<GroupItem> groupItems = productService.groupByProductStatus(prodTypeCode, filterMap);
        Assertions.assertEquals(1, groupItems.size());


    }
}
