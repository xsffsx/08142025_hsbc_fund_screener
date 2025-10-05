/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.elastic.validator.ContainerValidator;

@Configuration
public class ValidationBean {

    @Value("${validator.basePath}")
    private String basePath;

    @Value("${validator.xssValidator}")
    private String xssValidatorFilePath;

    @Bean(initMethod = "init")
    public ContainerValidator xssValidator() {
        ContainerValidator validator = new ContainerValidator();
        validator.setBasePath(this.basePath);
        validator.setFilePath(this.xssValidatorFilePath);
        return validator;
    }

}
