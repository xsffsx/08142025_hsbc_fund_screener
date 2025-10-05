package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.BondCharacter;
import com.dummy.wpb.product.util.LegacyConfig;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.writer.BondCharacterIndUpdateItemWriter;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;


@SpringJUnitConfig
@SpringBatchTest
@SpringBootTest(classes = ImportBondCharacterIndJonApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportBondCharacterIndJobTest {

    static JobParameters jobParameters;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    private LegacyConfig legacyConfig;

    @MockBean
    private MongoCollection<Document> productCollection;


    private FindIterable<Document> findIterableDoc = Mockito.mock(FindIterable.class);

    private FindIterable<Document> findIterableDoc2 = Mockito.mock(FindIterable.class);

    private BulkWriteResult bulkWriteResult = Mockito.mock(BulkWriteResult.class);

    @MockBean
    private MongoDatabase mongoDatabase;

    static JobExecution jobExecution;

    static BondCharacter bondCharacter = new BondCharacter();


    @BeforeClass
    public static void setUp() throws IOException {
        jobParameters = new JobParametersBuilder()
                .addString("ctryRecCde", "HK")
                .addString("grpMembrRecCde", "HBAP")
                .addString("incomingPath", new ClassPathResource("/test").getFile().getAbsolutePath())
                .toJobParameters();
        bondCharacter.setCtryRecCde("HK");
        bondCharacter.setGrpMembrRecCde("HBAP");
        bondCharacter.setProdAltPrimNum("48243");
        bondCharacter.setQtyTypeCde("FMT");
        bondCharacter.setProdLocCde("CMU");
        bondCharacter.setRbpMigrInd("N");
        jobExecution = MetaDataInstanceFactory.createJobExecution("importBondCharacterIndJob", 1L, 1L, jobParameters);
    }


    public JobExecution getJobExecution() {
        return jobExecution;
    }

    public StepExecution getStepExecution() {
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void testCharacterIndReader() throws Exception {
        ItemStreamReader<Object> bondCharacterIndReader = applicationContext.getBean("bondCharacterIndReader", ItemStreamReader.class);
        bondCharacterIndReader.open(new ExecutionContext());
        Object header = bondCharacterIndReader.read();
        Assert.assertTrue(header.toString().contains("File Header"));
        Assert.assertEquals(BondCharacter.class, bondCharacterIndReader.read().getClass());
        Object tail;
        while (!((tail = bondCharacterIndReader.read()) instanceof String) && tail != null){}
        Assert.assertTrue(tail.toString().contains("File Trailer"));
    }

    @Test
    public void testCharacterIndProcessor() throws Exception {
        ItemProcessor<Object, BondCharacter> processor = applicationContext.getBean("bcIndProcessor", ItemProcessor.class);
        Object bondCharacterAfterProcess = processor.process(bondCharacter);
        Assert.assertEquals(bondCharacter, bondCharacterAfterProcess);
        
        String header = "File Header: [HEADERWPC Ext Info Syn. iF   RSWPEIP dummydummy20240926180212P]";
        Assert.assertNull(processor.process(header));
        String trailer = "File Trailer: [TRAILERWPC Ext Info Syn. iF   RSWPEIP dummydummy0062578000000000000000+]";
        Assert.assertNull(processor.process(trailer));
        String dummy = "hello world";
        Assert.assertNull(processor.process(dummy));
    }

    @Test
    public void testCharacterIndWriter() {
        try {
            Mockito.when(mongoDatabase.getCollection(any())).thenReturn(productCollection);
            BondCharacterIndUpdateItemWriter writer = new BondCharacterIndUpdateItemWriter(mongoDatabase);
            ReflectionTestUtils.setField(writer, "ctryRecCde", "HK");
            ReflectionTestUtils.setField(writer, "grpMembrRecCde", "HBAP");

            BondCharacter bondCharacter = new BondCharacter();
            bondCharacter.setCtryRecCde("HK");
            bondCharacter.setGrpMembrRecCde("HBAP");
            bondCharacter.setProdAltPrimNum("XS0526606537");
            bondCharacter.setQtyTypeCde("FMT");
            bondCharacter.setRbpMigrInd("N");
            bondCharacter.setProdLocCde("EOC");

            Document product = new Document();
            product.put(Field.prodId, 123456L);
            product.put(Field.ctryRecCde, "HK");
            product.put(Field.grpMembrRecCde, "HBAP");
            product.put(Field.prodAltPrimNum, "XS0526606537");
            product.put(Field.qtyUnitProdCde, "FMT");
            product.put(Field.prodLocCde, "EOC");
            JsonPathUtils.setValue(product, "debtInstm.rbpMigrInd", "Y");

            ArgumentMatcher<Bson> queryArgumentMatcher1 = query -> query != null && query.toString().contains("HBAP");
            Mockito.when(productCollection.find(argThat(queryArgumentMatcher1))).thenReturn(findIterableDoc);
            Mockito.when(findIterableDoc.projection(any())).thenReturn(findIterableDoc2);
            Mockito.when(findIterableDoc2.into(any(List.class))).thenReturn(Collections.singletonList(product));

            Document product2 = new Document();
            product2.put(Field.prodId, 123457L);
            product2.put(Field.ctryRecCde, "HK");
            product2.put(Field.grpMembrRecCde, "dummy");
            product2.put(Field.prodAltPrimNum, "XS0526606537");
            product2.put(Field.qtyUnitProdCde, "FMT");
            product2.put(Field.prodLocCde, "EOO");
            JsonPathUtils.setValue(product2, "debtInstm.rbpMigrInd", "Y");

            ArgumentMatcher<Bson> queryArgumentMatcher2 = query -> query != null && query.toString().contains("dummy");
            Mockito.when(productCollection.find(argThat(queryArgumentMatcher2))).thenReturn(findIterableDoc2);
            Mockito.when(findIterableDoc2.projection(any())).thenReturn(findIterableDoc);
            Mockito.when(findIterableDoc.into(any())).thenReturn(Collections.singletonList(product2));
            Mockito.when(productCollection.bulkWrite(any())).thenReturn(bulkWriteResult);
            Mockito.when(bulkWriteResult.getModifiedCount()).thenReturn(2);

            writer.write(Collections.singletonList(bondCharacter));
        } catch (Exception e) {
            Assert.fail("UnExpected error arised.");
        }
    }

    @Test
    public void testCharacterIndWriter_logUnexpectUpdateCount() {
        Mockito.when(mongoDatabase.getCollection(any())).thenReturn(productCollection);
        BondCharacterIndUpdateItemWriter writer = new BondCharacterIndUpdateItemWriter(mongoDatabase);
        ReflectionTestUtils.setField(writer, "ctryRecCde", "HK");
        ReflectionTestUtils.setField(writer, "grpMembrRecCde", "HBAP");

        BondCharacter bondCharacter = new BondCharacter();
        bondCharacter.setCtryRecCde("HK");
        bondCharacter.setGrpMembrRecCde("HBAP");
        bondCharacter.setProdAltPrimNum("XS0526606537");
        bondCharacter.setQtyTypeCde("FMT");
        bondCharacter.setRbpMigrInd("N");
        bondCharacter.setProdLocCde("EOC");

        Document product = new Document();
        product.put(Field.prodId, 123456L);
        product.put(Field.ctryRecCde, "HK");
        product.put(Field.grpMembrRecCde, "HBAP");
        product.put(Field.prodAltPrimNum, "XS0526606537");
        product.put(Field.qtyUnitProdCde, "UNT");
        product.put(Field.prodLocCde, "EOC");
        JsonPathUtils.setValue(product, "debtInstm.rbpMigrInd", "Y");

        ArgumentMatcher<Bson> queryArgumentMatcher1 = query -> query != null && query.toString().contains("HBAP");
        Mockito.when(productCollection.find(argThat(queryArgumentMatcher1))).thenReturn(findIterableDoc);
        Mockito.when(findIterableDoc.projection(any())).thenReturn(findIterableDoc2);
        Mockito.when(findIterableDoc2.into(any(List.class))).thenReturn(Collections.singletonList(product));

        Document product2 = new Document();
        product2.put(Field.prodId, 123457L);
        product2.put(Field.ctryRecCde, "HK");
        product2.put(Field.grpMembrRecCde, "dummy");
        product2.put(Field.prodAltPrimNum, "XS0526606537");
        product2.put(Field.qtyUnitProdCde, "FMT");
        product2.put(Field.prodLocCde, "EOC");
        JsonPathUtils.setValue(product2, "debtInstm.rbpMigrInd", "N");
        ArgumentMatcher<Bson> queryArgumentMatcher2 = query -> query != null && query.toString().contains("dummy");
        Mockito.when(productCollection.find(argThat(queryArgumentMatcher2))).thenReturn(findIterableDoc2);
        Mockito.when(findIterableDoc2.projection(any())).thenReturn(findIterableDoc);
        Mockito.when(findIterableDoc.into(any())).thenReturn(Collections.singletonList(product2));
        Mockito.when(productCollection.bulkWrite(any())).thenReturn(bulkWriteResult);
        Mockito.when(bulkWriteResult.getModifiedCount()).thenReturn(2);

        writer.write(Collections.singletonList(bondCharacter));
        ArgumentCaptor<List> updateCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(productCollection, times(1)).bulkWrite(updateCaptor.capture());

        Assert.assertEquals(1, updateCaptor.getValue().size());
    }

    @Test
    public void testCharacterIndWriter_emptyBondCharacter_findEmptyProduct_emptyBatchUpdateList() {
        try {
            Mockito.when(mongoDatabase.getCollection(any())).thenReturn(productCollection);
            BondCharacterIndUpdateItemWriter writer = new BondCharacterIndUpdateItemWriter(mongoDatabase);
            ReflectionTestUtils.setField(writer, "ctryRecCde", "HK");
            ReflectionTestUtils.setField(writer, "grpMembrRecCde", "HBAP");

            Mockito.when(productCollection.find(any(Bson.class))).thenReturn(findIterableDoc);
            Mockito.when(findIterableDoc.projection(any())).thenReturn(findIterableDoc2);
            Mockito.when(findIterableDoc2.into(any(List.class))).thenReturn(new ArrayList());

            writer.write(Collections.singletonList(new BondCharacter()));
            writer.write(Collections.emptyList());

            BondCharacter bondCharacter = new BondCharacter();
            bondCharacter.setCtryRecCde("HK");
            bondCharacter.setGrpMembrRecCde("HBAP");
            bondCharacter.setProdAltPrimNum("XS0526606537");
            bondCharacter.setQtyTypeCde("FMT");
            bondCharacter.setRbpMigrInd("N");
            bondCharacter.setProdLocCde("EOC");

            Document product = new Document();
            product.put(Field.prodId, 123456L);
            product.put(Field.ctryRecCde, "HK");
            product.put(Field.grpMembrRecCde, "HBAP");
            product.put(Field.prodAltPrimNum, "XS0526606537");
            product.put(Field.qtyUnitProdCde, "FMT");
            product.put(Field.prodLocCde, "EOC");
            JsonPathUtils.setValue(product, "debtInstm.rbpMigrInd", "N");

            Mockito.when(productCollection.find(any(Bson.class))).thenReturn(findIterableDoc);
            Mockito.when(findIterableDoc.projection(any())).thenReturn(findIterableDoc2);
            Mockito.when(findIterableDoc2.into(any(List.class))).thenReturn(Collections.singletonList(product));
            writer.write(Collections.singletonList(bondCharacter));
        } catch (Exception e) {
            Assert.fail("UnExpected error arised.");
        }
    }


}