package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.load.*;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.SyncService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
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

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FullSyncTaskTests {

    private FullSyncTask fullSyncTask;
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mongodb;
    @Mock
    private SyncService syncService;
    @Mock
    private LockService lockService;
    @Mock
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Mock
    CreateIndexesSyncTask createIndexesSyncTask;

    @Mock
    MongoCollection<Document> collection;

    @Before
    public void setUp() throws Exception {
        fullSyncTask = new FullSyncTask();
        ReflectionTestUtils.setField(fullSyncTask, "namedParameterJdbcTemplate", namedParameterJdbcTemplate);
        ReflectionTestUtils.setField(fullSyncTask, "mongodb", mongodb);
        ReflectionTestUtils.setField(fullSyncTask, "syncService", syncService);
        ReflectionTestUtils.setField(fullSyncTask, "lockService", lockService);
        ReflectionTestUtils.setField(fullSyncTask, "threadPoolTaskExecutor", threadPoolTaskExecutor);
        ReflectionTestUtils.setField(fullSyncTask, "createIndexesSyncTask", createIndexesSyncTask);
        Mockito.when(mongodb.getCollection((String) any())).thenReturn(collection);
    }

    @Test
    public void testGetTaskName_noArgs_returnsString() {
        String taskName = fullSyncTask.getTaskName();
        Assert.assertEquals("FullSyncTask", taskName);
    }

    @Test
    public void testSetLockToken_givenString_doesNotThrow() {
        try {
            fullSyncTask.setLockToken("lockToken");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testRun_noArgs_doesNotThrow() {
        MockedConstruction<ProdTypeFinDocLoader> prodTypeFinDocLoaderMockedConstruction = Mockito.mockConstruction(ProdTypeFinDocLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        MockedConstruction<StaffLicenseCheckLoader> staffLicenseCheckLoaderMockedConstruction = Mockito.mockConstruction(StaffLicenseCheckLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        MockedConstruction<ReferenceDataLoader> referenceDataLoaderMockedConstruction = Mockito.mockConstruction(ReferenceDataLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        MockedConstruction<AsetVoltlClassCharLoader> asetVoltlClassCharLoaderMockedConstruction = Mockito.mockConstruction(AsetVoltlClassCharLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        MockedConstruction<AsetVoltlClassCorlLoader> asetVoltlClassCorlLoaderMockedConstruction = Mockito.mockConstruction(AsetVoltlClassCorlLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        MockedConstruction<SysParamLoader> sysParamLoaderMockedConstruction = Mockito.mockConstruction(SysParamLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        MockedConstruction<SingleTableLoader> singleTableLoaderMockedConstruction = Mockito.mockConstruction(SingleTableLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        MockedConstruction<ProductDataLoader> productDataLoaderMockedConstruction = Mockito.mockConstruction(ProductDataLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        MockedConstruction<ProdPrcHistLoader> prodPrcHistLoaderMockedConstruction = Mockito.mockConstruction(ProdPrcHistLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        MockedConstruction<PendAppoveTranLoader> pendAppoveTranLoaderMockedConstruction = Mockito.mockConstruction(PendAppoveTranLoader.class,
                (mockClass, context) -> Mockito.doNothing().when(mockClass).load());
        try {
            fullSyncTask.run();
        } catch (Exception e) {
            Assert.fail();
        }finally {
            prodTypeFinDocLoaderMockedConstruction.close();
            staffLicenseCheckLoaderMockedConstruction.close();
            referenceDataLoaderMockedConstruction.close();
            asetVoltlClassCharLoaderMockedConstruction.close();
            asetVoltlClassCorlLoaderMockedConstruction.close();
            sysParamLoaderMockedConstruction.close();
            singleTableLoaderMockedConstruction.close();
            prodPrcHistLoaderMockedConstruction.close();
            pendAppoveTranLoaderMockedConstruction.close();
            productDataLoaderMockedConstruction.close();
        }
    }
}
