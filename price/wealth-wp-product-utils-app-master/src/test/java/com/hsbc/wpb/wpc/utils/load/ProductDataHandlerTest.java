package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.model.ProductTableInfo;
import com.dummy.wpb.wpc.utils.service.LockService;
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
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.ResultSet;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDataHandlerTest {

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
    private FindIterable<Document> findIterableConfig;
    @Mock
    private FindIterable<Document> findIterableMetaData;
    @Mock
    private ThreadPoolTaskExecutor mockThreadPoolTaskExecutor;
    @Mock
    private LockService lockService;
    private ProductDataHandler productDataHandler;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.product)).thenReturn(colProduct);
        when(mockMongodb.getCollection(CollectionName.configuration)).thenReturn(configColl);
        when(mockMongodb.getCollection(CollectionName.metadata)).thenReturn(metadataColl);
        when(configColl.find(any(Bson.class))).thenReturn(findIterableConfig);
        Document move = new Document("from", "tradeElig.ext");
        move.put("to", "ext.tradeElig");
        Document document = new Document("move", Collections.singletonList(move));
        Document config = new Document("config", document);
        config.put("name", "user-defined-field-mapping");
        when(findIterableConfig.first()).thenReturn(config);
        when(metadataColl.find(any(Bson.class))).thenReturn(findIterableMetaData);
        when(findIterableMetaData.into(new ArrayList<>())).thenReturn(new ArrayList<>());
        productDataHandler = new ProductDataHandler(mockNamedParameterJdbcTemplate, mockMongodb) {
            @Override
            protected boolean showTableLoading() {
                return false;
            }

            @Override
            protected void saveProducts(Map<Long, Map<String, Object>> prodMap) {
            }
        };
        ReflectionTestUtils.setField(productDataHandler, "threadPoolTaskExecutor", mockThreadPoolTaskExecutor);
        ReflectionTestUtils.setField(productDataHandler, "lockService", lockService);
    }

    @Test
    public void testProductDataHandler() {
        ProductDataHandler productDataHandler2 = new ProductDataHandler(mockNamedParameterJdbcTemplate, mockMongodb) {
            @Override
            protected boolean showTableLoading() {
                return false;
            }

            @Override
            protected void saveProducts(Map<Long, Map<String, Object>> prodMap) {
            }
        };
        assertNotNull(productDataHandler2);
    }

    @Test
    public void testGetSql_givenImportMethod_returnsString() {
        // Setup
        final ProductTableInfo info = new ProductTableInfo();
        info.setTable("table");
        info.setImportMethod("importMethod");
        info.setProdIdField("prodIdField");
        info.setToAttribute("toAttribute");
        info.setValueField("valueField");

        List<String> ls = Arrays.asList(
                "importMasterTable",
                "importPriceHistoryTable",
                "importAsObject",
                "importAsObjectList",
                "importExtensionFields",
                "importUserDefinedFields",
                "importAsStringList"
        );
        for (String name : ls) {
            productDataHandler.getSql(name, info);
        }
        info.setTable("TB_EQTY_LINK_INVST_UNDL_STOCK");
        productDataHandler.getSql("importAsObjectList", info);
        info.setTable("PROD_TRADE_CCY");
        String result = productDataHandler.getSql("importAsObjectList", info);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSql_givenWrongImportMethod_throwsException() {
        // Setup
        ProductTableInfo info = new ProductTableInfo();
        productDataHandler.getSql("name", info);
    }

    @Test
    public void testImportTables_givenList_doesNotThrow() {
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.doAnswer(invocation -> {
                    RowCallbackHandler callbackHandler = invocation.getArgument(2);
                    callbackHandler.processRow(resultSet);
                    return null;
                })
                .when(mockNamedParameterJdbcTemplate)
                .query(Mockito.anyString(), any(SqlParameterSource.class), any(RowCallbackHandler.class));
        // Run the test
        try (MockedStatic<DbUtils> dbUtils = Mockito.mockStatic(DbUtils.class)) {
            dbUtils.when(() -> DbUtils.getStringObjectMap(resultSet)).thenReturn(
                    new HashMap<String, Object>() {{
                        put("ctryRecCde", "HK");
                        put(Field.prodId, 1L);
                        put("grpMembrRecCde", "HASE");
                        put("ext", "to");
                        put("PROD_ID", 1L);
                        put("perfm", "perfm");
                    }}
            );
            productDataHandler.importTables(Collections.singletonList(0L));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testImportTables_givenProductRemodelConfigDoesNotExist_doesNotThrow() {
        when(findIterableConfig.first()).thenReturn(null);
        try {
            productDataHandler.importTables(Collections.singletonList(0L));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testImportTablesMultiThread_givenList_doesNotThrow() {
        doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(mockThreadPoolTaskExecutor).execute(any(Runnable.class));

        ResultSet resultSet = mock(ResultSet.class);

        doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(2);
            callbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate)
                .query(Mockito.anyString(), any(SqlParameterSource.class), any(RowCallbackHandler.class));

        MockedStatic<DbUtils> dbUtils = Mockito.mockStatic(DbUtils.class);
        dbUtils.when(() -> DbUtils.getStringObjectMap(resultSet)).thenReturn(
                new HashMap<String, Object>() {{
                    put("ctryRecCde", "HK");
                    put(Field.prodId, 1L);
                    put("grpMembrRecCde", "HASE");
                    put("ext.form", "to");
                    put("PROD_ID", 1L);
                }}
        );
        // Run the test
        try {
            productDataHandler.importTablesMultiThread(Collections.singletonList(Collections.singletonList(1L)));
        } catch (Exception e) {
            Assert.fail();
        } finally {
            dbUtils.close();
        }

    }

    @Test
    public void testImportTablesByBatch_givenList_doesNotThrow() {
        // Run the test
        productDataHandler.importTablesByBatch(Collections.singletonList(Collections.singletonList(0L)));
        try {
            productDataHandler.importTablesByBatch(Collections.singletonList(Collections.singletonList(0L)));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testBreakdownByBatch_givenList_returnsBatchList() {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 1001; i += 1) {
            list.add((long) i);
        }
        List<List<Long>> batchList = productDataHandler.breakdownByBatch(list);
        assertNotNull(batchList);
    }

    @Test
    public void testAttachPerfmField_givenMapAndList_doesNotThrow() {
        Map<String, Object> prod = new HashMap<>();
        Map<String, Object> perfmTypeCde = new HashMap<>();
        perfmTypeCde.put(Field.perfmTypeCde, "P");
        perfmTypeCde.put(Field.performance, "performance");
        Map<String, Object> benchmark = new HashMap<>();
        benchmark.put(Field.perfmTypeCde, "B");
        benchmark.put(Field.benchmark, "benchmark");
        List<Map<String, Object>> list = Arrays.asList(perfmTypeCde, benchmark);
        try {
            productDataHandler.attachPerfmField(prod, list);
        } catch (Exception e) {
            Assert.fail();
        }
    }

}