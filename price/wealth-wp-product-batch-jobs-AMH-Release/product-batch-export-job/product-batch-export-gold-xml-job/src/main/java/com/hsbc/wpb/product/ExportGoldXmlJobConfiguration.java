package com.dummy.wpb.product;

import com.dummy.wpb.product.builder.GraphqlServiceItemReaderBuilder;
import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.ProductStatus;
import com.dummy.wpb.product.constant.ProductType;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.xml.DigitalAssetCurrency;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.GraphqlServiceItemReader;
import com.dummy.wpb.product.service.SystemParameterService;
import com.dummy.wpb.product.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@Slf4j
public class ExportGoldXmlJobConfiguration {

    @Value("${batch.chunk-size:5000}")
    private Integer chunkSize;

    @Value("${batch.outgoing.path}")
    private String outgoingPath;

    private final SystemParameterService systemParameterService;

    public ExportGoldXmlJobConfiguration(MongoOperations mongoOperations) {
        this.systemParameterService = new SystemParameterService(mongoOperations);
    }

    @Bean
    public Job exportGoldXmlJob(
            JobRepository jobRepository,
            Step goldPriceUpdateStep) {
        return new JobBuilder("Generate Gold XML Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(goldPriceUpdateStep)
                .end()
                .build();
    }

    @Bean
    public Step exportGoldXmlStep(
            PlatformTransactionManager transactionManager,
            JobRepository jobRepository,
            TaskExecutor executor,
            GraphqlServiceItemReader reader,
            ExportGoldXmlJobProcessor processor,
            SynchronizedItemStreamWriter<DigitalAssetCurrency> writer) {
        return new StepBuilder("exportGoldXmlStep")
                .<Document, DigitalAssetCurrency>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    @StepScope
    public GraphqlServiceItemReader productItemReader(GraphQLService graphQLService,
                                                      @Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                      @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                      @Value("#{jobParameters['prodStatCde']}") String prodStatCde,
                                                      @Value("#{jobParameters['fileType']}") String fileType) {
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.prodTypeCde).is(ProductType.DAC.name());

        // search with product status
        // if product status = ALL, then returns all products
        // if product status is empty, then returns non-delisted products
        if (StringUtils.isBlank(prodStatCde)) {
            criteria = criteria.and(Field.prodStatCde).ne(ProductStatus.D.name());
        } else if (!ProductStatus.ALL.name().equals(prodStatCde)){
            String[] prodTypes = StringUtils.split(prodStatCde, ",");
            if (prodTypes != null) {
                criteria = criteria.and(Field.prodStatCde).in(Arrays.asList(prodTypes));
            }
        }
        // search with product update time if file type = DELTA
        if (BatchConstants.FILE_TYPE_DELTA.equals(fileType)) {
            // read latest timestamp from database
            String lastSuccessTime = systemParameterService.getLastSuccessfulDateTime(
                    ctryRecCde, grpMembrRecCde, BatchConstants.EGRESS_GSOPSD_LAST_SUCCESSFUL_DT_TM);
            if (StringUtils.isNotBlank(lastSuccessTime)) {
                criteria = criteria.and(Field.recUpdtDtTm).gte(lastSuccessTime);
            }
        }

        // build graphql query
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/product-query.gql"));
        graphQLRequest.setVariables(Collections.singletonMap("filter", new Query(criteria).getQueryObject()));
        graphQLRequest.setDataPath("data.productByFilter");
        return new GraphqlServiceItemReaderBuilder()
                .name("productItemReader")
                .pageSize(chunkSize)
                .graphQLRequest(graphQLRequest)
                .graphQLService(graphQLService)
                .build();
    }

    @Bean(destroyMethod = "")
    @StepScope
    public SynchronizedItemStreamWriter<DigitalAssetCurrency> exportGoldXmlJobWriter(@Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                                                     @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                                                     @Value("#{jobParameters['systemCde']}") String systemCde) {
        // outgoing file settings
        // file path = /appvol/product-spring-batch/data/outgoing/HKHBAP/GSOPSD/
        String filePath = String.format("%s/%s/%s/", outgoingPath, ctryRecCde + grpMembrRecCde, systemCde);
        // file name = HK_HBAP_GSOPSD_DAC_20230818120522_3.xml
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(BatchConstants.FILENAME_DATETIME_PATTERN));
        System.setProperty(BatchConstants.EGRESS_GSOPSD_LAST_SUCCESSFUL_DT_TM, timestamp);
        // get latest sequence from database
        String sequence = systemParameterService.getNextSequence(ctryRecCde, grpMembrRecCde, BatchConstants.EGRESS_GSOPSD_SEQ);
        System.setProperty(BatchConstants.EGRESS_GSOPSD_SEQ, sequence);
        String fileName = String.format("%s_%s_%s_%s_%s_%s.xml",
                ctryRecCde, grpMembrRecCde, systemCde, ProductType.DAC.name(), timestamp, sequence);
        FileSystemResource resource = new FileSystemResource(filePath + fileName);
        // xml writer settings
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(DigitalAssetCurrency.class);
        StaxEventItemWriter<DigitalAssetCurrency> staxEventItemWriter = new StaxEventItemWriterBuilder<DigitalAssetCurrency>()
                .name("exportGoldXmlJobWriter")
                .marshaller(marshaller)
                .resource(resource)
                .rootTagName("digtlAssetCcyLst")
                .overwriteOutput(true)
                .build();
        SynchronizedItemStreamWriter<DigitalAssetCurrency> synchronizedItemStreamWriter = new SynchronizedItemStreamWriter<>();
        synchronizedItemStreamWriter.setDelegate(staxEventItemWriter);
        return synchronizedItemStreamWriter;
    }
}
