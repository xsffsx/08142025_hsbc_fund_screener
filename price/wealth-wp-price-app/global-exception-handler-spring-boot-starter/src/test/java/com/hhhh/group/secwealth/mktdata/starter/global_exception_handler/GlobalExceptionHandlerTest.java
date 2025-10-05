/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.configuration.GlobalExceptionHandlerAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GlobalExceptionHandlerAutoConfiguration.class)
public class GlobalExceptionHandlerTest {

    @Test
    public void testLoad() {}

}
