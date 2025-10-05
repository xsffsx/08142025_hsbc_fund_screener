package com.dummy.wpb.wpc.utils.task;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.BlockingQueue;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class WatchTaskTests {

    @InjectMocks
    private WatchTask watchTask;
    @Mock
    private BlockingQueue<String> blockingQueue;
    @Mock
    private SyncTask deltaSyncTask;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testSetStopWatch_noArgs_doesNotThrow() {
        try {
            watchTask.setStopWatch(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetStopWatch_noArgs_returnsObject() {
        Object stopWatch = watchTask.getStopWatch();
        Assert.assertNotNull(stopWatch);
    }

    @Test
    public void testRun_noArgs_doesNotThrow() {
        try {
            Mockito.when(blockingQueue.add(anyString())).thenReturn(true);
            Mockito.when(deltaSyncTask.getTaskName()).thenReturn("task");
            watchTask.run();
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
