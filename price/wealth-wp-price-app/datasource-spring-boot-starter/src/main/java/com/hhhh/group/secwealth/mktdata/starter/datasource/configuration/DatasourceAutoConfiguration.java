/*
 */
package com.hhhh.group.secwealth.mktdata.starter.datasource.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.hhhh.group.secwealth.mktdata.starter.datasource.aspect.DatasourceAspect;
import com.hhhh.group.secwealth.mktdata.starter.datasource.component.DynamicDatasource;
import com.hhhh.group.secwealth.mktdata.starter.datasource.component.JndiDatasourceFactory;
import com.hhhh.group.secwealth.mktdata.starter.datasource.interceptor.DatasourceInterceptor;
import com.hhhh.group.secwealth.mktdata.starter.datasource.interceptor.DatasourceInterceptorConfigurer;
import com.hhhh.group.secwealth.mktdata.starter.datasource.properties.DatasourceProperties;

@Configuration
@EnableConfigurationProperties(DatasourceProperties.class)
@ConditionalOnProperty(prefix = "datasource", value = "enabled", matchIfMissing = false)
public class DatasourceAutoConfiguration {

    @Autowired
    private DatasourceProperties properties;

    @Bean
    @ConditionalOnMissingBean(JndiDatasourceFactory.class)
    public JndiDatasourceFactory jndiDatasourceFactory() {
        final JndiDatasourceFactory jndiDatasourceFactory = new JndiDatasourceFactory();
        jndiDatasourceFactory.setDatasourceName(this.properties.getName());
        jndiDatasourceFactory.setJndiName(this.properties.getJndiName());
        jndiDatasourceFactory.setDefaultDatasourceName(this.properties.getDefaultName());
        jndiDatasourceFactory.setDefaultExCode(this.properties.getDefaultExCode());
        return jndiDatasourceFactory;
    }

    @Bean("dataSource")
    @Primary
    @ConditionalOnBean(JndiDatasourceFactory.class)
    public DataSource dataSource() throws Exception {
        final DynamicDatasource dataSource = new DynamicDatasource();
        dataSource.setTargetDataSources(jndiDatasourceFactory().getTargetDataSources());
        dataSource.setDefaultTargetDataSource(jndiDatasourceFactory().getDefaultDataSource());
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean(DatasourceInterceptor.class)
    public DatasourceInterceptor datasourceInterceptor() {
        final DatasourceInterceptor datasourceInterceptor = new DatasourceInterceptor();
        datasourceInterceptor.setPattern(this.properties.getPattern());
        datasourceInterceptor.setDefaultExCode(this.properties.getDefaultExCode());
        return datasourceInterceptor;
    }

    @Bean
    @ConditionalOnBean(DatasourceInterceptor.class)
    @ConditionalOnMissingBean(DatasourceInterceptorConfigurer.class)
    public DatasourceInterceptorConfigurer datasourceInterceptorConfigurer() {
        final DatasourceInterceptorConfigurer datasourceInterceptorConfigurer = new DatasourceInterceptorConfigurer();
        datasourceInterceptorConfigurer.setUrlPatterns(this.properties.getUrlPatterns());
        datasourceInterceptorConfigurer.setDatasourceInterceptor(datasourceInterceptor());
        return datasourceInterceptorConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean(DatasourceAspect.class)
    public DatasourceAspect datasourceAspect() {
        final DatasourceAspect datasourceAspect = new DatasourceAspect();
        datasourceAspect.setDefaultExCode(this.properties.getDefaultExCode());
        return datasourceAspect;
    }

}
