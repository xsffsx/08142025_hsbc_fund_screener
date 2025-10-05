package com.dummy.wpb.product;


import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.Arrays;

import static com.dummy.wpb.product.ImportReutersBondJobApplication.JOB_NAME;

@Configuration
public class ImportReutersBondConfiguration {

    @Bean
    public Job importBondCsvJob(Step importBondCsvStep, JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(importBondCsvStep)
                .end()
                .build();
    }

    @Bean
    public Step importBondCsvStep(PlatformTransactionManager transactionManager,
                                  JobRepository jobRepository,
                                  BondReutersReader bondReutersReader,
                                  CompositeItemProcessor<BondReutersStreamItem, BondReutersStreamItem> bondReutersProcessor,
                                  BondReutersItemWriter bondReutersItemWriter) {
        return new StepBuilder("importBondReutersStep")
                .<BondReutersStreamItem, BondReutersStreamItem>chunk(50)
                .reader(bondReutersReader)
                .processor(bondReutersProcessor)
                .writer(bondReutersItemWriter)
                .faultTolerant()
                .skip(InvalidRecordException.class)
                .skipLimit(Integer.MAX_VALUE)
                .transactionManager(transactionManager)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    public CompositeItemProcessor<BondReutersStreamItem, BondReutersStreamItem> bondReutersProcessor(
            ValidatingItemProcessor<BondReutersStreamItem> bondReuterValidatingProcessor,
            BondReutersFormatProcessor bondReutersFormatProcessor) {
        return new CompositeItemProcessorBuilder<BondReutersStreamItem, BondReutersStreamItem>()
                .delegates(bondReuterValidatingProcessor, bondReutersFormatProcessor)
                .build();
    }

    @Bean
    @StepScope
    public BondReutersValidator bondReutersValidator(ReferenceDataService referenceDataService) {
        return new BondReutersValidator(referenceDataService);
    }

    @Bean
    @StepScope
    public ValidatingItemProcessor<BondReutersStreamItem> bondReuterValidatingProcessor(BondReutersValidator bondReutersValidator) {
        ValidatingItemProcessor<BondReutersStreamItem> bondReutersValidateProcessor = new ValidatingItemProcessor<>();
        bondReutersValidateProcessor.setValidator(bondReutersValidator);
        bondReutersValidateProcessor.setFilter(true);
        return bondReutersValidateProcessor;
    }

    @Bean
    public BondReutersItemWriter bondReutersItemWriter(ProductService productService, ReferenceDataService referenceDataService) {
        return new BondReutersItemWriter(productService, referenceDataService);
    }

    @Bean
    @StepScope
    public BondReutersFormatProcessor bondReutersFormatProcessor() {
        return new BondReutersFormatProcessor();
    }

    @Value("${BOND.REUTERS.PROXYHOST}")
    private String proxyHost = null;

    @Value("${BOND.REUTERS.PROXYPORT}")
    private int proxyPort = 0;

    @Bean
    public RestTemplate bondRestTemplate(SSLContext sslContext) {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setMaxConnTotal(800)
                .setMaxConnPerRoute(400)
                .setProxy(new HttpHost(proxyHost, proxyPort, "http"))
                .build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectTimeout(10000);
        clientHttpRequestFactory.setReadTimeout(10000);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN));
        restTemplate.getMessageConverters().add(jsonConverter);
        return restTemplate;
    }

    @Bean
    @StepScope
    public BondReutersReader bondReutersReader(BondReutersService bondReutersService) {
        return new BondReutersReader(bondReutersService);
    }

}
