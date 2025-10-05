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
@Component("e2eSamlAssertionParsingFilter")
@WebFilter(urlPatterns = "/wmds/*", filterName = "e2eSamlAssertionParsingFilter")
public class E2eSamlAssertionParsingFilter implements Filter {

    private static Filter e2eSamlAssertionParsingFilter;

    public static void setE2eSamlAssertionParsingFilter(final Filter filter) {
        E2eSamlAssertionParsingFilter.e2eSamlAssertionParsingFilter = filter;
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try{
            if (SecurityFilterAutoConfiguration.e2eTrustEnable && null != E2eSamlAssertionParsingFilter.e2eSamlAssertionParsingFilter) {
                LogUtil.debug(E2eSamlAssertionParsingFilter.class, "Do E2e Saml Assertion Parsing Filter.");
                E2eSamlAssertionParsingFilter.e2eSamlAssertionParsingFilter.doFilter(request, response, chain);
            }else{
                LogUtil.debug(E2eSamlAssertionParsingFilter.class, "E2E Trust not enabled");
                chain.doFilter(request, response);
            }
        }catch(Exception e){
            LogUtil.error(E2eSamlAssertionParsingFilter.class, "Do Filter fail", e);
            return;
        }
    }

    public void destroy() {
        E2eSamlAssertionParsingFilter.e2eSamlAssertionParsingFilter.destroy();
    }

}
