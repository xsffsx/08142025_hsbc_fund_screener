package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.load.*;
import com.dummy.wpb.wpc.utils.service.LockService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(MockitoJUnitRunner.class)
public class CollectionSyncTaskTests {

    private CollectionSyncTask collectionSyncTask;
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mongodb;
    @Mock
    private SyncService syncService;
    @Mock
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Mock
    private LockService lockService;
    private MockedConstruction<ProductDataLoader> productDataLoaderMockedConstruction;
    private MockedConstruction<StaffLicenseCheckLoader> staffLicenseCheckLoaderMockedConstruction;
    private MockedConstruction<ProdTypeFinDocLoader> prodTypeFinDocLoaderMockedConstruction;
    private MockedConstruction<ReferenceDataLoader> referenceDataLoaderMockedConstruction;
    private MockedConstruction<AsetVoltlClassCharLoader> asetVoltlClassCharLoaderMockedConstruction;
    private MockedConstruction<PendAppoveTranLoader> pendAppoveTranLoaderMockedConstruction;
    private MockedConstruction<AsetVoltlClassCorlLoader> asetVoltlClassCorlLoaderMockedConstruction;
    private MockedConstruction<SingleTableLoader> singleTableLoaderMockedConstruction;
    private MockedConstruction<ProdPrcHistLoader> prodPrcHistLoaderMockedConstruction;

    @Mock
    CreateIndexesSyncTask createIndexesSyncTask;

    @Before
    public void setUp() {
        productDataLoaderMockedConstruction = Mockito.mockConstruction(ProductDataLoader.class);
        staffLicenseCheckLoaderMockedConstruction = Mockito.mockConstruction(StaffLicenseCheckLoader.class);
        prodTypeFinDocLoaderMockedConstruction = Mockito.mockConstruction(ProdTypeFinDocLoader.class);
        referenceDataLoaderMockedConstruction = Mockito.mockConstruction(ReferenceDataLoader.class);
        asetVoltlClassCharLoaderMockedConstruction = Mockito.mockConstruction(AsetVoltlClassCharLoader.class);
        pendAppoveTranLoaderMockedConstruction = Mockito.mockConstruction(PendAppoveTranLoader.class);
        asetVoltlClassCorlLoaderMockedConstruction = Mockito.mockConstruction(AsetVoltlClassCorlLoader.class);
        singleTableLoaderMockedConstruction = Mockito.mockConstruction(SingleTableLoader.class);
        prodPrcHistLoaderMockedConstruction = Mockito.mockConstruction(ProdPrcHistLoader.class);
        collectionSyncTask = new CollectionSyncTask(namedParameterJdbcTemplate, mongodb, syncService, threadPoolTaskExecutor, lockService);
        ReflectionTestUtils.setField(collectionSyncTask, "createIndexesSyncTask", createIndexesSyncTask);
    }

    @After
    public void tearDown() {
        productDataLoaderMockedConstruction.close();
        staffLicenseCheckLoaderMockedConstruction.close();
        prodTypeFinDocLoaderMockedConstruction.close();
        referenceDataLoaderMockedConstruction.close();
        asetVoltlClassCharLoaderMockedConstruction.close();
        pendAppoveTranLoaderMockedConstruction.close();
        asetVoltlClassCorlLoaderMockedConstruction.close();
        singleTableLoaderMockedConstruction.close();
        prodPrcHistLoaderMockedConstruction.close();
    }

    @Test
    public void testGetTaskName_noArgs_doesNotThrow() {
        String taskName = collectionSyncTask.getTaskName();
        Assert.assertEquals("CollectionSyncTask", taskName);
    }

    @Test
    public void testSetLockToken_noArgs_doesNotThrow() {
        try {
            collectionSyncTask.setLockToken("lockToken");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testSetCollectionName_noArgs_doesNotThrow() {
        try {
            collectionSyncTask.setCollectionName("collectionName");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testRun_noArgs_doesNotThrow1() {
        try {
            ReflectionTestUtils.setField(collectionSyncTask, "collectionName", CollectionName.prod_type_fin_doc);
            collectionSyncTask.run();
            ReflectionTestUtils.setField(collectionSyncTask, "collectionName", null);
            collectionSyncTask.run();
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetTableNames_noArgs_doesNotThrow() {
        try {
            Method getTableNames = collectionSyncTask.getClass().getDeclaredMethod("getTableNames", DataLoader.class);
            getTableNames.setAccessible(true);
            ProductDataLoader productDataLoader = Mockito.mock(ProductDataLoader.class);
            getTableNames.invoke(collectionSyncTask, productDataLoader);
            SingleTableLoader singleTableLoader = Mockito.mock(SingleTableLoader.class);
            getTableNames.invoke(collectionSyncTask, singleTableLoader);
            ProdTypeFinDocLoader prodTypeFinDocLoader = Mockito.mock(ProdTypeFinDocLoader.class);
            getTableNames.invoke(collectionSyncTask, prodTypeFinDocLoader);
            StaffLicenseCheckLoader staffLicenseCheckLoader = Mockito.mock(StaffLicenseCheckLoader.class);
            getTableNames.invoke(collectionSyncTask, staffLicenseCheckLoader);
            ReferenceDataLoader referenceDataLoader = Mockito.mock(ReferenceDataLoader.class);
            getTableNames.invoke(collectionSyncTask, referenceDataLoader);
            AsetVoltlClassCharLoader asetVoltlClassCharLoader = Mockito.mock(AsetVoltlClassCharLoader.class);
            getTableNames.invoke(collectionSyncTask, asetVoltlClassCharLoader);
            AsetVoltlClassCorlLoader asetVoltlClassCorlLoader = Mockito.mock(AsetVoltlClassCorlLoader.class);
            getTableNames.invoke(collectionSyncTask, asetVoltlClassCorlLoader);
            PendAppoveTranLoader pendAppoveTranLoader = Mockito.mock(PendAppoveTranLoader.class);
            getTableNames.invoke(collectionSyncTask, pendAppoveTranLoader);
            ProdPrcHistLoader prodPrcHistLoader = Mockito.mock(ProdPrcHistLoader.class);
            getTableNames.invoke(collectionSyncTask, prodPrcHistLoader);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void testGetTableNames_noArgs_throwException() throws Exception {
        Method getTableNames = collectionSyncTask.getClass().getDeclaredMethod("getTableNames", DataLoader.class);
        getTableNames.setAccessible(true);
        DataLoader dataLoader = Mockito.mock(DataLoader.class);
        getTableNames.invoke(collectionSyncTask, dataLoader);
    }
}
