/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_message_logger;

import com.hhhh.group.secwealth.mktdata.starter.http_message_logger.interceptor.HttpRequestMessageInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.hhhh.group.secwealth.mktdata.starter.http_message_logger.configuration.HttpMessageLoggerAutoConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HttpMessageLoggerAutoConfiguration.class)
public class HttpMessageLoggerTest {

    @Test
    public void testLoad() throws Exception {

        HttpRequestMessageInterceptor httpRequestMessageInterceptor = new HttpRequestMessageInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();

        request.addHeader("TEST-HEADER_00", "test-header-00");
        request.addHeader("TEST-HEADER_01", "test-header-01");
        request.addHeader("TEST-HEADER_02", "test-header-02");

        httpRequestMessageInterceptor.preHandle(request, response, handler);

    }

}
