package com.dummy.wpb.product.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class EliFinDocPathListenerTest {

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private EliFinDocPathListener pathListener;


    @Test
    void testHandleFileCreation() {
        executorService = Mockito.mock(ExecutorService.class);
        pathListener.setExecutorService(executorService);
        Mockito.doNothing().when(executorService).execute(Mockito.any());
        File file = new File("path/to/file.txt");
        pathListener.onFileCreate(file);
        assertTrue(true);
    }

}