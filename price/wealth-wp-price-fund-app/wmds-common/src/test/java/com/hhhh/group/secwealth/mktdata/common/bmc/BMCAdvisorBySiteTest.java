package com.hhhh.group.secwealth.mktdata.common.bmc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BMCAdvisorBySiteTest {

    @InjectMocks
    private BMCAdvisorBySite advisorBySite;

    @Test
    void test_init() throws Exception {
        ReflectionTestUtils.setField(advisorBySite, "configPath", "bmc/");
        ReflectionTestUtils.setField(advisorBySite, "bmcExceptionConfig", "bmc-exception-config.xml");
        ReflectionTestUtils.setField(advisorBySite, "bmcExceptionMapping", "bmc-exception-mapping.xml");
        advisorBySite.init();
        Assertions.assertNull(null);


    }
}