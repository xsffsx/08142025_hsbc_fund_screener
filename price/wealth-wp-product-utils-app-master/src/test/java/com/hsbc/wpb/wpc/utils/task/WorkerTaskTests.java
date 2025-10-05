package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.service.LockService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class WorkerTaskTests {

    @InjectMocks
    private WorkerTask workerTask;
    @Mock
    private BlockingQueue<String> blockingQueue;
    @Mock
    private DeltaSyncTask deltaSyncTask;
    @Mock
    private FullSyncTask fullSyncTask;
    @Mock
    private CollectionSyncTask collectionSyncTask;
    @Mock
    private LockService lockService;

    @Mock
    CreateIndexesSyncTask createIndexesSyncTask;

    @Test
    public void testInit_noArgs_doesNotThrow() {
        try {
            Mockito.when(deltaSyncTask.getTaskName()).thenReturn("deltaSyncTask");
            Mockito.when(fullSyncTask.getTaskName()).thenReturn("fullSyncTask");
            Mockito.when(collectionSyncTask.getTaskName()).thenReturn("collectionSyncTask");
            WorkerTask workerTask = new WorkerTask();
            ReflectionTestUtils.setField(workerTask, "deltaSyncTask", deltaSyncTask);
            ReflectionTestUtils.setField(workerTask, "fullSyncTask", fullSyncTask);
            ReflectionTestUtils.setField(workerTask, "collectionSyncTask", collectionSyncTask);
            ReflectionTestUtils.setField(workerTask, "createIndexesSyncTask", createIndexesSyncTask);
            Method init = workerTask.getClass().getDeclaredMethod("init");
            init.setAccessible(true);
            init.invoke(workerTask);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testRun_noArgs_doesNotThrow() throws InterruptedException {
        Mockito.when(blockingQueue.take()).thenReturn("fullSyncTask").thenReturn("deltaSyncTask").thenReturn("collectionSyncTask").thenReturn("task");
        Mockito.when(fullSyncTask.getTaskName()).thenReturn("fullSyncTask");
        Mockito.when(deltaSyncTask.getTaskName()).thenReturn("deltaSyncTask");
        Mockito.when(collectionSyncTask.getTaskName()).thenThrow(IllegalStateException.class);
        Mockito.when(blockingQueue.size()).thenReturn(1);
        Mockito.when(lockService.retrieveLock(anyString())).thenReturn("token");
        Mockito.doNothing().when(deltaSyncTask).setLockToken(anyString());
        Mockito.doNothing().when(deltaSyncTask).run();
        Map<String, SyncTask> taskMap = (Map<String, SyncTask>) ReflectionTestUtils.getField(WorkerTask.class, "taskMap");
        taskMap.put("fullSyncTask", fullSyncTask);
        taskMap.put("deltaSyncTask", deltaSyncTask);
        taskMap.put("collectionSyncTask", collectionSyncTask);
        ReflectionTestUtils.setField(workerTask, "disableDataLoad", true);
        workerTask.run();
        Mockito.verify(deltaSyncTask, Mockito.times(1)).run();
    }
}
