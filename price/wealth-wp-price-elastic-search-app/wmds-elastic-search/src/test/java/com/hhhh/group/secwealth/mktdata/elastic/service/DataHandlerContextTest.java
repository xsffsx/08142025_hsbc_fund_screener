package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.processor.ProductProcessor;
import com.hhhh.group.secwealth.mktdata.elastic.processor.StockInstmProductProcessor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataHandlerContextTest {
    @Mock
    private Map<String, ProductProcessor> processorContext;
    @InjectMocks
    private DataHandlerContext underTest;

    @Nested
    class WhenGettingProcessorByName {

        @Test
        void test_setApplicationContext() {
            ApplicationContext applicationContext = mock(ApplicationContext.class);
            Map<String, ProductProcessor> context = new HashMap<>();
            context.put("stockInstmProductProcessor", new StockInstmProductProcessor());
            when(applicationContext.getBeansOfType(ProductProcessor.class)).thenReturn(context);
            assertDoesNotThrow(() -> underTest.setApplicationContext(applicationContext));
        }

        @Test
        void test_getProcessorByName(){
            String nodeName = "stockinstm";
            ProductProcessor stockinstmProcessor = mock(StockInstmProductProcessor.class);
            when(processorContext.get(nodeName)).thenReturn(stockinstmProcessor);
            assertEquals(underTest.getProcessorByName("stockinstm"), stockinstmProcessor);
        }
    }
}
