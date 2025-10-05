package com.hhhh.group.secwealth.mktdata.common.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ErrResponseAdapterTest {

    @InjectMocks
    private ErrResponseAdapter adapter;

    @Test
    void test_init() throws Exception {
        ReflectionTestUtils.setField(adapter, "configPath", "exception/");
        ReflectionTestUtils.setField(adapter, "errorResponseConfig", "error-response-config.xml");
        ReflectionTestUtils.setField(adapter, "errorResponseMapping", "error-response-mapping.xml");
        adapter.init();
        assertNotNull(adapter);
    }
}