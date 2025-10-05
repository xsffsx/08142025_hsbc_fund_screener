package com.hhhh.group.secwealth.mktdata.api.equity.quotes.filter.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.filter.Log4jMdsWebFilter;


@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean mdsFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        Log4jMdsWebFilter log4jMdsWebFilter = new Log4jMdsWebFilter();
        registrationBean.setFilter(log4jMdsWebFilter);
        registrationBean.setOrder(Integer.MAX_VALUE);
        List<String> urls = new ArrayList<>();
        urls.add("/wealth/api/v1/market-data/*");
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }

}
