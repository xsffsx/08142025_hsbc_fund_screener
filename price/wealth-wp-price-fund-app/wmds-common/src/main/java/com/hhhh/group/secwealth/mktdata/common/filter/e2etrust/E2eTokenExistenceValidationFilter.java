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



@Component("e2eTokenExistenceValidationFilter")
@WebFilter(urlPatterns = "/wmds/*", filterName = "e2eTokenExistenceValidationFilter")
public class E2eTokenExistenceValidationFilter implements Filter{

    private static Filter e2eTokenExistenceValidationFilter;

    public static void setE2eTokenExistenceValidationFilter(final Filter filter) {
        E2eTokenExistenceValidationFilter.e2eTokenExistenceValidationFilter = filter;
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try{
            if (SecurityFilterAutoConfiguration.e2eTrustEnable && null != E2eTokenExistenceValidationFilter.e2eTokenExistenceValidationFilter) {
                LogUtil.debug(E2eTokenExistenceValidationFilter.class, "E2e Token Existence Validation Filter.");
                E2eTokenExistenceValidationFilter.e2eTokenExistenceValidationFilter.doFilter(request, response, chain);
            }else{
                LogUtil.debug(E2eTokenExistenceValidationFilter.class, "E2E Trust not enabled");
                chain.doFilter(request, response);
            }
        }catch(Exception e){
            LogUtil.error(E2eTokenExistenceValidationFilter.class, "Do Filter fail", e);
            return;
        }
    }

    public void destroy() {
        E2eTokenExistenceValidationFilter.e2eTokenExistenceValidationFilter.destroy();
    }

}
