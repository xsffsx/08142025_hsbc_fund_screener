package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TSUTILCsvFileReaderTest {

    private TSUTILCsvFileReader reader = new TSUTILCsvFileReader();

    private MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);

    @Before
    public void setUp() {
        reader.setCollection(CollectionName.prod_prc_hist);
        ReflectionTestUtils.setField(reader, "mongoTemplate", mongoTemplate);
    }

    @Test
    public void testDoOpen() throws Exception {
        // 模拟 MongoDB 查询结果
        Document doc1 = new Document(Field.prodId, 1L);
        Document doc2 = new Document(Field.prodId, 2L);
        List<Document> mockResults = Arrays.asList(doc1, doc2);

        when(mongoTemplate.find(any(BasicQuery.class), eq(Document.class), eq(CollectionName.prod_prc_hist.name())))
                .thenReturn(mockResults);

        reader.doOpen();

        // 验证 prodIdList 是否正确填充
        assertEquals(Arrays.asList(1L, 2L), reader.prodIdList);
    }

    @Test
    public void testDoPageRead() throws Exception {
        Document prod1 = new Document(Field.prodId, 6001L);
        Document prod2 = new Document(Field.prodId, 6002L);
        Document prod3 = new Document(Field.prodId, 6003L);

        when(mongoTemplate.find(any(BasicQuery.class), eq(Document.class), eq(CollectionName.prod_prc_hist.name()))).thenReturn(Arrays.asList(prod1, prod2, prod3));
        reader.doOpen();

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
        when(mongoTemplate.aggregate(any(Aggregation.class), eq(CollectionName.prod_prc_hist.name()), eq(Document.class)))
                .thenReturn(new AggregationResults<>(Arrays.asList(doc1, doc2), new Document()));
        List<Document> results = new ArrayList<>();
        Document document;
        while ((document = reader.read()) != null) {
            results.add(document);
        }
        Assertions.assertEquals(3, results.size());
    }
}
