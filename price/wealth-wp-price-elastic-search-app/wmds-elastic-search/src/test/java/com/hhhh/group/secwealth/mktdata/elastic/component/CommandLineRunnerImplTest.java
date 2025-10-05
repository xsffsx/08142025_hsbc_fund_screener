package com.hhhh.group.secwealth.mktdata.elastic.component;

import com.hhhh.group.secwealth.mktdata.elastic.service.ScheduleDataLoadService;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandLineRunnerImplTest {
    @Mock
    private ScheduleDataLoadService scheduleDataLoadService;
    @InjectMocks
    private CommandLineRunnerImpl underTest;

    @Nested
    class WhenRuning {

        private String[] args;

        @Test
        void test_run() {
            assertDoesNotThrow(() -> underTest.run(args));
        }

        @Test
        void test_run_exception() throws Exception {
            doThrow(ApplicationException.class).when(scheduleDataLoadService).loadData();
            assertThrows(ApplicationException.class, () -> underTest.run(args));
        }
    }
}
