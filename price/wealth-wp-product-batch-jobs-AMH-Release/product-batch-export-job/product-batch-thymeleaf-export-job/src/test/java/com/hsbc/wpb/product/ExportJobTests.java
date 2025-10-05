package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ExportRequest;
import com.dummy.wpb.product.service.SystemParameterService;
import com.dummy.wpb.product.thymeleaf.expression.ProductUserDefinedMapping;
import com.dummy.wpb.product.utils.CommonUtils;
import org.apache.commons.lang3.RegExUtils;
import org.bson.BsonArray;
import org.bson.BsonDocumentReader;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = ExportApplication.class, args = "test.json")
@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class ExportJobTests {

    @MockBean
    MongoTemplate mongoTemplate;


    @Autowired
    ExportRequest request;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job exportJob;

    @MockBean
    private SystemParameterService systemParameterService;

    @Autowired
    ProductUserDefinedMapping userDefinedMapping;

    private Map<String, List<Document>> prodTypeCdeMap;
    private JobParameters jobParameters;

    private String outputPath;

    @Before
    public void setUp() throws Exception {
        prodTypeCdeMap = BsonArray
                .parse(CommonUtils.readResource("/product.json"))
                .getValues()
                .stream()
                .map(item -> new DocumentCodec().decode(new BsonDocumentReader(item.asDocument()), DecoderContext.builder().build()))

                .collect(Collectors.groupingBy(prod -> prod.getString(Field.prodTypeCde)));
        ArgumentMatcher<Query> argumentMatcher = argument -> argument != null
                && argument.getSkip() == 0 && !argument.getQueryObject().containsKey(Field._id);
        Mockito.when(mongoTemplate.find(argThat(argumentMatcher), eq(Document.class), eq(CollectionName.product.name()))).thenAnswer(
                answer -> {
                    Query query = answer.getArgument(0);
                    String prodTypeCde = query.getQueryObject().getString(Field.prodTypeCde);
                    return prodTypeCdeMap.getOrDefault(prodTypeCde, Collections.emptyList());
                }
        );
        Mockito.when(mongoTemplate.count(any(Query.class), eq(CollectionName.product.name()))).thenAnswer(
                answer -> {
                    Query query = answer.getArgument(0);
                    String prodTypeCde = query.getQueryObject().getString(Field.prodTypeCde);
                    return Long.parseLong(prodTypeCdeMap.getOrDefault(prodTypeCde, Collections.emptyList()).size() + "");
                }
        );
        outputPath = new ClassPathResource("").getFile().getAbsolutePath();
        jobParameters = new JobParametersBuilder()
                .addString(Field.ctryRecCde, request.getCtryRecCde())
                .addString(Field.grpMembrRecCde, request.getGrpMembrRecCde())
                .addString("systemCde", request.getSystemCde())
                .addString("outputPath", outputPath)
                .toJobParameters();

        Document udfMappingConfig = Document.parse(CommonUtils.readResource("/user-defined-field-mapping.json"));
        Mockito.when(mongoTemplate.findOne(
                        argThat(query -> query != null && query.getQueryObject().getString("name").equals("user-defined-field-mapping")),
                        eq(Document.class), eq(CollectionName.configuration.name())))
                .thenReturn(udfMappingConfig);
        ReflectionTestUtils.invokeMethod(userDefinedMapping, "init");
    }

    @Test
    public void test() throws Exception {
        Mockito.when(systemParameterService.getNextSequence(eq("SG"), eq("HBSP"), anyString())).thenReturn("1");
        jobLauncherTestUtils.setJob(exportJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Assert.assertTrue("Can not find ack file.", Files.exists(Paths.get(outputPath, "SG_HBSP_RBP_1.out")));
        List<ExecutionContext> contexts = jobExecution.getStepExecutions().stream()
                .map(StepExecution::getExecutionContext)
                .collect(Collectors.toList());

        List<Path> expectedFiles = Files.list(Paths.get(outputPath, "expected")).collect(Collectors.toList());
        for (ExecutionContext context : contexts) {
            List<String> actualFiles = (List<String>) context.get("files");
            Assert.assertTrue(actualFiles != null && !actualFiles.isEmpty());

            for (String actualFile : actualFiles) {
                //ignore timestamp in file
                Path expectedFilePath = expectedFiles.stream()
                        .filter(ep -> RegExUtils.removePattern(ep.getFileName().toString(), "\\d{14}")
                                .equals(RegExUtils.removePattern(actualFile, "\\d{14}")))
                        .findFirst()
                        .orElse(null);
                Assert.assertNotNull("Can not find expected file.", expectedFilePath);
                String expected = new String(Files.readAllBytes(expectedFilePath));
                String actual = new String(Files.readAllBytes(Paths.get(outputPath, actualFile)));
                if (actualFile.endsWith(".xml")) {
                    XMLAssert.assertXMLEqual(expected, actual);
                } else if (actualFile.endsWith(".csv")) {
                    //ignore first line with timestamp
                    Assert.assertEquals(expected.split("\n", 2)[1], actual.split("\n", 2)[1]);
                }
            }
        }
    }
}
