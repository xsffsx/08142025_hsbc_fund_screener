/*
 */
package com.hhhh.group.secwealth.mktdata.common.filter.e2etrust;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

/**
 * <p>
 * <b> Filter for e2e trust. </b>
 * </p>
 */
@Component("e2eChannelMappedFilter")
@WebFilter(urlPatterns = "/wmds/*", filterName = "e2eChannelMappedFilter")
public class E2eChannelMappedFilter implements Filter {

    private static Filter e2eChannelMappedFilter;

    public static void setE2eChannelMappedFilter(final Filter filter) {
        E2eChannelMappedFilter.e2eChannelMappedFilter = filter;
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            if (SecurityFilterAutoConfiguration.e2eTrustEnable && null != E2eChannelMappedFilter.e2eChannelMappedFilter) {
                LogUtil.debug(E2eChannelMappedFilter.class, "DO E2e Channel Mapped Filter.");
                E2eChannelMappedFilter.e2eChannelMappedFilter.doFilter(request, response, chain);
            }else{
                LogUtil.debug(E2eChannelMappedFilter.class, "E2E Trust not enabled");
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            LogUtil.error(E2eChannelMappedFilter.class, "Do Filter fail", e);
            return;
        }
    }

    public void destroy() {
        E2eChannelMappedFilter.e2eChannelMappedFilter.destroy();
    }

}
