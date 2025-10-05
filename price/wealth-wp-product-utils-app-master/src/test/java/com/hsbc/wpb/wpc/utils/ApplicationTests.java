package com.dummy.wpb.wpc.utils;

import com.dummy.wpb.wpc.utils.task.WatchTask;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ScheduledFuture;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTests {

    @InjectMocks
    private Application application;
    @Mock
    private ThreadPoolTaskScheduler taskScheduler;
    @Mock
    private WatchTask watchTask;
    @Mock
    private ConfigurableApplicationContext configurableApplicationContext;
    @Mock
    private ScheduledFuture scheduledFuture;
    private MockedStatic<SpringApplication> springApplicationMockedStatic;

    @Before
    public void setUp() {
        springApplicationMockedStatic = Mockito.mockStatic(SpringApplication.class);
    }

    @After
    public void tearDown() {
        springApplicationMockedStatic.close();
    }

    @Test
    public void testMain_givenStringArray_doesNotThrow() {
        try {
            springApplicationMockedStatic.when(() ->
                    SpringApplication.run(any(Application.class.getClass()), anyString())).thenReturn(configurableApplicationContext);
             Application.main(new String[]{"A"});
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testScheduleTasks_omitsArgs_doesNotThrow(){
        try {
            Mockito.doNothing().when(watchTask).setStopWatch(anyBoolean());
            Mockito.when(taskScheduler.scheduleWithFixedDelay(any(WatchTask.class), anyLong())).thenReturn(scheduledFuture);
            ReflectionTestUtils.setField(application, "healthCrons", new String[]{"0 0/15 * ? * *"});
            ReflectionTestUtils.setField(application, "healthTimezone", "UTC");
            Method scheduleTasks = application.getClass().getDeclaredMethod("scheduleTasks");
            scheduleTasks.setAccessible(true);
            scheduleTasks.invoke(application);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
