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
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.wmd.itt.security.filter.opensaml.SamlGenerationFilter;

/**
 * <p>
 * <b> Filter for e2e trust. </b>
 * </p>
 */
@WebFilter(urlPatterns = "/wmds/*", filterName = "e2eSamlGenerationFilter")
@Component("e2eSamlGenerationFilter")
public class E2eSamlGenerationFilter implements Filter {

    private static Filter e2eSamlGenerationFilter;

    public static void setE2eSamlGenerationFilter(final Filter filter) {
        E2eSamlGenerationFilter.e2eSamlGenerationFilter = filter;
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            if(SecurityFilterAutoConfiguration.e2eTrustEnable && null != E2eSamlGenerationFilter.e2eSamlGenerationFilter) {
                LogUtil.debug(E2eChannelMappedFilter.class, "Do E2e Saml Generation Filter.");
                HttpServletRequest req = (HttpServletRequest) request;
                req.setAttribute(SamlGenerationFilter.DEFAULT_HTTP_PARAMETER_ATTRIBUTE_PREFIX + "channelId" , "OHB");
                E2eSamlGenerationFilter.e2eSamlGenerationFilter.doFilter(request, response, chain);
            }else {
                LogUtil.debug(E2eSamlAssertionParsingFilter.class, "E2E Trust not enabled");
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            LogUtil.error(E2eSamlGenerationFilter.class, "Do Filter fail", e);
            return;
        }
    }

    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    public void destroy() {
        E2eSamlGenerationFilter.e2eSamlGenerationFilter.destroy();
    }
}
