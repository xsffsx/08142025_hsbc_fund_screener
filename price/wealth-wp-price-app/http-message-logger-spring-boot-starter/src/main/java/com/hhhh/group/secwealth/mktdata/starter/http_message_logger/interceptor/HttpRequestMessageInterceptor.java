/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_message_logger.interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hhhh.group.secwealth.mktdata.starter.http_message_logger.constant.Constant;

public class HttpRequestMessageInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestMessageInterceptor.class);

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final Exception ex) throws Exception {
        // intentionally-blank override
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final ModelAndView modelAndView) throws Exception {
        // intentionally-blank override
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
        throws Exception {
        final String path = request.getRequestURI();
        final String query = request.getQueryString();
        HttpRequestMessageInterceptor.logger.info(URLDecoder
            .decode(Constant.INBOUND_MSG_PREFIX + path + Constant.SYMBOL_INTERROGATION + query, StandardCharsets.UTF_8.name()));
        HttpRequestMessageInterceptor.logger.info(getHeaderAsString(request));

        return true;
    }

    private String getHeaderAsString(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        List<String> headerStrList = new ArrayList<>();
        while (headers.hasMoreElements()) {
            final String header = headers.nextElement();
            headerStrList.add(header + Constant.SYMBOL_COLON + request.getHeader(header));
        }

        return StringUtils.arrayToCommaDelimitedString(headerStrList.toArray());
    }

}
