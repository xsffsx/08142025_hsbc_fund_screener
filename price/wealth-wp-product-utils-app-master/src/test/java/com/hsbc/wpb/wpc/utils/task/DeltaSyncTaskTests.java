package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.constant.Table;
import com.dummy.wpb.wpc.utils.load.ProductDataSynchronizer;
import com.dummy.wpb.wpc.utils.load.SingleTableSynchronizer;
import com.dummy.wpb.wpc.utils.model.DataChangeEvent;
import com.dummy.wpb.wpc.utils.model.ProductEventGroup;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.ProductService;
import com.dummy.wpb.wpc.utils.service.SyncService;
import com.mongodb.client.MongoDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeltaSyncTaskTests {
    @Mock
    private SyncService syncService;
    @Mock
    private LockService lockService;
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mongodb;
    @Mock
    private ProductService productService;
    @Mock
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private DeltaSyncTask task;
    private MockedConstruction<ProductDataSynchronizer> productDataSynchronizer;

    private MockedConstruction<SingleTableSynchronizer> singleTableSynchronizer;

    @Before
    public void setUp() throws Exception {
        task = new DeltaSyncTask();
        task.setLockToken("lockToken");
        ReflectionTestUtils.setField(task, "syncService", syncService);
        ReflectionTestUtils.setField(task, "lockService", lockService);
        ReflectionTestUtils.setField(task, "namedParameterJdbcTemplate", namedParameterJdbcTemplate);
        ReflectionTestUtils.setField(task, "mongodb", mongodb);
        ReflectionTestUtils.setField(task, "productService", productService);
        ReflectionTestUtils.setField(task, "threadPoolTaskExecutor", threadPoolTaskExecutor);
        //
        productDataSynchronizer = Mockito.mockConstruction(ProductDataSynchronizer.class);
        singleTableSynchronizer = Mockito.mockConstruction(SingleTableSynchronizer.class);
    }

    @After
    public void tearDown() {
        productDataSynchronizer.close();
        singleTableSynchronizer.close();
    }

    @Test
    public void testGetTaskName_noArgs_returnsString() {
        String taskName = task.getTaskName();
        assertNotNull(taskName);
    }

    @Test
    public void testRun_noArgs_doesNotThrow() {
        ProductEventGroup group = new ProductEventGroup();
        group.setEventCount(1);
        group.setProdIdList(Collections.singletonList(1L));
        group.setProcessStartTime(new Date());
        group.setEventStartTime(new Date());
        group.setEventEndTime(new Date());
        when(syncService.retrieveProductChangeEvents(any(Date.class))).thenReturn(group);
        DataChangeEvent event = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.TB_PROD,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event1 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.CDE_DESC_VALUE,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event2 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.ASET_VOLTL_CLASS_CHAR,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event3 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.ASET_VOLTL_CLASS_CORL,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event4 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.PROD_TYPE_FIN_DOC,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event5 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.PEND_APROVE_TRAN,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event6 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.PROD_ATRIB_MAP,
                "DELETE",
                "1",
                "1",
                null
        );
        DataChangeEvent event7 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.PROD_TYPE_STAF_LIC_CHECK,
                "ADD",
                "1",
                "1",
                null
        );
        DataChangeEvent event8 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.PROD_PRC_HIST,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event9 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.CHANL_COMN_CDE,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event10 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.FIN_DOC,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event11 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.FIN_DOC_HIST,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event12 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.PROD_TYPE_FIN_DOC_TYPE_REL,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event13 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.TB_PROD_TYPE_CHANL_ATTR,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event14 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.FIN_DOC_PARM,
                "UPDATE",
                "1",
                "1",
                null
        );
        DataChangeEvent event15 = new DataChangeEvent(
                1,
                Timestamp.valueOf("2023-09-26 14:50:00"),
                Table.PROD_SUBTP_STAF_LIC_CHECK,
                "UPDATE",
                "1",
                "1",
                null
        );

        List<DataChangeEvent> events = Arrays.asList(
                event,
                event1,
                event2,
                event3,
                event4,
                event5,
                event6,
                event7,
                event8,
                event9,
                event10,
                event11,
                event12,
                event13,
                event14,
                event15
        );
        when(syncService.retrieveNonProductChangeEvents(any(Date.class))).thenReturn(events);
        try {
            task.run();
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testRun_mockNull_doesNotThrow() {
        when(syncService.retrieveProductChangeEvents(any(Date.class))).thenReturn(null);
        when(syncService.retrieveNonProductChangeEvents(any(Date.class))).thenReturn(Collections.emptyList());
        try {
            task.run();
        } catch (Exception e) {
            Assert.fail();
        }
    }
}