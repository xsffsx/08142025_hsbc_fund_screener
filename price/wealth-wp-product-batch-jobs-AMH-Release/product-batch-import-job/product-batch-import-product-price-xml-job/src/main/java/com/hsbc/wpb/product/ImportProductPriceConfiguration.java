package com.dummy.wpb.product;


import com.dummy.wpb.product.condition.ConditionalOnJobParameters;
import com.dummy.wpb.product.listener.SystemCdeCheckListener;
import com.dummy.wpb.product.model.ProductPriceStreamItem;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.processor.MdsbePriceProcessor;
import com.dummy.wpb.product.processor.PrcCmparProcessor;
import com.dummy.wpb.product.processor.PriceHistoryProcessor;
import com.dummy.wpb.product.processor.ProductPriceValidator;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.writer.ProductPriceImportWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.ProxyUtils;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import static com.dummy.wpb.product.ImportProductPriceXmlJobApplication.JOB_NAME;

@Configuration
public class ImportProductPriceConfiguration {

    @Value("${batch.size}")
    private int batchSize;

    @Bean
    public Job importProductPriceJob(Step importProductPriceStep, JobRepository jobRepository,
                                     GraphQLService graphQLService, SystemCdeCheckListener systemCdeCheckListener) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(systemCdeCheckListener)
                .listener(new PriceLimitHolder(graphQLService))
                .flow(importProductPriceStep)
                .end()
                .build();
    }

    @Bean
    public Step importProductPriceStep(PlatformTransactionManager transactionManager,
                                       JobRepository jobRepository,
                                       CompositeItemProcessor<ProductStreamItem, ProductPriceStreamItem> productPriceProcessor,
                                       ProductPriceImportWriter productPriceImportWriter,
                                       ItemStreamReader<ProductStreamItem> productPriceXmlFileReader) {
        return new StepBuilder("importProductPriceStep")
                .<ProductStreamItem, ProductPriceStreamItem>chunk(batchSize)
                .reader(productPriceXmlFileReader)
                .processor(productPriceProcessor)
                .writer(productPriceImportWriter)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    public ValidatingItemProcessor<ProductPriceStreamItem> prodPriceValidatorProcessor() {
        ValidatingItemProcessor<ProductPriceStreamItem> prodPriceValidatorProcessor = new ValidatingItemProcessor<>();
        prodPriceValidatorProcessor.setValidator(productPriceValidator());
        prodPriceValidatorProcessor.setFilter(true);
        return prodPriceValidatorProcessor;
    }

    @Bean
    @StepScope
    public ProductPriceValidator productPriceValidator() {
        return new ProductPriceValidator();
    }

    @Bean
    @StepScope
    public PriceHistoryProcessor priceHistoryProcessor() {
        return new PriceHistoryProcessor();
    }

    @Bean
    @StepScope
    public PrcCmparProcessor prcCmparProcessor() {
        return new PrcCmparProcessor();
    }

    @Bean
    @StepScope
    @ConditionalOnJobParameters({"ctryRecCde", "HK", "systemCde", "MDSBE"})
    public MdsbePriceProcessor mdsbePriceFormatter() {
        return new MdsbePriceProcessor();
    }

    @Bean
    public CompositeItemProcessor<ProductStreamItem, ProductPriceStreamItem> productPriceProcessor(List<ItemProcessor<? extends ProductStreamItem, ProductPriceStreamItem>> processors) {
        CompositeItemProcessor<ProductStreamItem, ProductPriceStreamItem> productPriceProcessor = new CompositeItemProcessor<>();
        // As there are some processors depend on the predecessor data, then we need to sort processors here
        processors.sort((a, b) -> getOrder(a) < getOrder(b) ? -1 : 1);
        productPriceProcessor.setDelegates(processors);
        return productPriceProcessor;
    }

    /**
     * Sort the Item processor by annotation Order.class, for those who without the annotation will return -1
     * Please make sure set positive vale for the one created by yourself
     * @param processor Item Processor
     * @return -1 - the processor without Order annotation; 0 - 2147483647 - get from the Order Annotation
     */
    private int getOrder(ItemProcessor<?, ?> processor) {
        Class<?> processorClazz = ProxyUtils.getUserClass(processor.getClass());
        if (!processorClazz.isAnnotationPresent(Order.class)) return -1;
        return processorClazz.getAnnotation(Order.class).value();
    }
}
