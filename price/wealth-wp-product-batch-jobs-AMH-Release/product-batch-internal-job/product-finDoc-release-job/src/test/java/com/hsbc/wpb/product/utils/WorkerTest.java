package com.dummy.wpb.product.utils;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {

    @Test
    void run_Should_SetExitStatusToNonNull_When_ProcessCompletes() throws InterruptedException {
        Process mockedProcess = Mockito.mock(Process.class);
        Worker worker = new Worker(mockedProcess);
        worker.start();
        worker.join();
        assertNotNull(worker.getExit());
    }

    @Test
    void processInLimitTime_Should_ThrowTimeoutException_When_ProcessTakesTooLong() {
        Process mockedProcess = Mockito.mock(Process.class);
        Worker worker = new Worker(mockedProcess);
        assertThrows(TimeoutException.class, () -> worker.processInLimitTime(worker, 1));
    }

    @Test
    void processInLimitTime_Should_ReturnWorker_When_ProcessCompletesWithinLimit() throws TimeoutException, InterruptedException {
        Process mockedProcess = Mockito.mock(Process.class);
        Worker worker = new Worker(mockedProcess);
        Worker result = worker.processInLimitTime(worker, 36000);
        assertEquals(worker, result);
    }

}