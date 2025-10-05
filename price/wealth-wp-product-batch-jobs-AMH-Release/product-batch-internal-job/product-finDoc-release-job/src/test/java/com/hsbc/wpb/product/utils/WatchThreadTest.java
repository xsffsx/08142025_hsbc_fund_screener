package com.dummy.wpb.product.utils;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WatchThreadTest {


    @Test
    void run_Should_ReadOutputFromProcessAndStoreInStream() throws InterruptedException {
        // Define the initial context
        String expectedOutput = "Hello, world!";

        Process mockedProcess = Mockito.mock(Process.class);
        Mockito.when(mockedProcess.getInputStream()).thenReturn(new ByteArrayInputStream(expectedOutput.getBytes()));

        // Create an instance of the WatchThread class and set the process
        WatchThread watchThread = new WatchThread(mockedProcess);
        // Trigger the scenario
        watchThread.start();
        Thread.sleep(2000);//NOSONAR
        watchThread.setOver(true);

        // Verify the expected outcome
        assertEquals(expectedOutput, watchThread.stream.get(0));
    }

}