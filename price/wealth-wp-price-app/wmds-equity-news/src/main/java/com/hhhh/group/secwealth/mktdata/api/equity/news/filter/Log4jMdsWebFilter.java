/*
 */

/**
 *
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class Log4jMdsWebFilter implements Filter {

    private static final String TRACEID = "X-B3-TraceId";

    private static final String SPANID = "X-B3-SpanId";

    private static final String PARENTID = "X-B3-ParentSpan";

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String traceId = httpServletRequest.getHeader(Log4jMdsWebFilter.TRACEID);
            String spanId = httpServletRequest.getHeader(Log4jMdsWebFilter.SPANID);
            String parentId = httpServletRequest.getHeader(Log4jMdsWebFilter.PARENTID);

            // Put the zipkinTracing Id provided by PCF go router
            if (StringUtils.isNotBlank(traceId)) {
                MDC.put(Log4jMdsWebFilter.TRACEID, traceId);
            }

            if (StringUtils.isNotBlank(spanId)) {
                MDC.put(Log4jMdsWebFilter.SPANID, spanId);
            }

            if (StringUtils.isNotBlank(parentId)) {
                MDC.put(Log4jMdsWebFilter.PARENTID, parentId);
            }
        }

        chain.doFilter(request, response);

        // Remove the keys before leave
        MDC.remove(Log4jMdsWebFilter.TRACEID);
        MDC.remove(Log4jMdsWebFilter.SPANID);
        MDC.remove(Log4jMdsWebFilter.PARENTID);

    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}

