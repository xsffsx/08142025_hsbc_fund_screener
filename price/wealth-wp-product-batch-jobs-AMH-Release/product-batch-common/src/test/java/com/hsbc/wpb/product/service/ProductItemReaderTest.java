package com.dummy.wpb.product.service;

import com.dummy.wpb.product.constant.CollectionName;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ProductItemReaderTest {


    @Test
    void testDoPageRead() {
        // Given
        Query query = new Query();
        List<Document> productList = Arrays.asList(
                new Document("_id", 1L),
                new Document("_id", 2L),
                new Document("_id", 3L)
        );
        ProductItemReader productItemReader = new ProductItemReader(query);
        MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);
        ReflectionTestUtils.setField(productItemReader, "mongoTemplate", mongoTemplate);
        when(mongoTemplate.find(query, Document.class, CollectionName.product.name())).thenReturn(productList);

        // When
        Iterator<Document> iterator = productItemReader.doPageRead();

        // Then
        assertNotNull(iterator);
    }
}
