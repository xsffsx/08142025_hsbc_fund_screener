/*
 */
package com.hhhh.group.secwealth.mktdata.common.filter.gzip;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

@Component("gZipFilter")
@WebFilter(urlPatterns = "/wmds/*",initParams={@WebInitParam(name="targetBeanName",value="gZipFilter"),@WebInitParam(name= "targetFilterLifecycle", value= "true"),}, filterName = "gZipFilter")
public class GZipFilter implements Filter {

    @Value("#{systemConfig['gzip.enabled']}")
    private boolean gzipEnabled;

    private final String GET_METHOD = "get";
    private final String POST_METHOD = "post";

    @Override
    public void destroy() {}

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
        ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getMethod();
        if (this.GET_METHOD.equalsIgnoreCase(method) || this.POST_METHOD.equalsIgnoreCase(method)) {
            if (true == this.gzipEnabled && supportsGzip(request)) {
                LogUtil.debug(GZipFilter.class, "do GZipFilter");
                GZIPResponseWrapper gzipResponse = new GZIPResponseWrapper((HttpServletResponse) response);
                chain.doFilter(request, gzipResponse);
                gzipResponse.finish();
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {}

    /**
     * 
     * <p>
     * <b> Set Accept-Encoding of Request Header value is gzip will supports
     * Gzip json. </b>
     * </p>
     * 
     * @param request
     * @return
     */
    private boolean supportsGzip(final ServletRequest request) {
        String acceptEncoding = ((HttpServletRequest) request).getHeader("Accept-Encoding");
        return acceptEncoding != null && acceptEncoding.contains("gzip");
    }

}