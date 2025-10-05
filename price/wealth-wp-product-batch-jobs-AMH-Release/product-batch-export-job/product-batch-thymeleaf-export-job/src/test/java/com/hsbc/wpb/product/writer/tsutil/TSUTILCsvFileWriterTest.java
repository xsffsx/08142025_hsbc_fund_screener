package com.dummy.wpb.product.writer.tsutil;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.thymeleaf.productExpressionFactory;
import com.dummy.wpb.product.thymeleaf.productProcessorDialect;
import com.dummy.wpb.product.writer.CsvTemplateWriter;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TSUTILCsvFileWriterTest {

    private MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);
    private String outputPath;


    private TSUTILCsvFileWriter tsutilPrcCsvFileWriter = new TSUTILCsvFileWriter();

    private CsvTemplateWriter tsutilIdCsvFileWriter = new CsvTemplateWriter();

    static {
        productProcessorDialect dialect = new productProcessorDialect();
        ReflectionTestUtils.setField(dialect, "productExpressionFactory", new productExpressionFactory());
        ReflectionTestUtils.invokeMethod(dialect, "init");
    }

    @Before
    public void setUp() throws IOException {
        tsutilIdCsvFileWriter.setTemplateName("/csv/TSUTIL_ID.html");
        tsutilPrcCsvFileWriter.setTemplateName("/csv/TSUTIL_PRC.html");
        outputPath = new ClassPathResource("").getFile().getAbsolutePath();
        tsutilIdCsvFileWriter.setResource(new FileSystemResource(outputPath + "/" + "TSUTIL_UT_ID.csv"));
        tsutilPrcCsvFileWriter.setResource(new FileSystemResource(outputPath + "/" + "TSUTIL_UT_PRC.csv"));
        tsutilIdCsvFileWriter.setName("tsutilIdCsvFileWriter");
        tsutilPrcCsvFileWriter.setName("tsutilPrcCsvFileWriter");

    }

    @Test
    public void testDoWrite() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        Document doc1 = new Document(Field.prodId, 6001L)
                .append("prodAltNumT", "000000000000000000000000006001")
                .append("prices", Arrays.asList(
                        new Document("prcEffDt", sdf.parse("2025-06-03 00:00:00")).append("prodNavPrcAmt", 1.3523),
                        new Document("prcEffDt", sdf.parse("2025-06-04 00:00:00")).append("prodNavPrcAmt", 2.2456)
                ));
        Document doc2 = new Document(Field.prodId, 6002L)
                .append("prodAltNumT", "000000000000000000000000006001")
                .append("prices", Arrays.asList(
                        new Document("prcEffDt", sdf.parse("2025-06-03 00:00:00")).append("prodNavPrcAmt", 1.6526),
                        new Document("prcEffDt", sdf.parse("2025-06-04 00:00:00")).append("prodNavPrcAmt", 2.5723)
                ));
        List<Document> docs = Arrays.asList(doc1, doc2, new Document(Field.prodId, 6003L)); // This one will be counted as invalid

        UpdateResult updateResult = UpdateResult.acknowledged(3L, 3L, null);
        when(mongoTemplate.updateMulti(any(Query.class), any(Update.class), eq(CollectionName.product.name())))
                .thenReturn(updateResult);

        tsutilPrcCsvFileWriter.setMongoTemplate(mongoTemplate);
        tsutilPrcCsvFileWriter.open(new ExecutionContext(new Document("count", 3)));
        tsutilPrcCsvFileWriter.write(docs);
        tsutilPrcCsvFileWriter.close();

        verify(mongoTemplate, times(1)).updateMulti(any(Query.class), any(Update.class), eq(CollectionName.product.name()));
        assertEquals(1, tsutilPrcCsvFileWriter.invalidCount);

        tsutilIdCsvFileWriter.open(new ExecutionContext(new Document("count", 3)));
        tsutilIdCsvFileWriter.write(docs);
        tsutilIdCsvFileWriter.close();


        assertEquals(new String(Files.readAllBytes(Paths.get(outputPath + "/expected/TSUTIL_UT_PRC.csv"))),
                new String(Files.readAllBytes(Paths.get(outputPath + "/TSUTIL_UT_PRC.csv"))));
        assertEquals(new String(Files.readAllBytes(Paths.get(outputPath + "/expected/TSUTIL_UT_ID.csv"))),
                new String(Files.readAllBytes(Paths.get(outputPath + "/TSUTIL_UT_ID.csv"))));
    }

}
