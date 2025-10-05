/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.configuration;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.hhhh.group.secwealth.mktdata.starter.validation.properties.ValidationProperties;
import com.hhhh.group.secwealth.mktdata.starter.validation.service.ValidationService;
import com.hhhh.group.secwealth.mktdata.starter.validation.validator.util.ValidatorHelper;

@Configuration
@EnableConfigurationProperties(ValidationProperties.class)
@ConditionalOnProperty(prefix = "validation", value = "enabled", matchIfMissing = true)
public class ValidationAutoConfiguration implements EnvironmentAware {

    @Autowired
    private ValidationProperties properties;

    private Environment environment;

    @Bean
    public ResourceBundleMessageSource messageSource() {
        final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        source.setBasename("ValidationMessages");
        source.setUseCodeAsDefaultMessage(false);
        return source;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        final Properties props = new Properties();
        props.setProperty("hibernate.validator.fail_fast", "true");
        validator.setValidationProperties(props);
        validator.setProviderClass(HibernateValidator.class);
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    @Bean
    @ConditionalOnBean(LocalValidatorFactoryBean.class)
    @ConditionalOnMissingBean(ValidationService.class)
    public ValidationService validationService() {
        final ValidationService service = new ValidationService();
        service.setEnabled(this.properties.isEnabled());
        service.setPath(this.properties.getPath());
        service.setMapping(this.properties.getMapping());
        service.setConfig(this.properties.getConfig());
        service.setScanning(this.properties.getScanning());
        service.setEntityKey(this.properties.getEntityKey());
        service.setValidator(validator());
        return service;
    }

    @Bean
    @ConditionalOnMissingBean(ValidatorHelper.class)
    public ValidatorHelper validatorHelper() {
        final ValidatorHelper helper = new ValidatorHelper();
        helper.setEnv(this.environment);
        return helper;
    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

}
